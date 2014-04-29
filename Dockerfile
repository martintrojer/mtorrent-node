FROM ubuntu:trusty

RUN apt-get -y install nodejs libtorrent-rasterbar7

ADD mtorrent.js mtorrent.js
ADD public public
ADD node_modules node_modules
ADD libtorrent libtorrent
ADD libtorrent-rasterbar.so.7.0.0 /usr/lib/libtorrent-rasterbar.so.7.0.0
ADD libtorrent-rasterbar.so.7 /usr/lib/libtorrent-rasterbar.so.7

EXPOSE 1337

CMD ["nodejs", "./mtorrent.js"]
