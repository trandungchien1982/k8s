#!/usr/bin/env bash
set -euo pipefail
for svc in order-service inventory-service payment-service shipping-service temporal-worker; do
  docker build -f "$svc/Dockerfile" -t "saga-demo/$svc:1.0.0" .
done
