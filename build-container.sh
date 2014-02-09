sudo docker build -t mtorrent-node .
sudo docker ps -a | head

## export

# sudo docker export CONTAINER_ID > mtorrent-node.tar
# gzip mtorrent-node.tar

## import

# cat mtorrent-node.tar.gz | sudo docker import - mtorrent-node
