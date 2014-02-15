FROM ubuntu:precise

RUN apt-get update && apt-get -y install python-software-properties

RUN add-apt-repository ppa:chris-lea/node.js
RUN apt-get -y update
RUN apt-get -y install nodejs libtorrent-rasterbar6

ADD mtorrent.js mtorrent.js
ADD public public
ADD node_modules node_modules
ADD libtorrent libtorrent
ADD libtorrent-rasterbar.so.7.0.0 /usr/lib/libtorrent-rasterbar.so.7.0.0
ADD libtorrent-rasterbar.so.7 /usr/lib/libtorrent-rasterbar.so.7

EXPOSE 1337

CMD ["nodejs", "./mtorrent.js"]
