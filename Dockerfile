FROM nginxdemos/hello:0.1
MAINTAINER Chien Tran <trandungchien1982@gmail.com>

RUN echo 'Just keep the docker image in my Docker Registry :) '

# We will use port 80
EXPOSE 80