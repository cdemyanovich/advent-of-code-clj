(ns advent-of-code-clj.2021.day-02
  (:require [clojure.java.io :as io]
            [clojure.string :as str]))

(def commands
  (->> "data/2021/day_02.txt"
       io/resource
       io/reader
       line-seq))

(defn ->int
  [magnitude]
  (Integer/parseInt magnitude))

(defn get-magnitudes
  [vectors]
  (map ->int (map last vectors)))

;; Idea: use polymorphism to process commands. See defmulti and defmethod.
(defn part-one
  [commands]
  (let [all-commands (map #(str/split % #" ") commands)
        {forwards "forward" downs "down" ups "up"} (group-by first all-commands)
        forward-total (reduce + (get-magnitudes forwards))
        down-total (reduce + (get-magnitudes downs))
        up-total (reduce + (get-magnitudes ups))
        depth-total (- down-total up-total)]
    (* forward-total depth-total)))

(defn parse-command
  [raw-command]
  (let [[name magnitude] (str/split raw-command #" ")]
    {:name name :magnitude (->int magnitude)}))

(defmulti process-command
          (fn [command _position] (:name command)))

(defmethod process-command "down"
  [command position]
  (assoc position :aim (+ (:aim position)
                          (:magnitude command))))

(defmethod process-command "up"
  [command position]
  (assoc position :aim (- (:aim position)
                          (:magnitude command))))

(defmethod process-command "forward"
  [command position]
  (assoc position :depth (+ (:depth position)
                            (* (:aim position) (:magnitude command)))
                  :horizontal (+ (:horizontal position)
                                 (:magnitude command))))

(defn process-commands
  [commands]
  (loop [commands (map parse-command commands)
         position {:aim 0 :depth 0 :horizontal 0}]
    (if (empty? commands)
      position
      (recur (rest commands)
             (process-command (first commands) position)))))

(defn part-two
  [commands]
  (let [position (process-commands commands)]
    (* (:depth position) (:horizontal position))))

(comment
  (= 1947824 (part-one commands))
  (= 1813062561 (part-two commands)))
