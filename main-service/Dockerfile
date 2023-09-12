FROM openjdk:8u312
MAINTAINER Chien Tran <trandungchien1982@gmail.com>

WORKDIR /usr/src/myapp
COPY "./build/libs/main-service.jar" .

# Run when creating container
CMD java -jar "main-service.jar"

# We will use port 9237
EXPOSE 9237
