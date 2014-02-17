(ns mtorrent-node.config)

;; (default) configuration

(def config
  (atom {:version "2.1"
         :ui-port 1337
         :watch-path "/data/watch"
         :session-file "/data/session/mtorrent_session"
         :lock-file "/data/session/mtorrent_lock"
         :save-path "/data"
         :dht-routers [["router.bittorrent.com" 6881]
                       ["router.utorrent.com" 6881]
                       ["router.bitcomet.com" 6881]]
         :listen [6881, 6889]
         :max-connections 50
         :max-uploads -1
         :ratio 0.0
         :upload-limit 0
         :download-limit 0
         :resolve-countries false
         }))

(defn get-config [k]
  (k @config))
