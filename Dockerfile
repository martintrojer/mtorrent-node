FROM ubuntu:trusty

RUN apt-get -y update
RUN apt-get -y install nodejs libtorrent-rasterbar7

ADD mtorrent.js mtorrent.js
ADD public public
ADD node_modules node_modules
ADD node_modules/node-libtorrent-mt/build/Release/libtorrent.node libtorrent/libtorrent.node
ADD libtorrent-rasterbar.so.8.0.0 /usr/lib/libtorrent-rasterbar.so.8.0.0
ADD libtorrent-rasterbar.so.8 /usr/lib/libtorrent-rasterbar.so.8

EXPOSE 1337

CMD ["nodejs", "./mtorrent.js"]
