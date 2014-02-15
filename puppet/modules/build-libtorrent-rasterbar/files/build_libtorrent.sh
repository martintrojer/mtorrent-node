#!/bin/bash

VERSION="0.16.14"

wget -O libtorrent-rasterbar-$VERSION.tar.gz "http://downloads.sourceforge.net/project/libtorrent/libtorrent/libtorrent-rasterbar-$VERSION.tar.gz?r=http%3A%2F%2Fsourceforge.net%2Fprojects%2Flibtorrent%2Ffiles%2Flibtorrent%2F&ts=1392480392&use_mirror=netcologne"
tar zxf libtorrent-rasterbar-$VERSION.tar.gz
cd libtorrent-rasterbar-$VERSION
./configure --prefix=/usr
make install
cd /vagrant
cp /usr/lib/libtorrent-rasterbar.so.7.0.0 .
strip libtorrent-rasterbar.so.7.0.0
ln -s libtorrent-rasterbar.so.7.0.0 libtorrent-rasterbar.so.7
