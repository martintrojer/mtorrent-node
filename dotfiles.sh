echo "
[user]
  name = Martin Trojer
  email = martin.trojer@gmail.com
[color]
  ui = true
[http]
  sslVerify = false
  postBuffer = 524288000
" > .gitconfig

echo "setw -g xterm-keys on
setw -g default-terminal \"screen-256color\"" > .tmux.conf

if [ ! -d .emacs.d ]; then
		git clone https://github.com/martintrojer/.emacs.d.git
fi

chown -R vagrant /home/vagrant/.emacs.d
