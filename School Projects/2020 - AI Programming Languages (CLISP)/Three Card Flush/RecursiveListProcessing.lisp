( defun singleton-p ( x )
    "Returns true if there is only one element in the list x"
    ( = ( length x ) 1 )
)

( defun rac ( x )
    "Returns the last element of the list x"
    ( cond
        ( ( equal x '() )
            '()
        )
        ( ( singleton-p x )
            ( car x )
        )
        ( t
            ( rac ( cdr x ) )
        )
    )
)

( defun rdc ( x )
    "Returns all but the last elements of the list x"
    ( cond
        ( ( equal ( cdr x ) nil )
            '()
        ) 
        ( ( singleton-p ( cdr x ) )
            ( cons ( car x ) '() )
        )
        ( t
            ( cons ( car x ) ( rdc ( cdr x ) ) )
        )
    )
)

( defun snoc ( x y )
    "Appends x to the list y"
    ( cond
        ( ( equal y '() )
            ( list x )
        )
        ( t
            ( cons ( car y ) ( snoc x ( cdr y ) ) )
        )
    )
)

( defun palindrome-p ( x )
    "Returns true if x is a palindrome expression"
    ( cond
        ( ( equal x '() )
            t
        )
        ( ( singleton-p x )
            t
        )
        ( t
            ( and ( equal ( car x ) ( rac x ) ) ( palindrome-p ( cdr ( rdc x ) ) ) )
        )
    )
)

( defun select ( n x )
    "Returns the nth element of the list x"
    ( cond
        ( ( = n 0 )
            ( car x )
        )
        ( t
            ( select ( - n 1 ) ( cdr x ) )
        )
    )
)

( defun pick ( x )
    "Returns a random element of the list x"
    ( select ( random ( length x ) ) x )
)

( defun sum ( x )
    "Returns the sum of all elements in the list x"
    ( cond
        ( ( equal x '() )
            '0
        )
        ( ( singleton-p x )
            ( car x )
        )
        ( t
            ( + ( car x ) ( sum ( cdr x ) ) )
        )
    )
)

( defun product ( x )
    "Returns the product of all elements in the list x"
    ( cond
        ( ( equal x '() )
            '1
        )
        ( ( singleton-p x )
            ( car x )
        )
        ( t
            ( * ( car x ) ( product ( cdr x ) ) )
        )
    )
)

( defun iota ( n )
    "Returns n consecutive integers starting with 1"
    ( cond
        ( ( = n 0 )
            '()
        )
        ( ( > n 1 )
            ( snoc n ( iota ( - n 1 ) ) )
        )
        ( t
            '( 1 )
        )
    )
)

( defun duplicate ( n lo )
    "Returns a list containing n number of lo objects"
    ( cond
        ( ( = n 0 )
            '()
        )
        ( ( > n 1 )
            ( snoc lo ( duplicate ( - n 1 ) lo ) )
        )
        ( t
            ( list lo )
        )
    )
)

( defun factorial ( n )
    "Returns the factorial of n"
    ( product ( iota n ) )
)

( defun power ( n ex )
    "Returns n to the power of ex"
    ( product ( duplicate ex n ) )
)

( defun filter-in ( pred x )
    "Returns a subset of a list where the predicate applied to each element is true"
    ( cond
        ( ( equal x '() )
            '()
        )
        ( ( singleton-p x )
            ( cond
                ( ( apply pred ( list ( car x ) ) )
                    x
                )
                ( t
                    '()
                )
            )
        )
        ( ( apply pred ( list ( car x ) ) )
            ( cons ( car x ) ( filter-in pred ( cdr x ) ) )
        )
        ( t
            ( filter-in pred ( cdr x ) )
        )
    )
)

( defun filter-out ( pred x )
    "Returns a subset of a list where the predicate applied to each element is false"
    ( cond
        ( ( equal x '() )
            '()
        )
        ( ( singleton-p x )
            ( cond
                ( ( apply pred ( list ( car x ) ) )
                    '()
                )
                ( t
                    x
                )
            )
        )
        ( ( apply pred ( list ( car x ) ) )
            ( filter-out pred ( cdr x ) )
        )
        ( t
            ( cons ( car x ) ( filter-out pred ( cdr x ) ) )
        )
    )
)

( defun take-from ( obj x )
    "Returns a subset of a list where obj is taken out"
    ( cond
        ( ( equal x '() )
            '()
        )
        ( ( singleton-p x )
            ( cond
                ( ( equal ( car x ) obj )
                    '()
                )
                ( t
                    x
                )
            )
        )
        ( ( equal ( car x ) obj )
            ( take-from obj ( cdr x ) )
        )
        ( t
            ( cons ( car x ) ( take-from obj ( cdr x ) ) )
        )
    )
)

( defun random-permutation ( x )
    "Returns a random permutation of a list"
    ( cond
        ( ( equal x '() )
            '()
        )
        ( ( singleton-p x )
            x
        )
        ( t
            ( let ( ( element ( pick x ) ) )
                ( cons element ( random-permutation ( remove element x :count 1 ) ) )
            )
        )
    )
)