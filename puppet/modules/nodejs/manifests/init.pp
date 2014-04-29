class nodejs {
  package { ['nodejs', 'npm'] : ensure => present }
  package { ['python', 'build-essential'] : ensure => present }
  exec { 'node-gyp' :
    command => '/usr/bin/npm install -g node-gyp',
    creates => '/usr/bin/node-gyp',
    require => Package['npm'],
  }
}
