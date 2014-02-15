class dotfiles {
  File {
    owner => vagrant,
    group => vagrant,
    mode => 0644,
  }

  file { '.gitconfig' :
    name => '/home/vagrant/.gitconfig',
    ensure => present,
    source => 'puppet:///modules/dotfiles/.gitconfig',
  }

  file { '.tmux.conf' :
    name => '/home/vagrant/.tmux.conf',
    ensure => present,
    source => 'puppet:///modules/dotfiles/.tmux.conf',
  }

  package { 'git' : ensure => present }

  exec { '.emacs.d' :
    cwd => '/home/vagrant',
    user => vagrant,
    command => '/usr/bin/git clone https://github.com/martintrojer/.emacs.d.git',
    creates => '/home/vagrant/.emacs.d',
    require => Package['git'],
  }
}
