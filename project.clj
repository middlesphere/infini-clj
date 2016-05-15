(defproject com.middlesphere/infini-clj "0.1.1"
  :description "Clojure library to work with remote Infinispan using HotRod protocol"
  :url "https://github.com/middlesphere/infini-clj.git"
  :license {:name "Apache License 2.0"
            :url  "http://www.apache.org/licenses/"}
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [org.infinispan/infinispan-client-hotrod "8.2.1.Final"]
                 [org.infinispan/infinispan-query-dsl "8.2.1.Final"]]
  :source-paths ["src/clj"]
  :java-source-paths ["src/java"])
