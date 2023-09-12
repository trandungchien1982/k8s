FROM openjdk:8u312
MAINTAINER Chien Tran <trandungchien1982@gmail.com>

WORKDIR /usr/src/myapp
COPY "./build/libs/book-sub-service-1.jar" .

# Run when creating container
CMD java -jar "book-sub-service-1.jar"

# We will use port 9241
EXPOSE 9241
