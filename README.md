# mtorrent-node

Hipster cousin of [mtorrent](https://github.com/martintrojer/mtorrent)

Written in [ClojureScript](https://github.com/clojure/clojurescript), running on [Node](http://nodejs.org/), using [libtorrent-rasterbar](http://www.rasterbar.com/products/libtorrent)

## Building

```
$ apt-get install libtorrent-rasterbar-dev
$ npm install
$ lein cljsbuild once
```

## Usage

```
$ mkdir watch
$ mkdir session
$ screen node mtorrent.js
```

Point your browser to `http://locahost:1337`

## License

Copyright © 2013 Martin Trojer

Distributed under the Eclipse Public License, the same as Clojure.
