FROM ubuntu:14.04

ENV DEBIAN_FRONTEND noninteractive

RUN echo "#!/bin/sh\nexit 0" > /usr/sbin/policy-rc.d

RUN apt-get -y update
RUN apt-get -y -q install bc
RUN apt-get -y -q install gdb
Run apt-get -y -q install gcc

RUN useradd -ms /bin/bash someuser

ADD ./dumper.sh /home/someuser/dumper.sh
ADD ./target.c /home/someuser/target.c
ADD ./example.c /home/someuser/example.c

USER someuser
WORKDIR /home/someuser

RUN gcc -o target ./target.c
RUN gcc -Wall -O2 -fPIC -shared example.c -ldl -Wl,-soname,libexample.so -o libexample.so

#CMD ["LD_PRELOAD=./libexample.so ./target"]
#CMD ["./target"]
