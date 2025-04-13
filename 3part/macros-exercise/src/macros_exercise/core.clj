(ns macros-exercise.core
  (:require [clojure.string :as str]))

; Макрос multi-print
(defmacro multi-print [n s]
  `(dotimes [_ ~n]
     (println ~s)))

; Макрос and-ors
(defmacro and-ors [& clauses]
  (let [or-clauses (partition-by #(= % '|) clauses)
        or-clauses (remove #(= % '|) or-clauses)
        and-exprs (map (fn [clause]
                         `(or ~@clause)) or-clauses)]
    `(and ~@and-exprs)))

(defn my-function []
  (multi-print 3 "Hello from my-function!"))

(defn another-function [x y c d e f a b]
  (and-ors (> x 23) (> y 55) '| (= c d) (= e f) '| (> a 55) (> b 55)))