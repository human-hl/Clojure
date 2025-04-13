(ns url-shortener.client
  (:require [clj-http.client :as client]
            [clojure.string :as str]
            [cheshire.core :as json]
            [java.net.URLEncoder :as url-encoder]))

(def server-url "http://localhost:3000")

(defn encode-url [url]
  (url-encoder/encode url "UTF-8"))

(defn create-url [long-url]
  (try
    (let [encoded-url (encode-url long-url)
          response (client/post (str server-url "/normal-url")
                                {:form-params {:url encoded-url}
                                 :content-type :json
                                 :as :json})] 
      (if (= (:status response) 200)
        (get-in response [:body]) 
        (str "Error: " (:status response) " - " (:body response)))) 
    (catch Exception e
      (str "Error: " (.getMessage e)))))

(defn get-url [short-url]
  (try
    (let [response (client/get (str server-url "/" short-url)
                               {:as :json})] 
      (if (= (:status response) 200)
        (get-in response [:body]) 
        (str "Error: " (:status response) " - " (:body response)))) 
    (catch Exception e
      (str "Error: " (.getMessage e)))))

(defn update-url [short-url new-url]
  (try
    (let [encoded-new-url (encode-url new-url)
          response (client/put (str server-url "/" short-url "/" encoded-new-url)
                               {:content-type :json
                                :as :json})]
      (if (= (:status response) 200)
        (:body response)  
        (str "Error: " (:status response) " - " (:body response)))) 
    (catch Exception e
      (str "Error: " (.getMessage e)))))

(defn delete-url [short-url]
  (try
    (let [response (client/delete (str server-url "/" short-url)
                                  {:as :json})]
      (if (= (:status response) 200)
        (:body response) 
        (str "Error: " (:status response) " - " (:body response)))) 
    (catch Exception e
      (str "Error: " (.getMessage e)))))


(defn print-menu []
  (println "Действия:")
  (println "1. Создать")
  (println "2. Показать")
  (println "3. Изменить")
  (println "4. Удалить")
  (println "5. Выйти"))

(defn -main [& args]
  (loop []
    (print-menu)
    (print "Выберите действие: ")
    (flush)
    (let [choice (read-line)]
      (cond
        (= choice "1") (do (print "Введите полный URL: ") (flush)
                           (let [long-url (read-line)
                                 short-url (create-url long-url)]
                             (println (if (str/starts-with? short-url "Error:")
                                        short-url
                                        (str "Короткий URL: " short-url)))))
        (= choice "2") (do (print "Введите короткий URL: ") (flush)
                           (let [short-url (read-line)
                                 long-url (get-url short-url)]
                             (println (if (str/starts-with? long-url "Error:")
                                        long-url
                                        (str "Полный URL: " long-url)))))
        (= choice "3") (do (print "Введите короткий URL: ") (flush)
                           (let [short-url (read-line)
                                 (print "Введите новый полный URL: ") (flush)
                                 new-url (read-line)
                                 status (update-url short-url new-url)]
                             (println status)))
        (= choice "4") (do (print "Введите короткий URL: ") (flush)
                           (let [short-url (read-line)
                                 status (delete-url short-url)]
                             (println status)))
        (= choice "5") (println "Выход")
        :else (println "Неверный выбор"))
      (when (not= choice "5") (recur)))))