# mtorrent-node

Hipster cousin of [mtorrent](https://github.com/martintrojer/mtorrent)

Written in [ClojureScript](https://github.com/clojure/clojurescript), running on [Node](http://nodejs.org/), using [libtorrent-rasterbar](http://www.rasterbar.com/products/libtorrent)

## Building

Make sure you have [vagrant-vbguest](https://github.com/dotless-de/vagrant-vbguest) installed.

```
$ git submodule init
$ git submodule update
$ vagrant up
$ vagrant reload
$ vagrant ssh
$ sudo ./build_libtorrent.sh
$ cd /vagrant
$ npm install
$ lein cljsbuild once
```

You may want to change `puppet/modules/dotfiles`

## Usage

```
$ mkdir watch
$ mkdir session
$ screen node mtorrent.js
```

Point your browser to `http://locahost:1337`

## Docker container

You can put mtorrent-node in a docker container for easy deployment.

```
$ ./build-container.sh
$ ./run-container.sh
```

## License

Copyright © 2013 Martin Trojer

Distributed under the Eclipse Public License, the same as Clojure.
