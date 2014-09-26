(ns foo
  (:require-macros [cemerick.cljs.test :refer (is deftest)])
  (:require [cemerick.cljs.test :as t]))

(deftest foo-bar-test
  (is (= true false)))

(set! *main-cli-fn* #()) ;; node.js fu
