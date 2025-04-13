
;; лямбда функции
(def numbers [1 2 3])
(def doubled (map #(* % 2) numbers))

(println numbers)  
(println doubled) 

(defn apply-func [f x] (f x))
(println (apply-func inc 10))  

(def add #(+ % 2))
(def multiply #(* % 3))

(def users
  [{:id 1 :name "Alice" :age 25}
   {:id 2 :name "Bob" :age 30}
   {:id 3 :name "Charlie" :age 35}])

;; Фильтрация
(def adults (filter #(> (:age %) 30) users))
(println adults)  

;; Преобразование
(def names (map :name users))
(println names) 

;; Агрегация
(def total-age (reduce + (map :age users)))
(println total-age) 

(def fetch-data #(do (Thread/sleep 1000) (str "Data from " %)))
(def process-data #(str "Processed " %))

(let [data (future (fetch-data "https://example.com"))]
  (println (process-data @data)))  

(def products
  [{:name "Laptop" :price 1000}
   {:name "Mouse" :price 25}
   {:name "Keyboard" :price 50}])

;; Фильтрация по цене
(def filter-by-price #(filter (fn [p] (<= (:price p) %2)) %1))
(println (filter-by-price products 100))  

;; Композиция функций
(def compose (fn [f g] (fn [x] (f (g x)))))

(def add-then-multiply (compose #(* % 2) #(+ % 10)))
(println (add-then-multiply 5))  

(println (add-then-multiply 2))  

;; Функции как значения первого класса

(defn format-vladivostok-address [name street house]
  (str "Многоуважаемый(ая) " name ", ваш адрес: "
       "г. Владивосток, ул. " street ", д. " house "."))

(defn get-location [city name street house]
  (if (= city "Владивосток")
    (format-vladivostok-address name street house)
    (str "Адрес для города " city ": " name ", " street ", " house)))

(println (get-location "Владивосток" "Иван Иванов" "Ленинская" "10"))

(println (get-location "Москва" "Петр Петров" "Тверская" "5"))

(require '[clojure.string :as str])
(println (str/join " " ["Многоуважаемый(ая)," "Иван Иванов," "г. Владивосток," "ул. Ленинская," "д. 10."]))

;; Замыкания и частичное применение функций

;; Основная функция, проверяющая четность
(defn if-even [f x]
  (if (even? x)
    (f x)
    x))

;; Переопределенные функции с использованием частичного применения
(def if-even-inc (partial if-even inc))
(def if-even-double (partial if-even #(* 2 %)))
(def if-even-square (partial if-even #(* % %)))

(println (if-even-inc 4))     
(println (if-even-double 4))   
(println (if-even-square 4))   
(println (if-even-inc 3))      
(println (if-even-double 3))    
(println (if-even-square 3))   

;; Функция binary-partial
(defn binary-partial [f x]
  (fn [y] (f x y)))

(def add2 (fn [x y] (+ x y)))
(def add-five (binary-partial add2 5))

(println (add-five 10))  
(println (add-five 20))  


;; Коллекции

(defn repeat' [x]
  (lazy-seq (cons x (repeat' x))))

(println (take 5 (repeat' 42))) 

(defn my-subseq [start end coll]
  (take (- end start) (drop start coll)))

(println (my-subseq 2 5 (range 1 10))) 
(println (my-subseq 0 3 (list 10 20 30 40 50))) 


(defn in-first-half [elem coll]
  (let [half-length (int (/ (count coll) 2))]
    (contains? (set (take half-length coll)) elem)))

(println (in-first-half 20 (list 10 20 30 40))) 
(println (in-first-half 30 (list 10 20 30 40))) 

;; Правила рекурсии и сопоставление с образцом

(defn my-rest [coll]
  (if (empty? coll)
    nil
    (let [[_ & xs] coll] 
      xs)))

(println (my-rest [1 2 3])) 
(println (my-rest []))      

(defn gcd [a b]
  (cond
    (= b 0) a                      
    :else (recur b (mod a b))))    

(println (gcd 48 18)) 
(println (gcd 101 10)) 

;; Пишем рекурсивные функции

;; Функция для инверсии списка
(defn reverse [coll]
  (if (empty? coll)
    []
    (conj (reverse (rest coll)) (first coll))))

;; Функция для вычисления n-го числа Фибоначчи
(defn fib [n]
  (cond
    (= n 0) 0
    (= n 1) 1
    :else (+ (fib (- n 1)) (fib (- n 2)))))

;; Функция для быстрого вычисления n-го числа Фибоначчи
(defn fast-fib [n1 n2 counter]
  (if (= counter 1)
    n1
    (recur n2 (+ n1 n2) (dec counter))))

(println (reverse [1 2 3 4])) 
(println (fib 10)) 
(println (fast-fib (bigint 1) (bigint 1) 1000))




