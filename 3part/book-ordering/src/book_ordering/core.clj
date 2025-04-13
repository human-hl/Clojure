(ns book-ordering.core
  (:gen-class)
  (:require [clojure.java.io :as io]
            [clojure.string :as str]))

(def books-by-year
  {2021 ["Академия проклятий"]
   2022 ["Князь мира сего и имя мое легион"]})

(def order-file "orders.txt")

(defn display-main-menu []
  (println "|       Наши книги       |")
  (println "| 1-Меню 2-Заказы 3-Выход|")
  (println "|       Наши книги       |"))

(defn display-year-menu []
  (println "| Доступные книги по годам |")
  (doseq [[index year] (map-indexed vector (sort (keys books-by-year)))]
    (println (str (inc index) ". " year "  ")))
  (println "| Доступные книги по годам |"))

(defn display-books-for-year [year]
  (println (str "| Книги за " year " |"))
  (doseq [[index book] (map-indexed vector (get books-by-year year))]
    (println (str (inc index) ". " book " ")))
  (println (str "| Книги за " year " |")))

(defn get-book-price [book]
  (case book
    "Синтонимы" 100
    "Академия проклятий" 400
    "Алгоритмы и структуры данных" 150
    "Князь мира сего и имя мое легион" 250
    0))

(defn display-confirmation-message [book quantity total-price]
  (println (str "Покупка " quantity " шт. " book " всего за: R" total-price)))

(defn save-order [year book quantity]
  (with-open [w (io/writer order-file :append true)]
    (.write w (str year "," book "," quantity "\n"))))

(defn load-orders []
  (if (.exists (io/file order-file))
    (with-open [r (io/reader order-file)]
      (doall
       (map #(str/split % #",") (str/split (slurp r) #"\n"))))
    []))

(defn display-order [year book quantity]
  (let [price (get-book-price book)
        total-price (* price quantity)]
    (println (str "Куплено " quantity " шт. " book " за R" total-price))))

(defn display-orders-by-year []
  (let [orders (load-orders)
        grouped-orders (group-by first orders)]
    (if (empty? orders)
      (println "Нет заказов.")
      (do
        (println "|       Заказы по годам       |")
        (doseq [[year order-list] grouped-orders]
          (println (str "| Заказы за " year " |"))
          (doseq [order order-list]
            (let [book (second order)
                  quantity (Integer/parseInt (nth order 2))]
              (display-order year book quantity)))))))) 

(defn order-book-submenu []
  (display-year-menu)
  (println "Введите номер года:")
  (let [year-choice (try (Integer/parseInt (read-line)) (catch Exception e nil))]
    (if (and year-choice (contains? books-by-year year-choice))
      (let [year year-choice
            books (get books-by-year year)]
        (display-books-for-year year)
        (println "Введите номер книги:")
        (let [book-choice (try (Integer/parseInt (read-line)) (catch Exception e nil))]
          (if (and book-choice (<= 1 book-choice (count books)))
            (let [book (nth books (dec book-choice))]
              (println "Сколько экземпляров хотите приобрести?:")
              (let [quantity (try (Integer/parseInt (read-line)) (catch Exception e nil))]
                (if (and quantity (> quantity 0))
                  (let [price (get-book-price book)
                        total-price (* price quantity)]
                    (save-order year book quantity)
                    (display-confirmation-message book quantity total-price))
                  (println "Некорректное количество книг."))))
            (println "Некорректный номер книги.")))
        (println "Некорректный номер года.")))))

 (defn orders-submenu []
   (display-orders-by-year))
 
(defn -main [& args]
  (loop []
    (display-main-menu)
    (println "Введите номер меню:")
    (flush)
    (let [choice (try (Integer/parseInt (read-line)) (catch Exception e nil))]
      (case choice
        1 (order-book-submenu)
        2 (orders-submenu)
        3 (do (println "Выход из программы.") (shutdown-agents))
        (println "Некорректный ввод."))) 
    (recur))) 