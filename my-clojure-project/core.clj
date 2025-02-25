;; (ns clj.core)

;; (println "задание 1")

;; (defn increment [n]
;;   (+ n 1))

;; (defn my-double [n]
;;   (* n 2))

;; (defn square [n]
;;   (* n n))

;; (defn process-number [n]
;;   (if (even? n)
;;     (+ n 2)
;;     (- (* 3 n) 1)))

;; (println (process-number 2))
;; (println (process-number 7))
;; (println (increment 3))
;; (println (my-double 3))
;; (println (square 3))

;; (println "задание 2")

;; (println ((fn [n] (+ n 1)) 2))
;; (println ((fn [n] (* n 2)) 2))
;; (println ((fn [n] (* n n)) 2))


;; (println "задание 3")

;; (defn format-address [name street city postal-code]
;;   (str "Многоуважаемый(ая) " name ", " street ", " city ", " postal-code))

;; (defn get-location [f name]
;;   (let [street "ул. Пушкина, д. 10"
;;         city "Владивосток"
;;         postal-code "690000"]
;;     (f name street city postal-code)))

;; (println (get-location format-address "Иванов"))
