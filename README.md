# Spring Boot + Temporal + Kafka + PostgreSQL Saga Orchestration Demo

Production-oriented learning demo of the Saga pattern using orchestration on Kubernetes.

## Architecture

```text
Client
  |
  v
Order Service -- durable start request --> DB
  |                                      |
  |                               Workflow Starter
  |                                      |
  +-------------------------------> Temporal
  |                                      |
  |                                      v
  |                                Temporal Worker (3 replicas in K8s)
  |                                      |
  |                +---------------------+--------------------+
  |                |                     |                    |
  |                v                     v                    v
  |          Inventory Service     Payment Service      Shipping Service
  |                |                     |                    |
  |                v                     v                    v
  |           inventory DB          payment DB           shipping DB
  |
  +--> orders DB

Order creation transaction:
  order row + outbox_event + workflow_start_request

Every business service:
  local DB transaction -> business row + outbox_event
                                    |
                                    v
                                  Kafka
                              topic saga.events
```

Temporal owns workflow state, retry, compensation ordering, and recovery after worker/pod restarts. Order workflow startup is also made restart-safe by persisting `workflow_start_request` before dispatch; the fixed Temporal workflow ID makes repeated dispatch idempotent. Kafka is intentionally not used as the Saga coordinator; it carries integration/domain events emitted through the outbox pattern.

## Saga flow

```text
Create order
  -> reserve inventory
  -> charge payment
  -> create shipment
  -> COMPLETED
```

Compensation:

```text
payment failure:
  release inventory -> FAILED

shipping failure:
  refund payment -> release inventory -> FAILED
```

All participant endpoints use `orderId` as the idempotency/business key. Repeating reserve/charge/create returns the existing record. Compensation methods are also idempotent at business-state level.

## Versions used

- Java 21
- Spring Boot 4.1.1
- Temporal Java SDK 1.38.0
- Kafka 4.1.0 image for the demo
- PostgreSQL 17

The Java SDK 1.38.0 is a stable Temporal release from August 2026. Spring Boot 4.1.1 was released in August 2026.

## Run with Docker Compose

```bash
docker compose up --build
```

Ports:

| Component | Port |
|---|---:|
| Order API | 8081 |
| Inventory | 8082 |
| Payment | 8083 |
| Shipping | 8084 |
| Temporal worker actuator | 8085 |
| Temporal UI | 8088 |
| PostgreSQL | 5432 |
| Kafka | 9092 |

### Success

```bash
./scripts/test-success.sh
```

Or:

```bash
curl -X POST http://localhost:8081/api/orders \
  -H 'Content-Type: application/json' \
  -d '{
    "productId":"P-100",
    "quantity":2,
    "amount":49.90,
    "failPayment":false,
    "failShipping":false
  }'
```

Expected eventual state:

```json
{"status":"COMPLETED"}
```

Query it using the returned `orderId`:

```bash
curl http://localhost:8081/api/orders/1
```

### Simulate payment rejection

```bash
./scripts/test-payment-failure.sh
```

Expected Saga path:

```text
reserve inventory ✓
charge payment ✗ (HTTP 422 -> Temporal non-retryable business error)
release inventory ✓
order FAILED
```

### Simulate shipping rejection

```bash
./scripts/test-shipping-failure.sh
```

Expected Saga path:

```text
reserve inventory ✓
charge payment ✓
create shipment ✗
refund payment ✓
release inventory ✓
order FAILED
```

## Retry behavior

The worker configures activity retry:

```text
initial interval: 1 second
backoff: 2x
maximum attempts: 3
start-to-close: 10 seconds
```

HTTP 4xx from a participant is converted to a Temporal non-retryable `ApplicationFailure`. Infrastructure failures such as connection errors and typical 5xx errors remain retryable.

## Transactional Outbox

Each service writes an `outbox_event` row inside the same local transaction as its business state change. A scheduled publisher sends unpublished rows to Kafka topic `saga.events` and marks them published only after the Kafka send completes. `order-service` includes a demo Kafka listener (`SagaEventObserver`) so the integration events are directly visible in logs.

This demo implements the core outbox atomic-write property. A hardened production implementation commonly replaces polling with CDC/Debezium or adds stronger publisher locking/claiming semantics for multiple service replicas.

## Kubernetes

Build images:

```bash
./scripts/build-images.sh
```

For k3d:

```bash
./scripts/k3d-import.sh tdc-cluster
```

Deploy:

```bash
./scripts/deploy-k8s.sh
```

Check:

```bash
kubectl -n saga-demo get pods
kubectl -n saga-demo get svc
```

Port-forward:

```bash
kubectl -n saga-demo port-forward svc/order-service 8081:8080
kubectl -n saga-demo port-forward svc/temporal-ui 8088:8080
```

Then execute the same curl commands as the Compose demo.

## Important production notes

1. The demo uses one PostgreSQL server with separate logical databases. Production ownership can place each service DB on independent clusters/instances.
2. The Kafka StatefulSet is single-node for learning. Production Kafka needs a multi-broker architecture, durable storage, security, quotas, and monitoring; a managed Kafka offering is also common.
3. The Temporal deployment uses `auto-setup` and one replica for a compact lab. Use Temporal's supported Helm deployment or Temporal Cloud for production HA.
4. The workflow-start dispatcher is durable and idempotent through a fixed workflow ID, but the polling query is still a compact demo implementation; a high-throughput deployment should add row claiming/locking.
5. The demo outbox publisher is intentionally simple. With two replicas, both can observe the same unpublished row before one commits. Kafka consumers must remain idempotent; production should use row claiming/locking or CDC.
6. Saga compensation provides business recovery and eventual consistency. It does not provide distributed ACID isolation.
7. Workflow code must preserve Temporal determinism. Keep network/database calls inside Activities, as this project does.
8. Do not stack retries blindly. The Temporal activity layer owns retries in this demo; participant services do not add another generic retry layer.

## Useful inspection commands

```bash
kubectl -n saga-demo logs deploy/temporal-worker -f
kubectl -n saga-demo logs deploy/payment-service -f
kubectl -n saga-demo logs deploy/shipping-service -f
```

Open Temporal UI at http://localhost:8088 after port-forwarding and inspect workflow IDs such as `order-saga-1`.
