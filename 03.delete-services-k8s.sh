echo 'Start delete all services in K8S'

kubectl delete -f deployment-main-service.yaml
kubectl delete -f deployment-user-service.yaml
kubectl delete -f deployment-book-service.yaml
kubectl delete -f deployment-book-sub-service-1.yaml
kubectl delete -f deployment-book-sub-service-2.yaml
kubectl delete -f deployment-book-sub-service-3.yaml

echo 'Finish delete all services in K8S'