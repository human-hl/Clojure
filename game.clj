(ns game)
;; контрольнаяточка 1 угадай число
(def lower-bound (atom nil)) 
(def upper-bound (atom nil)) 
(def current-guess (atom nil)) 

(defn start [n m]
  (swap! lower-bound (fn [_] n)) 
  (swap! upper-bound (fn [_] m)) 
  (swap! current-guess (fn [_] (int (/ (+ n m) 2)))) 
  "Я готов...") 

(defn guess-my-number []
  (let [guess @current-guess] 
    guess)) 

(defn smaller []
  (swap! upper-bound (fn [_] @current-guess)) 
  (swap! current-guess (fn [_] (int (/ (+ @lower-bound @current-guess) 2)))) 
  @current-guess) 

(defn bigger []
  (swap! lower-bound (fn [_] @current-guess)) 
  (swap! current-guess (fn [_] (int (/ (+ @current-guess @upper-bound) 2)))) 
  @current-guess) 