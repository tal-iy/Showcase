;
; Assignment: Microproject 2
; Author: Vitaliy Shydlonok
; Date: 9/25/2019
;

(ns plminiproject2.core)

(defn norify
  "Adds a 'nor' to the start of an expression."
  [exp]
  (concat ['nor] exp))

(defn nor-convert
  "Converts logical expressions using and, or, and not connectives to using only nor."
  [exp]
  (let [operator (nth exp 0)
        operands (drop 1 exp)]
    (if (= operator 'not)
      (norify (list (nth operands 0)))
      (if (= operator 'or)
        (norify (list (norify operands)))
        (if (= operator 'and)
          (norify (map norify (map list operands)))
          '(INVALID EXPRESSION)))))
  )