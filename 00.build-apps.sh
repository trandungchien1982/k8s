echo 'Tien hanh Build Apps Java (Optional)'
echo 'Moi truong Gradle Java 6.5'

echo 'Build Java: spring-rest-project'
cd spring-rest-project
./gradlew build --no-daemon
cd ..
