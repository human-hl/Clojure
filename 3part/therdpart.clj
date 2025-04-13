;; Задание «Используем пространства имён»
(ns therdpart
    (:require [clojure.string :as str] 
              [clojure.pprint :refer [print-table]]
              [clojure.set :as set :refer [subset?]]))
  
  (def users #{"иван_шаповал" "софья_кузина" "алина_мага" "павел_безусый" "иван_соболев"})
  
  (defn capitalize-name [name]
    (-> name
        (str/replace #"_" " ")  
        str/capitalize))
  
  (def admin-names #{"иван" "павел" "алина"})
  
  (defn is-admin? [user]
    (let [[name _] (str/split user #"_")] 
      (contains? admin-names (str/lower-case name))))
  
  (def admins (->> users
                   (filter is-admin?)
                   (map capitalize-name)
                   (into #{})))  
  
  (println "администраторы:")
  (print-table [:user-name] (map #(hash-map :user-name %) (sort admins)))
  
  (println "\nявляется ли набор администраторов подмножеством набора пользователей?(false):")
  (println (subset? admins users))
