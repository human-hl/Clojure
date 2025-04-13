(ns coffee.core
  (:require [clojure.string :as str])
  (:gen-class))

(defn display-order [coffee quantity]
  (println (str "Заказ: " coffee ", " quantity " порций(и).")))

(defn file-exist [file-path]
  (.exists (java.io.File. file-path)))

(defn save-coffee-order [file-path order]
  (with-open [w (clojure.java.io/writer file-path :append true)]
    (.write w (str (:coffee order) "," (:quantity order) "\n"))))

(defn load-orders [file-path]
  (try
    (with-open [r (clojure.java.io/reader file-path)]
      (doall
       (map (fn [line]
              (let [[coffee quantity] (str/split line #",")]
                {:coffee coffee :quantity (Integer/parseInt quantity)}))
            (line-seq r))))
    (catch java.io.FileNotFoundException e [])))

(defn -main [& args]
  (println "Coffee app started!"))
