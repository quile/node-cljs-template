(ns leiningen.new.node-cljs
  (:use [leiningen.new.templates :only [renderer name-to-path sanitize-ns ->files]]))

(def render (renderer "node-cljs"))

(defn node-cljs
  [name]
  (let [data {:name name
              :ns-name (sanitize-ns name)
              :sanitized (name-to-path name)}]
    (->files data ["src/cljs/{{sanitized}}/core.cljs" (render "core.cljs" data)]
                  ["src/cljs/{{sanitized}}/web.cljs" (render "web.cljs" data)]
                  ["test/foo.cljs" (render "foo.cljs" data)]
                  ["project.clj" (render "project.clj" data)]
                  ["src/cljs/{{sanitized}}/log.cljs" (render "log.cljs" data)]
                  ["src/cljs/{{sanitized}}/config.cljs" (render "config.cljs" data)]
                  ["resources/config/development.js" (render "development.js")]
                  ["src/js/some_node_module.js" (render "some_node_module.js")]
                  ["README.md" (render "README.md")]
                  ["run.js" (render "run.js" data)]
                  ["test-runner.js" (render "test-runner.js" data)]
)))
