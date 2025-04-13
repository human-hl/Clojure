(ns secondpart (:require [clojure.string :as string]))
;; Используем функции высшего порядка
(defn contains [lst element]
  (> (count (filter #(= % element) lst)) 0))

(defn is-palindrome [text]
  (let [cleaned-text (->> text
                          (string/lower-case)
                          (filter (complement (set [\space])))
                          (apply str))]
    (= cleaned-text (string/reverse cleaned-text))))

(defn harmonic [n]
  (->> (range 2 (+ n 1))
       (map #(/ 1.0 %))
       (reduce +)))
(println (contains [1 2 3 4 5] 3))
(println (contains [1 2 3 4 5] 6))
(println (is-palindrome "А роза упала на лапу Азора"))
(println (is-palindrome "Madam, I'm Adam"))
(println (is-palindrome "Not a palindrome"))
(println (harmonic 5))

;; Расширение проекта

(defn robot-hp [robot]
  (second robot)) 

(def robots [["R1" 100] ["R2" 80] ["R3" 120]])

(def hp-values (map robot-hp robots)) 
(println "HP values:" hp-values) 

(defn three-round-fight [robot-a robot-b]
  (let [robot-a-name (first robot-a)
        robot-b-name (first robot-b)
        attack-fn (fn [robot] (rand-int 20)) 
        damage-fn (fn [attacker defender] (- (second defender) (attack-fn attacker)))]
    (loop [robot-a robot-a, robot-b robot-b, round 1]
      (if (> round 3)
        (if (>= (second robot-a) 0)
          (println (str robot-a-name " победил!"))
          (println (str robot-b-name " победил!")))
        (let [robot-a-attack (attack-fn robot-a)
              robot-b-attack (attack-fn robot-b)
              new-robot-b (assoc robot-b 1 (damage-fn robot-a robot-b))
              new-robot-a (assoc robot-a 1 (damage-fn robot-b robot-a))]
          (println (str "Раунд " round ": " robot-a-name " атакует " robot-b-name " на " robot-a-attack " урона, осталось " (second new-robot-b)))
          (println (str "Раунд " round ": " robot-b-name " атакует " robot-a-name " на " robot-b-attack " урона, осталось " (second new-robot-a)))
          (recur new-robot-a new-robot-b (+ round 1)))))))
; Пример использования
(def robot-a ["A" 100])
(def robot-b ["B" 80])
(three-round-fight robot-a robot-b)

(defn robot-attack [robot-a robot-b] 
  (let [damage (rand-int 20) 
        name-a (first robot-a)
        name-b (first robot-b)
        new-hp (max 0 (- (second robot-b) damage))] 
    [(first robot-b) new-hp]))

(defn create-fighter [name hp]
  [name hp])

(def robot-4 (create-fighter "R4" 90))
(def robots-3 [["R1" 100] ["R2" 80] ["R3" 120]])

(def fight-with-robot-4 (partial robot-attack robot-4))
(def results (map fight-with-robot-4 robots-3))
(println "Results after fight with R4:" results)

(defn robot [[name hp]] 
  (let [attack-fn (fn [] (rand-int 20))]
    (fn [method & args]
      (case method
        :name name
        :hp hp
        :attack (let [damage (attack-fn)]
                  (assoc [name hp] 1 (max 0 (- hp damage)))) 
        :info (str "Robot " name ", HP: " hp)))))

(def smart-robot (robot ["умный" 15 40])) 
(println (:name smart-robot))   
(println (:hp smart-robot))     
(println (:info smart-robot)) 
(println smart-robot)
(println (:attack smart-robot))

;; Задание «Каррируем функции»

;; (def keep-highest (fn [x] (fn [y] (if (>= x y) x y))))

;; (def words (partial clojure.string/split " "))

;; (def filter-qs (partial filter #(re-seq #"q" %)))

;; (def maximum (partial reduce (keep-highest ##-Inf)))

;; (println (words "This is a test string"))
;; (println (filter-qs ["quick" "brown" "fox" "jumps"]))
;; (println (maximum [1 5 2 8 3]))
;; (println (maximum [-1 -5 -2 -8 -3]))
;; (println (maximum []))

;; Задание «Используем композицию»

(defn average [xs]
  (/ (reduce + 0 xs) (count xs)))

(defn is-last-in-stock [cars]
  ((comp #(get % :in-stock) last) cars))

(defn average-dollar-value [cars]
  ((comp average (partial map #(get % :dollar-value))) cars))

(defn fastest-car [cars]
  ((comp #(str (:name (last %)) " - быстрее всех")
         (partial sort-by :horsepower)) cars))

(def cars
  [{:name "Car A" :horsepower 150 :dollar-value 20000 :in-stock true}
   {:name "Car B" :horsepower 180 :dollar-value 25000 :in-stock false}
   {:name "Car C" :horsepower 200 :dollar-value 30000 :in-stock true}
   {:name "Car D" :horsepower 220 :dollar-value 35000 :in-stock true}])
(println (is-last-in-stock cars))
(println (average-dollar-value cars))
(println (fastest-car cars))

;; Задание «Портируем код»

(defn conjoin [flockA flockB]
  (update flockA :seagulls + (:seagulls flockB)))

(defn breed [flockA flockB]
  (update flockA :seagulls * (:seagulls flockB)))

(def flockA {:seagulls 4})
(def flockB {:seagulls 2})
(def flockC {:seagulls 0})

(def result (-> flockA
                (conjoin flockC)
                (breed flockB)
                (conjoin (breed flockA flockB))
                :seagulls))

(println result)



