(defproject book-ordering "0.1.0-SNAPSHOT"
  :description "Приложение для заказа книг"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.12.0"]] 
  :main book-ordering.core
  :aot :all
  :target-path "target/%s" 
  :jvm-opts ["-Dfile.encoding=UTF-8"] 
  :profiles {:default [:dev :test]
             :dev {:dependencies [[eftest "0.5.9"]]}})


