apt-get -y update
apt-get -y install software-properties-common python build-essential

# node
add-apt-repository ppa:chris-lea/node.js
apt-get -y update
apt-get -y install nodejs

# leiningen
apt-get -y install openjdk-7-jdk
if [ ! -e "/usr/local/bin/lein" ]; then
		wget -O "/usr/local/bin/lein" https://raw.github.com/technomancy/leiningen/stable/bin/lein
		chmod 0775 "/usr/local/bin/lein"
fi

# docker
apt-key adv --keyserver keyserver.ubuntu.com --recv-keys 36A1D7869245C8950F966E92D8576A8BA88D21E9
sh -c "echo deb http://get.docker.io/ubuntu docker main > /etc/apt/sources.list.d/docker.list"
apt-get -y update
apt-get -y install lxc-docker

# setup mtorrent-node dev env
apt-get -y install libtorrent-rasterbar-dev
cd /vagrant
npm install

# stuff
apt-get -y install git tree htop tmux emacs24-nox emacs24-el

cd /home/vagrant
source "/vagrant/dotfiles.sh"
