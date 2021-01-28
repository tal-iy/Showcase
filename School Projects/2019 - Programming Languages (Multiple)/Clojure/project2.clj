;
; Assignment: Project 2
; Author: Vitaliy Shydlonok
; Date: 10/5/2019
;

(def p1 '(and x (or x (and y (not z)))))
(def p2 '(and (and z false) (or x true false)))
(def p3 '(or true a))
(def p4 '(or x y false (and true (not (not (not z))))))

(def t1 '(nor false))
(def t2 '(nor true))
(def t3 '(nor (nor x)))
(def t4 '(nor (nor (nor x))))
(def t5 '(nor (nor (nor (nor x)))))
(def t6 '(nor x x))
(def t7 '(nor x x x))
(def t8 '(nor x y))
(def t9 '(nor x true))
(def t10 '(nor x false))
(def t11 '(nor false false))
(def t12 '(nor x y false))
(def t13 '(nor x false false))
(def t14 '(nor false false false))
(def t15 '(nor x y true))
(def t16 '(nor x y z))

(defn norify
  "Adds a 'nor' to the start of an expression."
  [exp]
  (concat ['nor] exp))

(defn nor-convert
  "Converts logical expressions using and, or, and not connectives to using only nor."
  [exp]
  (if (seq? exp)
    (let [operator (nth exp 0)
          operands (drop 1 exp)
          converted (map nor-convert operands)]
      (cond
        (= operator 'not)
        (norify (list (nth converted 0)))
        (= operator 'or)
        (norify (list (norify converted)))
        (= operator 'and)
        (norify (map norify (map list converted)))
        (= operator 'nor)
        (norify converted)
        :else
        '"INVALID EXPRESSION"
        )
      )
    exp
    )
  )

(defn bind-values
  "Replaces variables in an expression with given bindings."
  [bindings exp]
  (map (fn [i]
         (if (seq? i)
           (bind-values bindings i)
           (get bindings i i)
           ))
       exp
       )
  )

(defn simplify
  "Symbolically simplifies an expression."
  [exp]
  (if (seq? exp)
    (let
      [
       operands (drop 1 exp)
       simplified (distinct (map simplify operands))
       ]
      (cond
        (= 'true simplified)
        'false
        (= 'false simplified)
        'true
        (some true? simplified)
        'false
        (every? false? simplified)
        'true
        (some false? simplified)
        (concat ['nor] (remove false? simplified))
        (and
          (= (count simplified) 1)
          (seq? (first simplified))
          (= (first (first simplified)) 'nor)
          (= (count (first simplified)) 2)
          )
        (first (drop 1 (first simplified)))
        :else
        (concat ['nor] simplified)
        )
      )
    exp
    )
  )

(defn evalexp
  "Evaluates an expression to the simplest terms."
  [exp bindings]
  (simplify (nor-convert (bind-values bindings exp))))

