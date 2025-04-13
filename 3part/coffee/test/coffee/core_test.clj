(ns coffee.core-test
  (:require [clojure.test :refer :all]
            [expectations :refer :all]
            [coffee.core :as c] 
            [clojure.java.io :as io]
            [midje.sweet :refer :all]
            [clojure.test.check.generators :as gen]
            [clojure.test.check.properties :as prop]
            [clojure.test.check.clojure-test :refer (defspec)]))

(deftest test-display-order-with-is
  (testing "Тестирование display-order с использованием is"
    (is (= "Заказ: Латте, 2 порций(и).\n" (with-out-str (c/display-order "Латте" 2))) "Проверка вывода для Латте")))

(deftest test-display-order-with-are
  (testing "Тестирование display-order с использованием are"
    (are [expected coffee-name quantity]
         (= expected (with-out-str (c/display-order coffee-name quantity)))
      "Заказ: Эспрессо, 1 порций(и).\n" "Эспрессо" 1
      "Заказ: Капучино, 3 порций(и).\n" "Капучино" 3)))

(deftest test-file-exist-true
  (testing "Проверка, что файл существует (существующий файл)"
    (spit "./test_file.txt" "test content")
    (is (c/file-exist "./test_file.txt") "Должен вернуть true для существующего файла")
    (io/delete-file "./test_file.txt")))

(deftest test-file-exist-false
  (testing "Проверка, что файл не существует (несуществующий файл)"
    (is (not (c/file-exist "nonexistent_file.txt")) "Должен вернуть false для несуществующего файла")))

(deftest test-save-and-load-orders
  (testing "Сохранение и загрузка заказов"
    (let [file-name "orders_test.txt"
          order1 {:coffee "Эспрессо" :quantity 2}
          order2 {:coffee "Капучино" :quantity 1}]
      (c/save-coffee-order file-name order1)
      (c/save-coffee-order file-name order2)
      (let [loaded-orders (c/load-orders file-name)]
        (is (= 2 (count loaded-orders)) "Должно быть 2 заказа")
        (is (= "Эспрессо" (:coffee (first loaded-orders))) "Проверка первого заказа (кофе)")
        (is (= 2 (:quantity (first loaded-orders))) "Проверка первого заказа (количество)"))
      (io/delete-file file-name))))

(deftest test-display-order-with-expectations
  (testing "Тестирование display-order с использованием expectations"
    (expect (with-out-str (c/display-order "Латте" 2))
            => "Заказ: Латте, 2 порций(и).\n")
    (expect (with-out-str (c/display-order "Эспрессо" 1))
            => "Заказ: Эспрессо, 1 порций(и).\n")))

(deftest test-file-exist-expectations
  (testing "Проверка существования файла с expectations"
    (spit "test_file.txt" "test content")
    (expect (c/file-exist "test_file.txt") => true)
    (expect (c/file-exist "nonexistent_file.txt") => false)
    (io/delete-file "test_file.txt")))

(deftest test-save-and-load-orders-expectations
  (testing "Сохранение и загрузка заказов с expectations"
    (let [file-name "orders_test.txt"
          order1 {:coffee "Эспрессо" :quantity 2}
          order2 {:coffee "Капучино" :quantity 1}]
      (c/save-coffee-order file-name order1)
      (c/save-coffee-order file-name order2)
      (let [loaded-orders (c/load-orders file-name)]
        (expect (count loaded-orders) => 2)
        (expect (:coffee (first loaded-orders)) => "Эспрессо")
        (expect (:quantity (first loaded-orders)) => 2))
      (io/delete-file file-name))))

(facts "Тестирование display-order с Midje"
       (fact "Проверка вывода для Латте"
             (with-out-str (c/display-order "Латте" 2)) => "Заказ: Латте, 2 порций(и).\n") 
       (fact "Проверка вывода для Эспрессо"
             (with-out-str (c/display-order "Эспрессо" 1)) => "Заказ: Эспрессо, 1 порций(и).\n"))

(facts "Тестирование file-exist с Midje"
       (let [file-name "test_file.txt"]
         (fact "Файл существует"
               (spit file-name "test content")
               (c/file-exist file-name) => true
               (io/delete-file file-name))
         (fact "Файл не существует"
               (c/file-exist "nonexistent_file.txt") => false)))

(facts "Тестирование load-orders с Midje"
       (let [file-name "orders_test.txt"
             order1 {:coffee "Эспрессо" :quantity 2}
             order2 {:coffee "Капучино" :quantity 1}]
         (fact "Загрузка заказов из файла"
               (spit file-name (str "Эспрессо,2\nКапучино,1\n")) 
               (c/load-orders file-name) => (list {:coffee "Эспрессо", :quantity 2} {:coffee "Капучино", :quantity 1})
               (io/delete-file file-name))))

(defspec test-display-order-test-check 100  
   (prop/for-all [coffee-name gen/string-ascii
                  quantity (gen/choose 1 10)] 
                 (let [expected (str "Заказ: " coffee-name ", " quantity " порций(и).\n")]
                   (= expected (with-out-str (c/display-order coffee-name quantity))))))

(defspec test-file-exist-test-check 50
  (prop/for-all [file-name gen/string-alphanumeric]
                (let [test-file (str "test_" file-name ".txt")]
                  (try
                    (when (c/file-exist test-file)
                      (io/delete-file test-file))
                    (catch Exception e (println "Error deleting file:" (.getMessage e))))

                  (let [should-exist (pos? (rand-int 2)) 
                        result (if should-exist
                                 (do
                                   (spit test-file "content")
                                   (let [exists (c/file-exist test-file)]
                                     (io/delete-file test-file) 
                                     exists)) 
                                 (c/file-exist test-file))] 
                    (finally
                      (when (c/file-exist test-file)
                        (try
                          (io/delete-file test-file)
                          (catch Exception e (println "Error deleting file in finally:" (.getMessage e)))))) 

                    (if should-exist
                      result 
                      (not result)))))) 

(defspec test-load-orders-test-check 50
  (prop/for-all [orders (gen/vector (gen/tuple gen/string-alphanumeric gen/pos-int) 0 5)]
                (let [file-name "orders_test.txt"
                      orders-str (str/join "\n" (map #(str (first %) "," (second %)) orders))
                      expected (map (fn [[coffee quantity]] {:coffee coffee :quantity quantity}) orders)]
                  (spit file-name (str orders-str "\n"))
                  (let [loaded-orders (c/load-orders file-name)]
                    (io/delete-file file-name)
                    (= expected loaded-orders)))))