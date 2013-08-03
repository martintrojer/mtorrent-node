(ns mtorrent-node.settings
  (:require [mtorrent-node.config :as c]
            [mtorrent-node.libtorrent :as lt]))

;; TODO - replace with forms and endpoints to update

(defn render []
  (str (assoc @c/config :full-version (lt/get-version))))
