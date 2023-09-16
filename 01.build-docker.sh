# Tiến hành build Docker để có 1 stable Docker image được host trên registry của chính mình

export MAIN_DOCKER_IMAGE=tdchien1982/k8s:nginxdemos-hello-1.0
echo "Build Docker Image from base [nginxdemos/hello:0.1], to be [$MAIN_DOCKER_IMAGE]"

docker login

echo "Start build Docker Image: $MAIN_DOCKER_IMAGE"
docker build . -t $MAIN_DOCKER_IMAGE

echo 'Push the result Docker Image into Docker Registry ...'
docker push $MAIN_DOCKER_IMAGE

echo "Finish push Docker Image data $MAIN_DOCKER_IMAGE"

