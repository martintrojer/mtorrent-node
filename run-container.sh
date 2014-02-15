## import
# cat mtorrent-node.tar.gz | sudo docker import - mtorrent-node

sudo docker run -rm=true -v /vagrant:/data:rw -p 1337:1337 mtorrent-node
