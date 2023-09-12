FROM openjdk:8u312
MAINTAINER Chien Tran <trandungchien1982@gmail.com>

WORKDIR /usr/src/myapp
COPY "./build/libs/user-service.jar" .

# Run when creating container
CMD java -jar "user-service.jar"

# We will use port 9238
EXPOSE 9238
