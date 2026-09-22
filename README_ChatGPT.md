# Argo CD + kube-prometheus-stack demo

Demo GitOps repository for deploying kube-prometheus-stack with Argo CD and monitoring two sample applications: `orders` and `users`.

## Structure

- `argocd/monitoring.yaml`: Argo CD Application for kube-prometheus-stack Helm chart.
- `argocd/demo-apps.yaml`: Argo CD Application for sample workloads.
- `monitoring/values.yaml`: Helm values for kube-prometheus-stack.
- `apps/`: Deployments, Services and ServiceMonitors for orders/users.

## Before use

1. Replace `https://github.com/YOUR_ORG/YOUR_REPO.git` in `argocd/demo-apps.yaml` with your Git repository URL.
2. Check `storageClassName` requirements in `monitoring/values.yaml`. The example leaves it unset so the cluster default StorageClass is used.
3. Replace the sample application images with real images that expose `/actuator/prometheus` on port 8080.
4. Ensure Argo CD is already installed and its namespace is `argocd`.

## Bootstrap

Apply monitoring first so Prometheus Operator CRDs such as ServiceMonitor exist:

```bash
kubectl apply -f argocd/monitoring.yaml
```

Wait until kube-prometheus-stack is synced/healthy, then:

```bash
kubectl apply -f argocd/demo-apps.yaml
```

## Verify

```bash
kubectl get pods -n monitoring
kubectl get pods -n apps -o wide
kubectl get servicemonitor -n apps
kubectl get endpointslice -n apps
```

Port-forward Prometheus:

```bash
kubectl port-forward -n monitoring svc/monitoring-kube-prometheus-prometheus 9090:9090
```

Then check Prometheus targets.
