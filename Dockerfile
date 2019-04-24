FROM ubuntu:14.04

ENV DEBIAN_FRONTEND noninteractive

RUN echo "#!/bin/sh\nexit 0" > /usr/sbin/policy-rc.d

RUN apt-get -y update
RUN apt-get -y -q install gdb
Run apt-get -y -q install gcc

ADD ./target.c /root/target.c
ADD ./example.c /root/example.c

WORKDIR /root

RUN gcc -o target ./target.c
RUN gcc -Wall -O2 -fPIC -shared example.c -ldl -Wl,-soname,libexample.so -o libexample.so

#CMD ["LD_PRELOAD=libexample.so target"]
#CMD ["./target"]
