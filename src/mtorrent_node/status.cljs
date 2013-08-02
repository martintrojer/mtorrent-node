(ns mtorrent-node.status
  (:require [mtorrent-node.libtorrent :as lt]))

;; the status page

(defn get-action-button [type & msgs]
  [:div.btn-toolbar
   [:div.btn-group
    [:button {:class (str "btn btn-small dropdown-toggle " type)
              :data-toggle "dropdown"}
     "Action "
     [:span.caret]]
    [:ul.dropdown-menu
     (for [[p m] msgs]
       [:li [:a {:href p} m]])]]])

(defn render []
  [:div
   [:table {:class "table table-hover table-condensed"}
    [:thead
     [:tr
      [:th "Name"]
      [:th "Size"]
      [:th "Status"]
      [:th "Progress"]
      [:th "Seeds"]
      [:th "Peers"]
      [:th "Down"]
      [:th "Up"]
      [:th (get-action-button "btn-danger"
                              ["/pause_all" "Pause All"]
                              ["/resume_all" "Resume All"]
                              ["/remove_all" "Remove All"])]]]
    [:tbody
     (for [s (lt/get-state)]
       [:tr
        [:td (:name s)]
        [:td (:size s)]
        [:td (:status s)]
        [:td
         [:div.progress
          [:div {:class "progress-bar progress-bar-success"
                 :style (str "width: " (:progress s) "%;")}]]]
        [:td (str (:seeds s) "(" (:seeds-total s) ")")]
        [:td (str (:peers s) "(" (:peers-total s) ")")]
        [:td (str (:down-rate s))]
        [:td (str (:up-rate s))]
        [:td (get-action-button "btn-primary"
                                [(str "/pause?id=" (:hash s)) "Pause"]
                                [(str "/resume?id=" (:hash s)) "Resume"]
                                [(str "/remove?id=" (:hash s)) "Remove"])]])]
    ]])
