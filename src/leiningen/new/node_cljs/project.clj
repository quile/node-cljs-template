(defproject {{ns-name}} "0.1.0-SNAPSHOT"
  :description "FIXME: write this!"
  :url "http://example.com/FIXME"

  :dependencies [[org.clojure/clojure "1.6.0"]
                 [org.clojure/clojurescript "0.0-2311"]
                 [quile/component "0.2.6-SNAPSHOT"]
                 [cljs-http "0.1.16"]]

  :node-dependencies [[express "3.0.0"]
                      [underscore "*"]
                      [moment "*"]
                      [source-map-support "*"]
                      [redis "0.8.x"]]

  :plugins [[lein-cljsbuild "1.0.4-SNAPSHOT"]
            [lein-npm "0.4.0"]
            [org.bodil/lein-noderepl "0.1.11"]]

  :cljsbuild {
    :builds [{:source-paths ["src/cljs"]
              :compiler {
                :output-to "{{ns-name}}.js"
                :output-dir "js"
                :optimizations :none
                :target :nodejs
                :source-map "{{ns-name}}.js.map"}}]})
