(ns commandline-sum.core
  (:gen-class))

(defn -main [& args]
  (try
    (let [numbers (map #(Integer/parseInt %) args) 
          sum (reduce + numbers)]             
      (println "Sum:" sum))                    
    (catch NumberFormatException e
      (println "Error: Please provide only integer arguments."))) 
  (shutdown-agents)) 
