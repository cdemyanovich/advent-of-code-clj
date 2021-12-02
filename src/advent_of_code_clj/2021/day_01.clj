(ns advent-of-code-clj.2021.day-01
  (:require [clojure.edn :as edn]
            [clojure.java.io :as io]))

(def input
  (->> "data/2021/day_01.txt"
       io/resource
       io/reader
       line-seq))

(def depths
  (map edn/read-string input))

(defn part-one
  [depths]
  (->> depths
       (partition 2 1)
       (map #(- (last %) (first %)))
       (filter pos?)
       (count)))

(defn part-two
  [depths]
  (->> depths
       (partition 3 1)
       (map #(reduce + %))
       (partition 2 1)
       (map #(- (last %) (first %)))
       (filter pos?)
       (count)))

(comment
  (= 1298 (part-one depths))
  (= 1248 (part-two depths))
  )