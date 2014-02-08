# -*- mode: ruby -*-
# vi: set ft=ruby :

VAGRANTFILE_API_VERSION = "2"

Vagrant.configure(VAGRANTFILE_API_VERSION) do |config|
  config.vm.box = "raring64"
  config.vm.box_url = "http://goo.gl/ceHWg"

  config.ssh.forward_agent = true

  config.vm.provider :virtualbox do |vb|
    vb.customize ["modifyvm", :id, "--memory", "1024"]
  end

  config.vm.network :forwarded_port, guest: 1337, host: 1337

  config.vm.provision "shell", path: "provision.sh"

end
