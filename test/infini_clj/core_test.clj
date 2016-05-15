(ns infini-clj.core-test
  (:require [clojure.test :refer :all]
            [infini-clj.cache :refer :all])
  (:import (org.infinispan.client.hotrod.impl RemoteCacheImpl)))

(deftest infini-clj.cache-test
  (testing "Test all cache functions."
    ;(def c (connect! "127.0.0.1:11222" "websessions"))
    (def c (connect! "127.0.0.1:11222"))
    (is ( = (type c) RemoteCacheImpl))

    (def lc (add-listener! c (fn [k v] (println "new entry | key: " k " value: " v)) :entry-created))
    (def lm (add-listener! c (fn [k v] (println "modified entry | key: " k " value: " v)) :entry-modified))
    (def lr (add-listener! c (fn [k v] (println "removed entry | key: " k " value: " v)) :entry-removed))
    (def le (add-listener! c (fn [k v] (println "expired entry | key: " k " value: " v)) :entry-expired))

    (put-value c :a 2)
    (put-value c :b 3)
    (put-value c :a 4)
    (put-value c :d 5 5000)
    (put-value c :e 654)

    (is (= 4 (get-value c :a)))
    (is (= #{:d, :b, :a, :e} (get-all-keys c)))

    (put-if-absent c :b [1 2 3 4 5])
    (clear-cache c)
    (disconnect! c)))

