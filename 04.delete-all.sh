# Tiến hành xoá toàn bộ resources trên 2 namspace + Ingress
kubectl delete all --all --namespace dev-custom-ns
kubectl delete all --all --namespace stg-custom-ns

kubectl delete -f deploy-NS+Ingress.yaml