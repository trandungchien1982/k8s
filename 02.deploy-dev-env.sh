# Tiến hành deploy Dev Env
kubectl apply -f deploy-NS+Ingress.yaml
kubectl apply -f deploy-main-service-in-ns.yaml --namespace dev-custom-ns

