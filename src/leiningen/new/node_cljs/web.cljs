(ns {{ns-name}}.web
  (:require [cljs.nodejs :as node]
            [{{ns-name}}.log :as log :refer [info error]]
            [com.stuartsierra.component :as component]))

(def express (node/require "express"))

(defrecord WebComponent [config fpga queue]
  component/Lifecycle

  (start [this]
    (info ";; Starting WebComponent")
    (let [port (-> config :-config :web :port)
          app (express)]
      (doto app
        (.get "/" (fn [req res]
                    (.send res "Hello World")))
        (.listen port))
      (assoc this :app app)))

  (stop [this]
    (info ";; Stopping WebComponent")
    (dissoc this :app)))

(defn new-web []
  (map->WebComponent {}))
