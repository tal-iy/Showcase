;---------------------------------------------------------------------------------
; DESCRIPTION
;
; This is a state space solver for the missionaries and cannibals problem. The
; solution is found using a breadth first search, keeping track of explored states.
;---------------------------------------------------------------------------------
; REPRESENTATIONAL NOTES
;
; Banks are represented as a 3-slot class consisting of
; missionaries, cannibals, and boat.
;
; States are represented as a 2-slot class consisting of
; left-bank and right-bank.
;
; Operators are represented as a 3-slot class consisting of
; a name, a precondition, and a description.
;
; Nodes are represented as a 4-slot class consisting of
; a name, a state, a parent node, and a move (state space operator)
;---------------------------------------------------------------------------------

;------------------------------------------------------------------
; BANK MODEL
;------------------------------------------------------------------

( defclass bank ()
    (
        ( missionaries accessor bank-missionaries initarg missionaries )
        ( cannibals accessor bank-cannibals initarg cannibals )
        ( boat accessor bank-boat initarg boat )
    )
)

( defmethod display ( ( b bank ) )
    ( format t MISSIONARIES ~A CANNIBALS ~A BOAT ~A ~% ( bank-missionaries b ) ( bank-cannibals b ) ( bank-boat b ) )
    nil
)

( defmethod copy-bank ( (b bank ) )
    ( make-instance 'bank missionaries ( bank-missionaries b ) cannibals ( bank-cannibals b ) boat ( bank-boat b ) )
)

( defmethod add-cannibal ( ( b bank ) )
    ( setf ( bank-cannibals b ) ( cons 'c ( bank-cannibals b ) ) )
    nil
)

( defmethod add-missionary ( ( b bank ) )
    ( setf ( bank-missionaries b ) ( cons 'm ( bank-missionaries b ) ) )
    nil
)

( defmethod add-boat ( ( b bank ) )
    ( setf ( bank-boat b ) 'b )
    nil
)

( defmethod remove-cannibal ( ( b bank ) )
    ( setf ( bank-cannibals b ) ( cdr ( bank-cannibals b ) ) )
    nil
)

( defmethod remove-missionary ( ( b bank ) )
    ( setf ( bank-missionaries b ) ( cdr ( bank-missionaries b ) ) )
    nil
)

( defmethod remove-boat ( ( b bank ) )
    ( setf ( bank-boat b ) nil )
    nil
)

( defmethod has-boat-p ( ( b bank ) )
    ( equal ( bank-boat b ) 'b )
)

( defmethod count-cannibals ( ( b bank ) )
    ( length ( bank-cannibals b ) )
)

( defmethod count-missionaries ( ( b bank ) )
    ( length ( bank-missionaries b ) )
)

( defmethod equal-bank-p ( ( b1 bank ) ( b2 bank ) )
    ( and 
        ( = ( count-missionaries b1 ) ( count-missionaries b2 ) )
        ( = ( count-cannibals b1 ) ( count-cannibals b2 ) )
        ( equal ( bank-boat b1 ) ( bank-boat b2 ) )
    )
)

;------------------------------------------------------------------
; STATE MODEL
;------------------------------------------------------------------

( defclass state ()
    (
        ( left-bank accessor state-left-bank initarg left-bank )
        ( right-bank accessor state-right-bank initarg right-bank )
    )
)

( defmethod display ( ( s state ) )
    ( display ( state-left-bank s ) )
    ( display ( state-right-bank s ) )
    nil
)

( defmethod copy-state ( (s state ) &aux left right)
    ( setf left ( copy-bank ( state-left-bank s ) ) )
    ( setf right ( copy-bank ( state-right-bank s ) ) )
    ( make-instance 'state left-bank left right-bank right )
)

; Is it a feast state
( defmethod feast-state-p ( ( s state ) )
    ( or
        ( and 
            (  ( count-cannibals ( state-left-bank s ) ) ( count-missionaries ( state-left-bank s ) ) ) 
            ( = ( count-missionaries ( state-left-bank s ) ) 0 )
        )
        ( and 
            (  ( count-cannibals ( state-right-bank s ) ) ( count-missionaries ( state-right-bank s ) ) ) 
            ( = ( count-missionaries ( state-right-bank s ) ) 0 )
        )
    )
)

; Is it a goal state
( defmethod goalp ( ( s state ) )
    ( and 
        ( = ( count-cannibals ( state-right-bank s ) ) 3 )
        ( = ( count-missionaries ( state-right-bank s ) ) 3 )
        ( has-boat-p ( state-right-bank s ) )
    )
)

; Is it an explored state
( defmethod exploredp ( ( s state ) )
    ( member s explored key #'node-state test #'equal-state-p )
)

( defmethod equal-state-p ( ( s1 state ) ( s2 state ) )
    ( and
        ( equal-bank-p ( state-left-bank s1 ) ( state-left-bank s2 ) )
        ( equal-bank-p ( state-right-bank s1 ) ( state-right-bank s2 ) )
    )
)

;------------------------------------------------------------------
; NODE MODEL
;------------------------------------------------------------------

( defclass node ()
    (
        ( name accessor node-name initarg name )
        ( state accessor node-state initarg state )
        ( parent accessor node-parent initarg parent )
        ( operator accessor node-operator initarg operator )
    )
)

( defmethod display ( ( n node ) )
    ( format t ~A  ( node-name n ) )
    ( if ( not ( rootp n ) )
        ( let ()
            ( format t ~A  ( node-name ( node-parent n ) ) )
            ( display ( node-operator n ) )
        )
    )
    ( terpri )
    ( display ( node-state n ) )
    nil
)

( defmethod rootp ( ( n node ) )
    ( eq ( node-name n ) 'root )
)

;------------------------------------------------------------------
; OPERATOR MODEL
;------------------------------------------------------------------

( defclass operator ()
    (
        ( name accessor operator-name initarg name )
        ( precondition accessor operator-precondition initarg precondition )
        ( description accessor operator-description initarg description )
    )
)

( defmethod describe-operators ()
    ( mapcar #'describe-operator operator-list )
    ( terpri )
    nil
)

( defmethod display ( ( op operator ) )
    ( format t ~A ( operator-name op ) )
)

( defmethod describe-operator ( ( op operator ) )
    ( format t Operator name ~A~% ( operator-name op ) )
    ( format t Precondition ~A~% ( operator-precondition op ) )
    ( format t Description ~A~%~% ( operator-description op ) )
)

; Checking for applicability
( defmethod applicablep ( (op operator) (n node) &aux s)
    ( setf s ( node-state n ) )
    ( cond
        ( ( eq ( operator-name op ) 'move-cc-lr )
            ( applicable-move-cc-lr-p s )
        )
        ( ( eq ( operator-name op ) 'move-cm-lr )
            ( applicable-move-cm-lr-p s );v
        )
        ( ( eq ( operator-name op ) 'move-mm-lr )
            ( applicable-move-mm-lr-p s )
        )
        ( ( eq ( operator-name op ) 'move-c-lr )
            ( applicable-move-c-lr-p s );i
        )
        ( ( eq ( operator-name op ) 'move-m-lr )
            ( applicable-move-m-lr-p s )
        )
        ( ( eq ( operator-name op ) 'move-cc-rl )
            ( applicable-move-cc-rl-p s );t
        )
        ( ( eq ( operator-name op ) 'move-cm-rl )
            ( applicable-move-cm-rl-p s );a
        )
        ( ( eq ( operator-name op ) 'move-mm-rl )
            ( applicable-move-mm-rl-p s );l
        )
        ( ( eq ( operator-name op ) 'move-c-rl )
            ( applicable-move-c-rl-p s );i
        )
        ( ( eq ( operator-name op ) 'move-m-rl )
            ( applicable-move-m-rl-p s );y
        )
    )
)

( defmethod applicable-move-cc-lr-p ( (s state) &aux b )
    ( setf b ( state-left-bank s ) )
    ( and 
        ( has-boat-p b )
        ( = ( count-cannibals b ) 2 )
    )
)

( defmethod applicable-move-cm-lr-p ( (s state) &aux b )
    ( setf b ( state-left-bank s ) )
    ( and 
        ( has-boat-p b )
        ( = ( count-cannibals b ) 1 )
        ( = ( count-missionaries b ) 1 )
    )
)

( defmethod applicable-move-mm-lr-p ( (s state) &aux b )
    ( setf b ( state-left-bank s ) )
    ( and 
        ( has-boat-p b )
        ( = ( count-missionaries b ) 2 )
    )
)

( defmethod applicable-move-c-lr-p ( (s state) &aux b )
    ( setf b ( state-left-bank s ) )
    ( and 
        ( has-boat-p b )
        ( = ( count-cannibals b ) 1 )
    )
)

( defmethod applicable-move-m-lr-p ( (s state) &aux b )
    ( setf b ( state-left-bank s ) )
    ( and 
        ( has-boat-p b )
        ( = ( count-missionaries b ) 1 )
    )
)

( defmethod applicable-move-cc-rl-p ( (s state) &aux b )
    ( setf b ( state-right-bank s ) )
    ( and 
        ( has-boat-p b )
        ( = ( count-cannibals b ) 2 )
    )
)

( defmethod applicable-move-cm-rl-p ( (s state) &aux b )
    ( setf b ( state-right-bank s ) )
    ( and 
        ( has-boat-p b )
        ( = ( count-cannibals b ) 1 )
        ( = ( count-missionaries b ) 1 )
    )
)

( defmethod applicable-move-mm-rl-p ( (s state) &aux b )
    ( setf b ( state-right-bank s ) )
    ( and 
        ( has-boat-p b )
        ( = ( count-missionaries b ) 2 )
    )
)

( defmethod applicable-move-c-rl-p ( (s state) &aux b )
    ( setf b ( state-right-bank s ) )
    ( and 
        ( has-boat-p b )
        ( = ( count-cannibals b ) 1 )
    )
)

( defmethod applicable-move-m-rl-p ( (s state) &aux b )
    ( setf b ( state-right-bank s ) )
    ( and 
        ( has-boat-p b )
        ( = ( count-missionaries b ) 1 )
    )
)

;------------------------------------------------------------------
; ESTABLISHING 10 STATE SPACE OPERATORS
;
; move-cc-lr move-cm-lr move-mm-lr move-c-lr move-m-lr
; move-cc-rl move-cm-rl move-mm-rl move-c-rl move-m-rl
;------------------------------------------------------------------

( defmethod establish-operators ()
    ( setf move-cc-lr
        ( make-instance 'operator
            name 'move-cc-lr
            precondition Boat on the left, at least 2 cannibals on the left.
            description Move ( C C B ) from left bank to right bank.;
        )
    )
    ( setf move-cm-lr
        ( make-instance 'operator
            name 'move-cm-lr
            precondition Boat on the left, at least 1 cannibal and 1 missionary on the left.
            description Move ( C M B ) from left bank to right bank.;v
        )
    )
    ( setf move-mm-lr
        ( make-instance 'operator
            name 'move-mm-lr
            precondition Boat on the left, at least 2 missionaries on the left.
            description Move ( M M B ) from left bank to right bank.;i
        )
    )
    ( setf move-c-lr
        ( make-instance 'operator
            name 'move-c-lr
            precondition Boat on the left, at least 1 cannibal on the left.
            description Move ( C B ) from left bank to right bank.;t
        )
    )
    ( setf move-m-lr
        ( make-instance 'operator
            name 'move-m-lr
            precondition Boat on the left, at least 1 missionary on the left.
            description Move ( M B ) from left bank to right bank.;a
        )
    )
    ( setf move-cc-rl
        ( make-instance 'operator
            name 'move-cc-rl
            precondition Boat on the right, at least 2 cannibals on the right.
            description Move ( C C B ) from right bank to left bank.;l
        )
    )
    ( setf move-cm-rl
        ( make-instance 'operator
            name 'move-cm-rl
            precondition Boat on the right, at least 1 cannibal and 1 missionary on the right.
            description Move ( C M B ) from right bank to left bank.;i
        )
    )
    ( setf move-mm-rl
        ( make-instance 'operator
            name 'move-mm-rl
            precondition Boat on the right, at least 2 missionaries on the right.
            description Move ( M M B ) from right bank to left bank.;y
        )
    )
    ( setf move-c-rl
        ( make-instance 'operator
            name 'move-c-rl
            precondition Boat on the right, at least 1 cannibal on the right.
            description Move ( C B ) from right bank to left bank.;
        )
    )
    ( setf move-m-rl
        ( make-instance 'operator
            name 'move-m-rl
            precondition Boat on the right, at least 1 missionary on the right.
            description Move ( M B ) from right bank to left bank.;
        )
    )
    ( setf operator-list
        ( list move-cc-lr move-cm-lr move-mm-lr move-c-lr move-m-lr
        move-cc-rl move-cm-rl move-mm-rl move-c-rl move-m-rl
        )
    )
    nil
)

( defmethod apply-operator ( ( o operator ) ( s state ) &aux left-bank right-bank )
    ( setf left-bank ( state-left-bank s ) )
    ( setf right-bank ( state-right-bank s ) )
    ( cond
        ( ( eq ( operator-name o ) 'move-cc-lr )
            ( remove-boat left-bank )
            ( remove-cannibal left-bank )
            ( remove-cannibal left-bank )
            ( add-boat right-bank )
            ( add-cannibal right-bank )
            ( add-cannibal right-bank )
        )
        ( ( eq ( operator-name o ) 'move-cm-lr )
            ( remove-boat left-bank )
            ( remove-cannibal left-bank )
            ( remove-missionary left-bank )
            ( add-boat right-bank )
            ( add-cannibal right-bank )
            ( add-missionary right-bank )
        )
        ( ( eq ( operator-name o ) 'move-mm-lr )
            ( remove-boat left-bank )
            ( remove-missionary left-bank )
            ( remove-missionary left-bank )
            ( add-boat right-bank )
            ( add-missionary right-bank )
            ( add-missionary right-bank )
        )
        ( ( eq ( operator-name o ) 'move-c-lr )
            ( remove-boat left-bank )
            ( remove-cannibal left-bank )
            ( add-boat right-bank )
            ( add-cannibal right-bank )
        )
        ( ( eq ( operator-name o ) 'move-m-lr )
            ( remove-boat left-bank )
            ( remove-missionary left-bank )
            ( add-boat right-bank )
            ( add-missionary right-bank )
        )
        ( ( eq ( operator-name o ) 'move-cc-rl )
            ( remove-boat right-bank )
            ( remove-cannibal right-bank )
            ( remove-cannibal right-bank )
            ( add-boat left-bank )
            ( add-cannibal left-bank )
            ( add-cannibal left-bank )
        )
        ( ( eq ( operator-name o ) 'move-cm-rl )
            ( remove-boat right-bank )
            ( remove-cannibal right-bank )
            ( remove-missionary right-bank )
            ( add-boat left-bank )
            ( add-cannibal left-bank )
            ( add-missionary left-bank )
        )
        ( ( eq ( operator-name o ) 'move-mm-rl )
            ( remove-boat right-bank )
            ( remove-missionary right-bank )
            ( remove-missionary right-bank )
            ( add-boat left-bank )
            ( add-missionary left-bank )
            ( add-missionary left-bank )
        )
        ( ( eq ( operator-name o ) 'move-c-rl )
            ( remove-boat right-bank )
            ( remove-cannibal right-bank )
            ( add-boat left-bank )
            ( add-cannibal left-bank )
        )
        ( ( eq ( operator-name o ) 'move-m-rl )
            ( remove-boat right-bank )
            ( remove-missionary right-bank )
            ( add-boat left-bank )
            ( add-missionary left-bank )
        )
    )
)

;------------------------------------------------------------------
; THE MAIN PROGRAM - argument values of e u x eu ex ux eux will cause tracing
;------------------------------------------------------------------
( defmethod mc ( ( trace-instruction symbol ) )
    ( setf trace-instruction trace-instruction )
    ( establish-operators )
    ( setup )
    ( solve )
)

;------------------------------------------------------------------
; BREADTH FIRST SEARCH
;------------------------------------------------------------------
( defmethod solve ( &aux kids e-node )
    ( if ( member trace-instruction '(u eu ux eux) ) ( display-the-unexplored-list ) )
    ( if ( member trace-instruction '(x ex ux eux) ) ( display-the-explored-list ) )
    ( cond
        ( ( null unexplored )
            ( format t  THERE IS NO SOLUTION.~% )
            ( return-from solve NIL )
        )
    )
    ( setf e-node ( pop unexplored ) )
    ( if ( member trace-instruction '(e ex eu eux) ) ( display-the-e-node e-node ) )
    ( cond
        ( ( goalp ( node-state e-node ) )
            ( format t ~% GOT A SOLUTION! )
            ( display-solution e-node )
        )
        ( ( feast-state-p ( node-state e-node ) )
            ( solve )
        )
        ( ( exploredp ( node-state e-node ) )
            ( solve )
        )
        ( t
            ( push e-node explored )
            ( setf kids ( children-of e-node ) )
            ( setf unexplored ( append unexplored kids ) )
            ( solve )
        )
    )
    nil
)

( defmethod display-the-unexplored-list ( )
    ( format t ~% UNEXPLORED LIST~% )
    ( mapcar #'display unexplored )
    nil
)

( defmethod display-the-explored-list ( )
    ( format t ~% EXPLORED LIST~% )
    ( mapcar #'display explored ) 
    nil
)

( defmethod display-the-e-node ( ( e-node node ) )
    ( format t ~% E-NODE~% )
    ( display e-node )
    nil
)

( defmethod display-solution ( (n node) )
    ( cond
        ( ( rootp n )
            ( terpri )
        )
        ( t
            ( display-solution ( node-parent n ) )
            ( format t ~A~% ( operator-description ( node-operator n ) ) )
        )
    )
    nil
)

;------------------------------------------------------------------
; THE SETUP
;------------------------------------------------------------------
( defmethod setup ( &aux root lb rb istate )
    ;; establish root node
    ( setf lb ( make-instance 'bank missionaries '(m m m) cannibals '(c c c) boat 'b ) )
    ( setf rb ( make-instance 'bank missionaries '() cannibals '() boat nil ) )
    ( setf istate ( make-instance 'state left-bank lb right-bank rb ) )
    ( setf root ( make-instance 'node state istate name 'root ) )
    ;; initialize list of unexplored nodes
    ( setf unexplored ( list root ) )
    ;; initialize list of explored nodes
    ( setf explored () )
    ; get ready to create good names
    ( setf ng ( make-instance 'name-generator prefix N ) )
)

;------------------------------------------------------------------
; GENERATING CHILDREN
;------------------------------------------------------------------
( defmethod children-of ( (e-node node) &aux kids )
    ( if ( applicablep move-cc-lr 
        e-node ) ; e-node )
        ( push ( child-of e-node move-cc-lr ) kids )
    )
    ( if ( applicablep move-cm-lr
        e-node ) ; e-node )
        ( push ( child-of e-node move-cm-lr ) kids )
    )
    ( if ( applicablep move-mm-lr
        e-node ) ; v e-node )
        ( push ( child-of e-node move-mm-lr ) kids )
    )
    ( if ( applicablep move-c-lr
        e-node ) ; i e-node )
        ( push ( child-of e-node move-c-lr ) kids )
    )
    ( if ( applicablep move-m-lr
        e-node ) ; t e-node )
        ( push ( child-of e-node move-m-lr ) kids )
    )
    ( if ( applicablep move-cc-rl
        e-node ) ; a e-node )
        ( push ( child-of e-node move-cc-rl ) kids )
    )
    ( if ( applicablep move-cm-rl
        e-node ) ; l e-node )
        ( push ( child-of e-node move-cm-rl ) kids )
    )
    ( if ( applicablep move-mm-rl
        e-node ) ; i e-node )
        ( push ( child-of e-node move-mm-rl ) kids )
    )
    ( if ( applicablep move-c-rl
        e-node ) ; y e-node )
        ( push ( child-of e-node move-c-rl ) kids )
    )
    ( if ( applicablep move-m-rl
        e-node ) ; e-node )
        ( push ( child-of e-node move-m-rl ) kids )
    )
    kids
)

( defmethod child-of ( ( n node ) ( o operator ) &aux c )
    ( setf new-node ( make-instance 'node ) )
    ( setf ( node-name new-node ) ( next ng ) )
    ( setf ( node-parent new-node ) n )
    ( setf ( node-operator new-node ) o )
    ( setf c ( copy-state ( node-state n ) ) )
    ( apply-operator o c )
    ( setf ( node-state new-node ) c )
    new-node
)

( defclass name-generator ()
    ( ( prefix accessor name-generator-prefix initarg prefix initform name )
        ( nr accessor name-generator-nr initform 0 )
    )
)

( defmethod next ( ( ng name-generator ) )
    ( setf ( name-generator-nr ng ) ( + 1 ( name-generator-nr ng ) ) )
    ( concatenate 'string
        ( name-generator-prefix ng )
        ( write-to-string ( name-generator-nr ng ) )
    )
)
