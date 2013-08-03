(ns mtorrent-node.config)

;; (default) configuration

(def config
  (atom {:version "2.0"
         :ui-port 1337
         :watch-path "watch"
         :session-file "session/mtorrent_session"
         :lock-file "session/mtorrent_lock"
         :save-path "."
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
