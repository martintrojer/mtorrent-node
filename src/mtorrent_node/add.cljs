(ns mtorrent-node.add)

;; the add page

(defn render []
  [:div
   [:form.form-horizontal {:name "magnet"
                           :action "magnet"
                           :method "post"}
    [:div.form-group
     [:label {:class "col-lg-2 control-label"
              :for "magnetinput"} "Magnet"]
     [:div.col-lg-9
      [:input.form-control {:type "text"
                            :id "magnetinput"
                            :name "magnet"
                            :placeholder "magent:"}]]
     [:button {:class "col-lg-1 btn btn-default"
               :type "submit"} "Submit"]]]
   [:form.form-horizontal {:name "url"
                           :action "url"
                           :method "post"}
    [:div.form-group
     [:label {:class "col-lg-2 control-label"
              :for "urlinput"} "Torrent URL"]
     [:div.col-lg-9
      [:input.form-control {:type "text"
                            :id "urlinput"
                            :name "url"
                            :placeholder "http://b0rked"}]]
     [:button.btn {:class "col-lg-1 btn btn-default"
                   :type "submit"} "Submit"]]]])
