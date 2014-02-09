FROM triangle/ubuntu-saucy

RUN apt-get update && apt-get -y install software-properties-common

RUN add-apt-repository ppa:chris-lea/node.js
RUN apt-get -y update
RUN apt-get -y install nodejs libtorrent-rasterbar7

ADD mtorrent.js mtorrent.js
ADD public public
ADD node_modules node_modules
ADD libtorrent libtorrent

EXPOSE 1337

CMD ["nodejs", "./mtorrent.js"]
