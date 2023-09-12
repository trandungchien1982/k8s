FROM openjdk:8u312
MAINTAINER Chien Tran <trandungchien1982@gmail.com>

WORKDIR /usr/src/myapp
COPY "./build/libs/book-service.jar" .

# Run when creating container
CMD java -jar "book-service.jar"

# We will use port 9239
EXPOSE 9239
