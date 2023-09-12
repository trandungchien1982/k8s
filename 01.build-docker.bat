echo 'Tien hanh Build Docker cho cac Service'

echo 'Login vao Docker su dung Credential mac dinh tu truoc'
docker login


echo 'Build main-service'
SET MAIN_SERVICE=tdchien1982/k8s:service_to_service-main
cd main-service
docker build . -t %MAIN_SERVICE%
docker push %MAIN_SERVICE%
cd ..


echo 'Build book-service'
SET BOOK_SERVICE=tdchien1982/k8s:service_to_service-book
cd book-service
docker build . -t %BOOK_SERVICE%
docker push %BOOK_SERVICE%
cd ..


echo 'Build user-service'
SET USER_SERVICE=tdchien1982/k8s:service_to_service-user
cd user-service
docker build . -t %USER_SERVICE%
docker push %USER_SERVICE%
cd ..


echo 'Build book-sub-service-1'
SET BOOK_SUB_SERVICE_1=tdchien1982/k8s:service_to_service-book.sub.01
cd book-sub-service-1
docker build . -t %BOOK_SUB_SERVICE_1%
docker push %BOOK_SUB_SERVICE_1%
cd ..


echo 'Build book-sub-service-2'
SET BOOK_SUB_SERVICE_2=tdchien1982/k8s:service_to_service-book.sub.02
cd book-sub-service-2
docker build . -t %BOOK_SUB_SERVICE_2%
docker push %BOOK_SUB_SERVICE_2%
cd ..


echo 'Build book-sub-service-3'
SET BOOK_SUB_SERVICE_3=tdchien1982/k8s:service_to_service-book.sub.03
cd book-sub-service-3
docker build . -t %BOOK_SUB_SERVICE_3%
docker push %BOOK_SUB_SERVICE_3%
cd ..
