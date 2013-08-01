(ns mtorrent-node.libtorrent
  (:require [mtorrent-node.config :as c]))

(defn get-version []
  (let [lt (js/require "./libtorrent/libtorrent")]
    (str "mtorrent/" (c/get-config :version) "-" "libtorrent/" (.-version lt))))

(defn load-session-state [lt session]
  (try
    (let [fs (js/require "fs")
          fd (fs/openSync (c/get-config :session-file) "r")
          buf (js/Buffer. 16384)
          r (fs/readSync fd buf 0 (.-length buf) 0)]
      (when (> r 0)
        (.load_state session (.bdecode lt buf))
        (println "Restored session state")))
    (catch js/Object e
      (println "Unable to restore session state"))))

(defn start-extensions [session]
  (.add_extension session "ut_metadata")
  (.add_extension session "ut_pex")
  (.add_extension session "smart_ban")
  ;;(.add_extension session "lt_trackers")
  ;;(.add_extension session "metadata_transfer")
  )

(defn do-stuff []
  (let [lt (js/require "./libtorrent/libtorrent")
        s (.-session lt)
        session (new s)]
    (load-session-state lt session)
    (start-extensions session)
    ))
