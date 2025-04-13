(defproject coffee "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[org.clojure/clojure "1.11.1"]
                 [clojure.test "1.9.0"] 
                 [expectations "2.2.1"] 
                 [midje "1.10.0"] 
                 [org.clojure/test.check "1.1.1"]] 
  :repl-options {:init-ns coffee.core})
