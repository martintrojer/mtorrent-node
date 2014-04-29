include dotfiles

include nodejs
include build-libtorrent-rasterbar
include leiningen

package { 'docker.io' : ensure => present }
package { ['emacs24-nox', 'emacs24-el', 'emacs24-common-non-dfsg'] : ensure => present }
package { ['tree', 'htop', 'tmux'] : ensure => present }
