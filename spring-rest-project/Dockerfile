FROM openjdk:8u312
MAINTAINER Chien Tran <trandungchien1982@gmail.com>

WORKDIR /usr/src/myapp
COPY "./build/libs/spring-rest-project-0.1.0.jar" .

# Run when creating container
CMD java -jar "spring-rest-project-0.1.0.jar"

# We will use port 9234
EXPOSE 9234
