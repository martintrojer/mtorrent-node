## import
# cat mtorrent-node.tar.gz | sudo docker.io import - mtorrent-node

sudo docker.io run -v /vagrant:/data:rw -p 1337:1337 mtorrent-node
