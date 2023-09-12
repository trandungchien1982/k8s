echo 'Tien hanh Build Apps Java (Optional)'
echo 'Moi truong Gradle Java 6.5'

echo 'Build Java: main-service'
cd main-service
./gradlew build --no-daemon
cd ..


echo 'Build Java: user-service'
cd user-service
./gradlew build
cd ..


echo 'Build Java: book-service'
cd book-service
./gradlew build
cd ..


echo 'Build Java: book-sub-service-1'
cd book-sub-service-1
./gradlew build
cd ..


echo 'Build Java: book-sub-service-2'
cd book-sub-service-2
./gradlew build
cd ..


echo 'Build Java: book-sub-service-3'
cd book-sub-service-3
./gradlew build
cd ..
