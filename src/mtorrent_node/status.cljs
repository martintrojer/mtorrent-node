(ns mtorrent-node.status
  (:require [hiccups.runtime :as hiccupsrt])
  (:require-macros [hiccups.core :as hiccups]))

(defn render []
  [:h1 "status"])
