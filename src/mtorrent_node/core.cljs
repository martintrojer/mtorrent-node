(ns mtorrent-node.core
  (:require [hiccups.runtime :as hiccupsrt]
            [mtorrent-node.libtorrent :as lt]
            [mtorrent-node.status :as status]
            [mtorrent-node.add :as add])
  (:require-macros [hiccups.core :as hiccups]))

;; Page common stuff and routes

(defn include-css [name]
  [:link {:href (str "css/" name)
          :type "text/css"
          :rel "stylesheet"}])

(defn include-js [name]
  [:script {:src (str "js/" name)
            :type "text/javascript"}])

(defn header []
  [:head
   [:meta {:charset "utf-8"}]
   [:meta {:name "viewport"
            :content "width=device-width, initial-scale=1.0"}]
   [:meta {:description (lt/get-version)}]
   [:meta {:author "Martin Trojer"}]

   [:title "mtorrent-node"]

   (include-css "bootstrap.min.css")
   (include-css "mtorrent.css")])

(defn navbar [active]
  (let [lis (list [:li (when (= :status active) {:class "active"}) [:a {:href "/"} "Status"]]
                  [:li (when (= :add active) {:class "active"}) [:a {:href "add"} "Add"]]
                  [:li (when (= :settings active) {:class "active"}) [:a {:href "settings"} "Settings"]])]
    [:div {:class "navbar navbar-inverse navbar-fixed-top"}
     [:div.container
      [:button {:class "navbar-toggle"
                :type "button"
                :data-toggle "collapse"
                :data-target ".nav-collapse"}
       [:span.icon-bar]
       [:span.icon-bar]
       [:span.icon-bar]]
      [:a.navbar-brand {:href "/"} "mtorrent-node"]
      [:div {:class "nav-collapse collapse"}
       [:ul {:class "nav navbar-nav"} lis]]
      [:p.navbar-text (lt/get-version)]]]))

(defn render-settings []
  [:h1 "settings"])

(defn render-page [active]
  (hiccups/html
   (header)
   [:body
    (include-js "jquery-1.10.2.min.js")
    (include-js "bootstrap.min.js")
    (navbar active)
    [:div.container
     [:div.mtorrent
      (condp = active
        :status (status/render)
        :add (add/render)
        :settings (render-settings))]]]))

(defn -main [& args]
  (println "Starting" (lt/get-version))

  (let [express (js/require "express")
        http (js/require "http")
        path (js/require "path")
        app (express)
        dirname (js* "__dirname")
        port 1337]

    (when (= "development" (.get app "env"))
      (.use app (express/errorHandler)))

    (-> app
        (.set "port" port)
        ;; (.use (express/logger "dev"))
        (.use (express/bodyParser))
        (.use (express/methodOverride))
        (.use ((aget express "static") (path/join dirname "public")))
        (.use (express/favicon "public/img/gl.ico"))

        (.get "/" (fn [req res] (.send res (render-page :status))))
        (.get "/add" (fn [req res] (.send res (render-page :add))))
        (.get "/settings" (fn [req res] (.send res (render-page :settings))))

        (.post "/magnet" (fn [req res] (lt/add-magnet (.param req "magnet" nil)) (.redirect res "/add")))
        (.post "/url" (fn [req res] (println "url:" (.param req "url" nil)) (.redirect res "/add")))
        (.get "/pause_all" (fn [_ res] (lt/pause-all) (.redirect res "/")))
        (.get "/resume_all" (fn [_ res] (lt/resume-all) (.redirect res "/")))
        (.get "/remove_all" (fn [_ res] (lt/remove-all) (.redirect res "/")))
        (.get "/pause" (fn [req res] (lt/pause-torrent (-> req .-query .-id)) (.redirect res "/")))
        (.get "/resume" (fn [req res] (lt/resume-torrent (-> req .-query .-id)) (.redirect res "/")))
        (.get "/remove" (fn [req res] (lt/remove-torrent (-> req .-query .-id)) (.redirect res "/"))))

    (.listen (http/createServer app) port
             #(println "Server started on port" port))

    (lt/setup-session)
    (lt/restart-magnets)

    ))

(set! *main-cli-fn* -main)
