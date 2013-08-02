(ns mtorrent-node.settings
  (:require [mtorrent-node.config :as c]
            [clojure.string :as s]))

(defn render []
  (s/replace
   (.stringify js/JSON
               (clj->js @c/config)
               js/undefined 2)
   #"[\n]" "<br/>"))
