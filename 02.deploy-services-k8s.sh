echo 'Start deploy all services to K8S'

kubectl apply -f deployment-main-service.yaml
kubectl apply -f deployment-user-service.yaml
kubectl apply -f deployment-book-service.yaml
kubectl apply -f deployment-book-sub-service-1.yaml
kubectl apply -f deployment-book-sub-service-2.yaml
kubectl apply -f deployment-book-sub-service-3.yaml

echo 'Finish deploy all services'