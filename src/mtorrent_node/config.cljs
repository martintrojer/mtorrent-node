(ns mtorrent-node.config)

(def config
  {:version "2.0"
   :watch-path "watch"
   :session-file "session/mtorrent_session"
   :lock-file "session/mtorrent_lock"}
  )

(defn get-config [k]
  (k config))
