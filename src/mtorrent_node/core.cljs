(ns mtorrent-node.core
  (:require [hiccups.runtime :as hiccupsrt]
            [cljs.core.async :as async :refer [<! >! chan close! timeout]]
            [mtorrent-node.libtorrent :as lt]
            [mtorrent-node.status :as status]
            [mtorrent-node.add :as add])
  (:require-macros [hiccups.core :as hiccups]
                   [cljs.core.async.macros :as m :refer [go alt!]]))

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
       [:ul {:class "nav navbar-nav"} lis]]]]))

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
        (.use (express/logger "dev"))
        (.use (express/bodyParser))
        (.use (express/methodOverride))
        (.use ((aget express "static") (path/join dirname "public")))
        (.get "/" (fn [req res] (.send res (render-page :status))))
        (.get "/add" (fn [req res] (.send res (render-page :add))))
        (.get "/settings" (fn [req res] (.send res (render-page :settings))))
        (.post "/magnet" (fn [req res]
                           (lt/add-maget (.param req "magnet" nil))
                           (.redirect res "/add")))
        (.post "/url" (fn [req res]
                        (println "url:" (.param req "url" nil))
                        (.redirect res "/add")))
        )

    (.listen (http/createServer app) port
             #(println "Server started on port" port))

    (lt/setup-session)

    (lt/add-magnet "magnet:?xt=urn:btih:55650db7cbb64f2c0596ecdeca4b5751e361efb5&dn=Suits+S03E03+HDTV+x264-EVOLVE%5Bettv%5D&tr=udp%3A%2F%2Ftracker.publicbt.com%3A80%2Fannounce&tr=udp%3A%2F%2Ftracker.openbittorrent.com%2F&tr=udp%3A%2F%2Ftracker.ccc.de%2F&tr=http%3A%2F%2Ftracker.torrentbay.to%3A6969%2Fannounce&tr=udp%3A%2F%2Ftracker.istole.it%2F&tr=http%3A%2F%2Fwww.h33t.com%3A3310%2Fannounce&tr=http%3A%2F%2Fcpleft.com%3A2710%2Fannounce&tr=http%3A%2F%2Fputo.me%3A6969%2Fannounce&tr=http%3A%2F%2Ftracker.coppersurfer.tk%3A6969%2Fannounce"
                   )


    #_(go
       (<! (timeout 2000))
       (lt/teardown-session))
    ))

(set! *main-cli-fn* -main)
