(defproject mtorrent-node "0.1.0-SNAPSHOT"
  :description "node.js - libtorrent-rasterbar torrent client"
  :url "https://github.com/martintrojer/mtorrent-node"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [hiccups "0.2.0"]]
  :plugins [[lein-cljsbuild "0.3.2"]]
  :cljsbuild {:builds [{:source-paths ["src/mtorrent_node"]
                        :compiler {:output-to "mtorrent.js"
                                   :target :nodejs
                                   :optimizations :simple
                                   :pretty-print true}}]})
