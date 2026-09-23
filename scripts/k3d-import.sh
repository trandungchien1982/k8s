#!/usr/bin/env bash
set -euo pipefail
CLUSTER=${1:-tdc-cluster}
for svc in order-service inventory-service payment-service shipping-service temporal-worker; do
  k3d image import "saga-demo/$svc:1.0.0" -c "$CLUSTER"
done
