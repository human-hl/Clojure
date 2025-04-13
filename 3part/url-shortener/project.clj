(defproject url-shortener "0.1.0-SNAPSHOT"
  :dependencies [[org.clojure/clojure "1.11.1"]
                 [ring/ring-core "1.9.6"]  
                 [ring/ring-jetty-adapter "1.9.6"] 
                 [compojure "1.7.0"]
                 [cheshire "5.11.0"]
                 [ring/ring-defaults "0.3.4"]]  
  :main ^:skip-aot url-shortener.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
