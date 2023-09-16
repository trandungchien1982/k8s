# Tiến hành xoá toàn bộ resources trên 2 namspace + Ingress tương ứng ...
kubectl delete all --all --namespace dev-custom-ns
kubectl delete all --all --namespace stg-custom-ns

kubectl delete -f deploy-dev-namespace+Ingress.yaml
kubectl delete -f deploy-stg-namespace+Ingress.yaml