(ns {{ns-name}}.core
  (:require [{{ns-name}}.web :as web]
            [{{ns-name}}.config :as config]
            [{{ns-name}}.log :as log :refer [info error]]
            [cljs.nodejs :as node]
            [quile.component :as component]))

(def some-node-module (apply (js* "require") ["../../src/js/some_node_module"]))

(defrecord NodeComponent [options config queue web]
  component/Lifecycle

  (start [this]
    (info ";; Starting NodeComponent")
    ;; In the 'start' method, a component may assume that its
    ;; dependencies are available and have already been started.
    (assoc this :foo "bar!"))

  (stop [this]
    (info ";; Stopping NodeComponent")
    ;; Likewise, in the 'stop' method, a component may assume that its
    ;; dependencies will not be stopped until AFTER it is stopped.
    this))

;; Not all the dependencies need to be supplied at construction time.
;; In general, the constructor should not depend on other components
;; being available or started.

(defn node-component [config-options]
  (map->NodeComponent {:options config-options}))

(defrecord NodeSystem [config-options web]
  component/Lifecycle
  (start [this]
    (component/start-system this [:web :config]))
  (stop [this]
    (component/stop-system this [:web :config])))

(defn n-system [config-options]
  (let [port (-> config-options :web :port)]
    (map->NodeSystem
      {:config-options config-options
       :config (config/new-config config-options)
       :web (component/using (web/new-web) {:config :config})
       :app (component/using
             (node-component config-options)
             {:web :web
              :config :config})})))

(def app (atom nil))

;; Just start the system, wait a few seconds, then stop it.
(defn -main [& args]
  ;; our JS node module is exposed here:
  (let [foo (aget some-node-module "foo")
        bar (aget some-node-module "bar")]
    (foo)
    (bar))
  (let [cfg (config/load-config)
        systems (reset! app {:system (n-system cfg)})
        started (component/start-system (:system systems))]
    (swap! app assoc :system started)
    (js/setTimeout #(do
                      (component/stop-system (:system @app))
                      (.exit js/process)) 5000)))

(enable-console-print!)
(set! *main-cli-fn* -main)
