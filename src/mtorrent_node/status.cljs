(ns mtorrent-node.status)

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
   [:h1 "status"]
   [:table {:class "table table-hover table-condensed"}
    [:thead
     [:tr
      [:th "Name"]
      [:th "Status"]
      [:th "Progress"]
      [:th "Seeds"]
      [:th "Leach"]
      [:th "Up"]
      [:th "Down"]
      [:th (get-action-button "btn-danger" ["/" "Pause All"] ["/" "Remove All"])]]]
    [:tbody
     [:tr
      [:td "Foo"]
      [:td "Working"]
      [:td
       [:div.progress
        [:div {:class "progress-bar progress-bar-success"
               :style "width: 43%;"}]]]
      [:td "32(2032)"]
      [:td "54(1023)"]
      [:td "1.32 kB/s"]
      [:td "43.2 B/s"]
      [:td (get-action-button "btn-primary" ["/" "Pause"] ["/" "Remove"])]]]
    ]])