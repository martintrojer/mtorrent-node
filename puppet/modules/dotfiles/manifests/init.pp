define dotfiles::link {
  $parts = split($title, '/')
  file { $title :
    ensure => link,
    target => sprintf("/home/vagrant/dotfiles/%s", $parts[-1]),
    require => Exec['dotfiles'],
  }
}

class dotfiles {

  package { 'git' : ensure => present }
  ->
  exec { 'dotfiles' :
    cwd => '/home/vagrant',
    user => vagrant,
    command => '/usr/bin/git clone https://github.com/martintrojer/dotfiles.git',
    creates => '/home/vagrant/dotfiles',
  }

  link { '/home/vagrant/.tmux.conf' : }
  link { '/home/vagrant/.gitconfig' : }
  link { '/home/vagrant/.emacs.d' : }
  link { '/home/vagrant/.lein' : }
}
