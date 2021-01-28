( defun mc ()
    ( establish-world )
    ( init-move-list )
    ( make-moves )
)

( defun make-moves ()
    ( display-world )
    ( cond
        ( ( goalp )
            ( write-line "Good work!" )
            nil
        )
        ( ( feast-state-p )
            ( write-line "Yummy yummy yummy, I got Good in my tummy!!" )
            nil
        )
        ( t
            ( let ( m )
                ( format t ">>> " ) ( setf m ( read ) )
                ( if ( applicable-p m )
                    ( let () ( perform-move m ) ( make-moves ) )
                    ( let () ( write-line "Move inapplicable" ) nil )
                )
            )
        )
    )
)

( defun perform-move ( move )
    ( setf *move-list* ( snoc move *move-list* ) )
    ( if ( equal ( current-bank ) *left-bank* )
        ( move-lr move )
        ( move-rl move )
    )
)

( defun move-lr ( ml )
    ( if ( null ml ) ( return-from move-lr ) )
    ( move-lr-1 ( first ml ) )
    ( move-lr ( rest ml ) )
)

( defun move-rl ( ml )
    ( if ( null ml ) ( return-from move-rl ) )
    ( move-rl-1 ( first ml ) )
    ( move-rl ( rest ml ) )
)

( defun establish-world ()
    ( setf *left-bank* '(M M M C C C B) )
    ( setf *right-bank* '() )
)

( defun init-move-list ()
    ( setf *move-list* '() )
)

( defun display-world ()
    ( princ "*left-bank* " )
    ( write *left-bank* )
    ( write-line "" )
    ( princ "*right-bank* " )
    ( write *right-bank* )
    ( write-line "" )
)

( defun goalp ()
    ( and 
        ( = ( count 'M *right-bank* ) 3 ) 
        ( = ( count 'C *right-bank* ) 3 ) 
        ( = ( count 'B *left-bank* ) 0 )
    )
)

( defun feast-state-p ()
    ( or 
        ( and 
            ( > ( count 'M *left-bank* ) 0 ) 
            ( > ( count 'C *left-bank* ) ( count 'M *left-bank* ) ) 
        ) 
        ( and 
            ( > ( count 'M *right-bank* ) 0 ) 
            ( > ( count 'C *right-bank* ) ( count 'M *right-bank* ) ) 
        )
    )
)

( defun applicable-p ( move )
    ( and 
        ( = ( count 'B move ) 1 ) 
        ( or 
            ( > ( count 'M move ) 0 ) 
            ( > ( count 'C move ) 0 ) 
        )
        ( <= ( count 'M move ) ( count 'M ( current-bank ) ) )
        ( <= ( count 'C move ) ( count 'C ( current-bank ) ) )
    )
)

( defun current-bank ()
    ( if ( = ( count 'B *left-bank* ) 1 )
        *left-bank*
        *right-bank*
    )
)

( defun move-lr-1 ( move )
    ( setf *left-bank* ( remove move *left-bank* :count 1 ) )
    ( setf *right-bank* ( append ( list move ) *right-bank* ) )
)

( defun move-rl-1 ( move )
    ( setf *right-bank* ( remove move *right-bank* :count 1 ) )
    ( setf *left-bank* ( append ( list move ) *left-bank* ) )
)

( defun display-solution ( &optional rest)
    ( if ( equal *move-list* NIL )
        NIL
        ( if ( equal rest NIL )
            ( let () 
                ( write ( car *move-list* ) ) 
                ( write-line "" ) 
                ( display-solution ( cdr *move-list* ) ) 
            )
            ( let () 
                ( write ( car rest ) ) 
                ( write-line "" )
                ( if ( equal ( cdr rest ) NIL )
                    NIL
                    ( display-solution ( cdr rest ) ) 
                )
            )
        )
    )
)

( defun snoc ( move move-list )
    ( append move-list ( list move ) )
)