(ns {{ns-name}}.log
  (:require [cljs.nodejs :as node]))

(def util (node/require "util"))
(def moment (node/require "moment"))

(defn date
  []
  (let [d (js/Date.)]
    (str (-> (moment d)
             (.format "YYYY-MM-DD HH:mm:ss.SSS")))))

(defn format
  [& args]
  (let [date-str (date)
        formatted (apply (aget util "format") args)]
    (str date-str " - " formatted)))

(defn log
  [& args]
  (let [string (apply (aget util "format") args)]
    (.log js/console (format string))))

(defn info
  [& args]
  (apply log args))

(defn error
  [& args]
  (let [string (apply (aget util "format") args)]
    (.error js/console (format string))))

(defn info
  [& args]
  (apply log args))
