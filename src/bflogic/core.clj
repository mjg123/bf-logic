(ns bflogic.core
  (:refer-clojure :exclude [==])
  (:use [clojure.core.logic]))

;;  --------------------------------------------------------
;;   Helper fn
(defn to-number 
    "turns a seq of numbers (eg [1 2 3 4]) into a number (eg 1234)"
    ([l] (to-number l 0))
    ([l n]
        (if (empty? l) n
            (recur (rest l) (+ (first l) (* 10 n))))))

;;  --------------------------------------------------------
;;   Our solver
(defn solve-bflogic []

    (map to-number

        (run* [q]  ;; q is the solution, expressed as a list of 9 digits.
                   ;; run* is the core.logic fn to yield _all_ solutions

            (fresh [q1 q2 q3 q4 q5 q6 q7 q8 q9 n]  ;; fresh introduces new logic vars 

            (permo q [1 2 3 4 5 6 7 8 9])          ;; our answer is a permutation of 1-9

            (== q [q1 q2 q3 q4 q5 q6 q7 q8 q9])    ;; bind q1-q9 as the elements of q

            (project [q1 q2 q3 q4 q5 q6 q7 q8 q9]  ;; project allows us to treat q1-q9 as
                                                   ;; numbers rather than unbound logic vars

                (== 1 (Math/abs (- q1 q2)))        ;; the ends are easy to specify
                (== 1 (Math/abs (- q8 q9)))

                (== n (to-number [q1 q2 q3 q4 q5 q6 q7 q8 q9]))

                (project [n]                       ;; lemma 2
                    (== 0 (mod n 2))               ;; not all necessary, but no harm...
                    (== 0 (mod n 3))               
                    (== 0 (mod n 4))               ;; lemma 1
                    (== 0 (mod n 6))
                    (== 0 (mod n 7))               
                    (== 0 (mod n 8))
                    (== 0 (mod n 9))
                    (== 0 (mod n 11))
                    (== 0 (mod n 12))

                    (if (contains? #{12 56 76} (mod n 100)) ;; lemma 3, lemma 4
                        succeed fail)))))))

