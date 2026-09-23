#!/usr/bin/env bash
curl -sS -X POST http://localhost:8081/api/orders -H 'Content-Type: application/json' -d '{"productId":"P-100","quantity":2,"amount":49.90,"failPayment":false,"failShipping":true}'
