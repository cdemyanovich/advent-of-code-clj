(ns advent-of-code-clj.2019.day-02
  (:require [clojure.java.io :as io]
            [clojure.edn :as edn]
            [clojure.math.combinatorics :as combinatorics]
            [clojure.string :as str]))

(def raw-program
  (->> "data/2019/day_02.txt"
       io/resource
       slurp))

(defn parse-program
  [program]
  (vec
    (map edn/read-string
         (str/split program #","))))

(defn restore-program-to-state-before-fire
  [program]
  (-> program
      (assoc 1 12)
      (assoc 2 2)))


(defn execute
  [program]
  (loop [program program
         opcode-position 0]
    (let [opcode (nth program opcode-position)]
      (cond
        (= 99 opcode)
        program
        (= 1 opcode)
        (recur
          (assoc program
            (nth program (+ 3 opcode-position))
            (+ (nth program (nth program (+ 1 opcode-position)))
               (nth program (nth program (+ 2 opcode-position)))))
          (+ 4 opcode-position))
        (= 2 opcode)
        (recur
          (assoc program
            (nth program (+ 3 opcode-position))
            (* (nth program (nth program (+ 1 opcode-position)))
               (nth program (nth program (+ 2 opcode-position)))))
          (+ 4 opcode-position))))))

(def allowable-inputs
  "Each of the two input values will be between 0 and 99, inclusive."
  (range 0 100))

(defn combinations-of-inputs
  "Since our two operations, addition (opcode 1) and multiplication (opcode 2), are
  commutative (see: https://en.wikipedia.org/wiki/Commutative_property), we produce a
  selection (see: https://en.wikipedia.org/wiki/Combination) of allowable inputs n
  at a time."
  [inputs n]
  (combinatorics/selections inputs n))

(defn update-program
  [program [noun verb]]
  (-> program
      (assoc 1 noun)
      (assoc 2 verb)))

(defn part-one
  ;; What value is left at position 0 after the program halts? Answer: 4138687.
  []
  (->> raw-program
       parse-program
       restore-program-to-state-before-fire
       execute
       first))

(defn part-two
  []
  ;; Find the input noun and verb that cause the program to produce the output 19690720.
  ;; What is 100 * noun + verb? (For example, if noun=12 and verb=2, the answer would be 1202.)
  ;; Answer: noun=66 and verb=35, or 6635.
  (let [initial-program (parse-program raw-program)
        update-program' (partial update-program initial-program)
        inputs (combinations-of-inputs allowable-inputs 2)
        possible-programs (map update-program' inputs)
        candidate-programs (map execute possible-programs)
        desired-programs (filter #(= 19690720 (first %)) candidate-programs)
        desired-program (first desired-programs)
        noun (nth desired-program 1)
        verb (nth desired-program 2)
        answer (+ (* 100 noun) verb)]
    answer))

(comment
  (= 4138687 (part-one))
  (= 6635 (part-two))
  )
