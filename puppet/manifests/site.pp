# core modules
import 'puppetlabs-stdlib'
class { 'apt': }

# apt-get update
stage { 'preinstall': before => Stage['main'] }
class apt_get_update { exec { '/usr/bin/apt-get -y update': } }
class { 'apt_get_update': stage => preinstall }

# my stuff
include nodejs
include leiningen
include docker
include dotfiles
include emacs
include build-libtorrent-rasterbar

package { 'tree' : ensure => present }
package { 'htop' : ensure => present }
package { 'tmux' : ensure => present }
