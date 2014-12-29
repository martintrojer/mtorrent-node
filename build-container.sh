sudo docker.io build -t mtorrent-node .
sudo docker.io ps -a | head

## with registry
# sudo docker.io tag IMAGE_ID regisry-host:5000/mtorrent-node
# sudo docker.io push regisry-host:5000/mtorrent-node

## export
# sudo docker.io export CONTAINER_ID > mtorrent-node.tar
# gzip mtorrent-node.tar
