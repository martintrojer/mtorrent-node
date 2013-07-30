(ns mtorrent-node.core
  (:require [cljs.core.async :as async :refer [<! >! chan close! timeout]]
            [hiccups.runtime :as hiccupsrt])
  (:require-macros [cljs.core.async.macros :refer [go alt!]]
                   [hiccups.core :as hiccups]))


(defn -main [& args]

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
        (.get "/" (fn [req res] (.send res (hiccups/html [:h1 "Hello"]))))
        (.get "/foo" (fn [req res] (.send res (hiccups/html [:h1 "Foo"])))))

    (.listen (http/createServer app) port
             #(println "Server started on port" port))
    ))

(set! *main-cli-fn* -main)
