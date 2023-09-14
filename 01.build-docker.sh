echo 'Tien hanh Build Docker cho cac Service'

echo 'Login vao Docker su dung Credential mac dinh tu truoc'
docker login


echo 'Build spring-rest-project for using ConfigMap'
export MAIN_SERVICE=tdchien1982/k8s:use-config-map-v1
cd spring-rest-project
docker build . -t $MAIN_SERVICE
docker push $MAIN_SERVICE
cd ..