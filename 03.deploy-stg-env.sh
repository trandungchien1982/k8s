# Tiến hành deploy Stg Env
kubectl apply -f deploy-stg-namespace+Ingress.yaml
kubectl apply -f deploy-main-service-in-ns.yaml --namespace stg-custom-ns

