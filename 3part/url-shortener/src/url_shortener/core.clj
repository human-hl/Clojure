
(ns url-shortener.core
  (:require [ring.adapter.jetty :as jetty]
            [compojure.core :refer :all]
            [compojure.route :as route]
            [ring.middleware.defaults :refer [wrap-defaults api-defaults]]
            [clojure.java.jdbc :as jdbc]
            [cheshire.core :as json])  
  (:import [java.sql Types]))

(def db-spec
  {:dbtype "h2"
   :dbname "urlshortener"})

(defn generate-short-url [long-url]
  (str "short/" (hash long-url)))

(defroutes app-routes
  (POST "/normal-url" [request]  
    (let [long-url (get-in request [:params "url"])  
          short-url (generate-short-url long-url)]
      {:status 200
       :headers {"Content-Type" "application/json"}  
       :body (json/generate-string short-url)}))  

  (GET "/:short-url" [short-url]
    (let [long-url "http://example.com/long-url"]
      {:status 200
       :headers {"Content-Type" "application/json"}  
       :body (json/generate-string long-url)})) 

  (PUT "/:short-url/:normal-url" [short-url normal-url]
    {:status 200
     :headers {"Content-Type" "application/json"}
     :body (json/generate-string "URL updated")})

  (DELETE "/:short-url" [short-url]
    {:status 200
     :headers {"Content-Type" "application/json"}
     :body (json/generate-string "URL deleted")})

  (route/not-found "Not Found"))

(def app
  (wrap-defaults app-routes api-defaults))

(defn -main [& args]
  (jdbc/db-do-commands db-spec
                       (jdbc/create-table-ddl
                        :urls
                        [[:short (java.sql.Types/VARCHAR 255) :primary key]
                         [:long (java.sql.Types/VARCHAR 255)]]))
  (jetty/run-jetty app {:port 3000}))