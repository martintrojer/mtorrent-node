(ns mtorrent-node.settings
  (:require [mtorrent-node.config :as c]
            [clojure.string :as s]))

(defn render []
  (str @c/config))
