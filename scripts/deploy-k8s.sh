#!/usr/bin/env bash
set -euo pipefail
kubectl apply -f k8s/00-namespace.yaml
kubectl apply -f k8s/01-postgres.yaml
kubectl apply -f k8s/02-kafka.yaml
kubectl apply -f k8s/03-temporal.yaml
kubectl apply -f k8s/04-order-service.yaml
kubectl apply -f k8s/05-inventory-service.yaml
kubectl apply -f k8s/06-payment-service.yaml
kubectl apply -f k8s/07-shipping-service.yaml
kubectl apply -f k8s/08-temporal-worker.yaml
kubectl -n saga-demo get pods
