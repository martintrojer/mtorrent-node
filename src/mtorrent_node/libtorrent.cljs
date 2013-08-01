(ns mtorrent-node.libtorrent
  (:require [mtorrent-node.config :as c]))

(def lt (js/require "./libtorrent/libtorrent"))
(def session (atom nil))
(def torrents (atom {}))

(defn get-version []
  (str "mtorrent/" (c/get-config :version) "-" "libtorrent/" (.-version lt)))

(defn restore-session-state [session]
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

(defn save-session-state [session]
  (try
    (let [fs (js/require "fs")
          fd (fs/openSync (c/get-config :session-file) "w")]
      (fs/writeSync fd (.bencode lt (.save_state session)))
      (println "Saved session state"))
    (catch js/Object e
      (println "Unable to save session state"))))

(defn start-extensions [session]
  (.add_extension session "ut_metadata")
  (.add_extension session "ut_pex")
  (.add_extension session "smart_ban")
  ;;(.add_extension session "lt_trackers")
  ;;(.add_extension session "metadata_transfer")
  )

(defn start-dht [session]
  (doseq [[r p] (c/get-config :dht-routers)]
    (.add_dht_router session r p))
  (.start_dht session))

(defn setup-session []
  (let [lt (js/require "./libtorrent/libtorrent")
        s (.-session lt)
        sess (new s)]
    (restore-session-state sess)
    (start-extensions sess)
    (start-dht sess)

    (.listen_on sess (into-array (c/get-config :listen)))
    ;;(.start_upnp sess)
    (reset! session sess)))

(defn save-torrent-state [session]
  ;; TODO
  )

(defn get-torrent-state [session info-hash]
  ;; TODO
  )

(defn teardown-session []
  (when @session
    (println "Stopping libtorrent session")
    (.pause @session)
    (save-torrent-state @session)
    (save-session-state @session)

    (reset! session nil)))

(defn create-magnet-restart-file [uri info-hash]
  (try
    (let [fs (js/require "fs")
          fname (str (c/get-config :watch-path) "/" info-hash ".magnet")
          fd (fs/openSync fname "w")]
      (fs/writeSync fd uri)
      (println "Create magnet restart-file" fname))
    (catch js/Object e
      (println "Unable to create restart-file" info-hash))))

(defn setup-handle [handle]
  (.set_max_connections handle (c/get-config :max-connections))
  (.set_max_uploads handle (c/get-config :max-uploads))
  ;;(.set_ratio handle (c/get-config :ratio))
  (.set_upload_limit handle (c/get-config :upload-limit))
  (.set_download_limit handle (c/get-config :download-limit))
  (.resolve_countries handle (c/get-config :resolve-countries)))

(defn add-magnet [uri]
  (when-not ((-> @torrents keys set) uri)
    (try
      (let [p {:save_path (c/get-config :save-path)
               :resume_data (get-torrent-state session "")
               :paused false
               :duplicate_is_error false
               :auto_managed true
               :url uri}
            handle (.add_torrent @session p)
            info-hash (.info_hash handle)]
        (setup-handle handle)
        (println "Magnet added" info-hash)
        (swap! torrents assoc uri handle)
        (create-magnet-restart-file uri info-hash))
      (catch js/Object e
        (println "Error adding manget" uri)))))

(def statuses
  ["queued for checking"
   "checking files"
   "downloading metadata"
   "downloading"
   "finished"
   "seeding"
   "allocating"
   "checking resume data"])

(defn get-state []
  (for [h (vals @torrents)
        :let [s (.status h)]]
    {:name (.name h)
     :hash (str (.info_hash h))
     ;;:size (try (-> h .get_torrent_info .total_size) (catch js/Object e 0))
     :status (or (get statuses (.-state s)) "unknown")
     :progress (* (.-progress s) 100)
     :down-rate (.-download_rate s)
     :up-rate (.-upload_rate s)
     :seeds (.-num_seeds s)
     :seeds-total (.-list_seeds s)
     :peers (.-num_peers s)
     :peers-total (.-list_peers s)
     ;;:is-paused (.is_paused h)
     }))
