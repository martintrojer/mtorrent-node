class build-libtorrent-rasterbar {
  package { 'libboost-dev' : ensure => present }
  package { 'libboost-system-dev' : ensure => present }
  package { 'libboost-filesystem-dev' : ensure => present }
  package { 'libboost-thread-dev' : ensure => present }
  package { 'libssl-dev' : ensure => present }
  package { 'zlib1g-dev' : ensure => present }
  package { 'libgeoip-dev' : ensure => present }
  package { 'pkg-config' : ensure => present }

  file { 'build_libtorrent.sh' :
    name => '/home/vagrant/build_libtorrent.sh',
    source => 'puppet:///modules/build-libtorrent-rasterbar/build_libtorrent.sh',
    owner => vagrant,
    mode => 0755,
    ensure => present,
  }
}
