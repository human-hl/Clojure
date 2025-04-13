(defproject commandline-sum "0.1.0-SNAPSHOT"
  :description "A simple command-line application to sum integers."
  :url "http://example.com/" 
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
:dependencies [[org.clojure/clojure "1.11.1"]
               [clojure.tools.cli "1.0.219"]]
  :main commandline-sum.core 
  :target-path "target/%s" 

  :profiles {:default [:dev :test]
             :dev {:dependencies [[eftest "0.5.9"]]}
             :test {:test-paths ["test"]}} 
  :aliases {"test" ["run" "-m" "eftest.runner" "commandline-sum.test"]}

  :repl-options {:init-ns commandline-sum.core})

