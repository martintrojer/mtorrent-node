(ns mtorrent-node.libtorrent
  (:require [mtorrent-node.config :as c]))

;; libtorrent and fs stuff

(def lt (js/require "./libtorrent/libtorrent"))
(def session (atom nil))
(def torrents (atom {}))

(defn get-version []
  (str "mtorrent/" (c/get-config :version) "-" "libtorrent/" (.-version lt)))

(defn readable-size [size]
  (loop [size size, units ["B", "kB", "MB", "GB", "TB", "PB", "EB", "ZB", "YB"]]
    (if (< size 1024)
      (format "%.1f%s" size (first units))
      (recur (/ size 1024.0) (rest units)))))

(defn restore-session-state [session]
  (try
    (let [fs (js/require "fs")
          data (fs/readFileSync (c/get-config :session-file))]
      (when (> (.-length data) 0)
        (.load_state session (.bdecode lt data))
        (println "Restored session state")))
    (catch js/Object e
      (println "Unable to restore session state"))))

(defn save-session-state [session]
  (try
    (let [fs (js/require "fs")]
      (fs/writeFileSync (c/get-config :session-file) (.bencode lt (.save_state session)))
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
  (let [s (.-session lt)
        sess (new s)]
    (restore-session-state sess)
    (start-extensions sess)
    (start-dht sess)

    (.listen_on sess (into-array (c/get-config :listen)))
    ;;(.start_upnp sess)
    (reset! session sess)))

(defn save-torrent-resume-data [session]
  ;; TODO
  )

(defn get-torrent-resume-data [session info-hash]
  ;; TODO
  )

(defn teardown-session []
  (when @session
    (println "Stopping libtorrent session")
    (.pause @session)
    (save-torrent-resume-data @session)
    (save-session-state @session)

    (reset! session nil)))

(defn create-magnet-restart-file [uri info-hash]
  (try
    (let [fs (js/require "fs")
          fname (str (c/get-config :watch-path) "/" info-hash ".magnet")]
      (fs/writeFileSync fname uri)
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

(def torrent-params
  {:save_path (c/get-config :save-path)
   :paused false
   :duplicate_is_error false
   :auto_managed true})

(defn add-magnet [uri]
  (when-not ((-> @torrents keys set) uri)
    (try
      (let [p (assoc torrent-params
                ;;:resume_data (get-torrent-resume-data session ???)
                :url uri)
            handle (.add_torrent @session (clj->js p))
            info-hash (.info_hash handle)]
        (setup-handle handle)
        (println "Magnet added" info-hash)
        (swap! torrents assoc uri handle)
        (create-magnet-restart-file uri info-hash))
      (catch js/Object e
        (println "Error adding manget" uri)))))

(defn add-torrent [fname]
  (try
    (when-not ((-> @torrents keys set) fname)
      (let [fs (js/require "fs")
            ti (.-torrent_info lt)
            ti (new ti fname)
            p (assoc torrent-params :ti ti
                     ;;:resume_data (get-torrent-resume-data session ???)
                     )
            handle (.add_torrent @session (clj->js p))
            info-hash (.info_hash handle)]
        (setup-handle handle)
        (println "Torrent added" info-hash)
        (swap! torrents assoc fname handle)))
    (catch js/Object e
      (println "Error adding torrent" fname))))

(defn get-status-string [status]
  (let [statuses ["queued for checking"
                  "checking files"
                  "downloading metadata"
                  "downloading"
                  "finished"
                  "seeding"
                  "allocating"
                  "checking resume data"]]
       (or (get statuses (.-state status)) "unknown")))

(defn get-state []
  (for [h (vals @torrents)
        :let [s (.status h)]]
    {:name (.name h)
     :hash (str (.info_hash h))
     :size (readable-size
            (if (#{"downloading" "seeding"} (get-status-string s))
              (-> h .get_torrent_info .total_size)
              0))
     :status (get-status-string s)
     :progress (* (.-progress s) 100)
     :down-rate (readable-size (.-download_rate s))
     :up-rate (readable-size (.-upload_rate s))
     :seeds (.-num_seeds s)
     :seeds-total (.-list_seeds s)
     :peers (.-num_peers s)
     :peers-total (.-list_peers s)
     ;;:is-paused (.is_paused h)
     }))
