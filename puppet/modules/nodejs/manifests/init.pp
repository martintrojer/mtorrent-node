class nodejs {
  apt::ppa { 'ppa:chris-lea/node.js' :
    before => Package['nodejs'],
  }

  package { 'nodejs' : ensure => present }
  package { 'python' : ensure => present }
  package { 'build-essential' : ensure => present }
  exec { 'node-gyp' :
    command => '/usr/bin/npm install -g node-gyp',
    creates => '/usr/bin/node-gyp',
    require => Package['nodejs'],
  }
}
