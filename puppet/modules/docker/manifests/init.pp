class docker {
  package { 'linux-image-generic-lts-raring' : ensure => present }
  package { 'linux-headers-generic-lts-raring' : ensure => present }

  apt::source { 'docker' :
    location => 'http://get.docker.io/ubuntu',
    repos => 'main',
    release => 'docker',
    include_src => false,
    key => '36A1D7869245C8950F966E92D8576A8BA88D21E9',
    before => Package['lxc-docker'],
  }

  package { 'lxc-docker' : ensure => present }
}
