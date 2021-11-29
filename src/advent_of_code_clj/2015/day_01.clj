(ns advent-of-code-clj.2015.day-01)

(def input
  (slurp "resources/data/2015/day_01.txt"))

(defn ->value
  [instruction]
  (case instruction
    \( 1
    \) -1
    0))

(defn part-1
  []
  (reduce + (map ->value input)))

(defn part-2
  []
  (->> input
       (map ->value)
       (reductions +)
       (keep-indexed #(if (= -1 %2) %1))
       (first)
       (+ 1)))

(comment
  (= 74 (part-1))
  (= 1795 (part-2))
  )


