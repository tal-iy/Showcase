
;; Load helper functioins
( load "RecursiveListProcessing.lisp" )

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; PART 1: The Deck and the Shuffle
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

( defun make-deck ()
    ( mapcan #'make-selected-cards '(club diamond heart spade) )
)

( defun make-selected-cards ( suit &aux ranks )
    ( setf ranks '(2 3 4 5 6 7 8 9 10 jack queen king ace) )
    ( setf suit-duplicates ( duplicate ( length ranks ) suit ) )
    ( mapcar #'cons ranks suit-duplicates )
)

( defun demo--make-deck ()
    ( format t ">>> Testing: make-deck~%" )
    ( setf deck ( make-deck ) )
    ( format t "--- deck = ~A~%" deck )
    ( format t "--- number of cards in deck = ~A~%" ( length deck ) )
    nil
)

( defun shuffle-deck ()
    ( setf *deck* ( shuffle ( make-deck ) ) )
    nil
)

( defun shuffle ( deck )
    ( cond
        ( ( equal deck '() )
            '()
        )
        ( t
            ( let ( ( element ( pick deck ) ) )
                ( cons element ( shuffle ( remove element deck :count 1 ) ) )
            )
        )
    )
)

( defun demo--shuffle-deck ()
    ( format t ">>> Testing: shuffle-deck~%" )
    ( shuffle-deck )
    ( format t "--- *deck* = ~A~%" *deck* )
    ( format t "--- number of cards in *deck* = ~A~%" ( length *deck*))
    nil
)

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; PART 2: The Hands, the Deal, and the Discard
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

( defun deal-hands ()
    ( shuffle-deck )
    ( setf *hand1* () )
    ( setf *hand2* () )
    ( deal-card-to-hand1 )
    ( deal-card-to-hand2 )
    ( deal-card-to-hand1 )
    ( deal-card-to-hand2 )
    ( deal-card-to-hand1 )
    ( deal-card-to-hand2 )
    nil
)

( defun deal-card-to-hand1 ()
    ( setf *hand1* ( snoc ( first *deck* ) *hand1* ) )
    ( setf *deck* ( cdr *deck* ) )
)

( defun deal-card-to-hand2 ()
    ( setf *hand2* ( snoc ( first *deck* ) *hand2* ) )
    ( setf *deck* ( cdr *deck* ) )
)

( defun demo--deal-hands ()
    ( format t ">>> Testing: deal-hands~%" )
    ( deal-hands )
    ( format t "--- *hand1* = ~A~%" *hand1* )
    ( format t "--- *hand2* = ~A~%" *hand2* )
    ( format t "--- number of cards in *deck* = ~A~%" ( length *deck*))
    nil
)

( defun randomly-discard-cards ()
    ( randomly-discard-card-from-hand1 )
    ( randomly-discard-card-from-hand2 )
    nil
)

( defun randomly-discard-card-from-hand1 ()
    ( setf ( nth ( random ( length *hand1* ) ) *hand1* ) '() )
)

( defun randomly-discard-card-from-hand2 ()
    ( setf ( nth ( random ( length *hand2* ) ) *hand2* ) '() )
)

( defun demo--randomly-discard-cards ()
    ( format t ">>> Testing: randomly-discard-cards~%" )
    ( deal-hands )
    ( format t "--- *hand1* = ~A~%" *hand1* )
    ( format t "--- *hand2* = ~A~%" *hand2* )
    ( randomly-discard-cards )
    ( format t "--- *hand1* = ~A~%" *hand1* )
    ( format t "--- *hand2* = ~A~%" *hand2* )
    nil
)

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; PART 3: Replacing Cards in Hands, Taking Turns
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

( defun replace-cards ()
    ( replace-card-in-hand1 )
    ( replace-card-in-hand2 )
    nil
)

( defun replace-card-in-hand1 ()
    ( setf *hand1* ( substitute ( first *deck* ) '() *hand1* ) )
    ( setf *deck* ( cdr *deck* ) )
)

( defun replace-card-in-hand2 ()
    ( setf *hand2* ( substitute ( first *deck* ) '() *hand2* ) )
    ( setf *deck* ( cdr *deck* ) )
)

( defun demo--replace-cards ()
    ( format t ">>> Testing: replace-cards~%" )
    ( deal-hands )
    ( format t "--- *hand1* = ~A~%" *hand1* )
    ( format t "--- *hand2* = ~A~%" *hand2* )
    ( randomly-discard-cards )
    ( format t "--- *hand1* = ~A~%" *hand1* )
    ( format t "--- *hand2* = ~A~%" *hand2* )
    ( replace-cards )
    ( format t "--- *hand1* = ~A~%" *hand1* )
    ( format t "--- *hand2* = ~A~%" *hand2* )
    nil
)

( defun players-each-take-a-turn ()
    ( randomly-discard-cards )
    ( replace-cards )
    nil
)

( defun demo--players-each-take-a-turn ()
    ( format t ">>> Testing: players-each-take-a-turn~%" )
    ( deal-hands )
    ( format t "--- The hands ...~%" )
    ( format t "--- *hand1* = ~A~%" *hand1* )
    ( format t "--- *hand2* = ~A~%" *hand2* )
    ( players-each-take-a-turn )
    ( format t "--- Each player takes a turn ...~%" )
    ( format t "--- *hand1* = ~A~%" *hand1* )
    ( format t "--- *hand2* = ~A~%" *hand2* )
    ( players-each-take-a-turn )
    ( format t "--- Each player takes a turn ...~%" )
    ( format t "--- *hand1* = ~A~%" *hand1* )
    ( format t "--- *hand2* = ~A~%" *hand2* )
    ( players-each-take-a-turn )
    ( format t "--- Each player takes a turn ...~%" )
    ( format t "--- *hand1* = ~A~%" *hand1* )
    ( format t "--- *hand2* = ~A~%" *hand2* )
    ( players-each-take-a-turn )
    ( format t "--- Each player takes a turn ...~%" )
    ( format t "--- *hand1* = ~A~%" *hand1* )
    ( format t "--- *hand2* = ~A~%" *hand2* )
    nil
)

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; PART 4: Hand Analysis
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

( defun flush-p ( cards )
    ( cond
        ( ( equal cards '() )
            t
        )
        ( ( singleton-p cards )
            t
        )
        ( ( equal ( first ( mapcar 'cdr cards ) ) ( second ( mapcar 'cdr cards ) ) )
            ( flush-p ( cdr cards ) )
        )
        ( t
            nil
        )
    )
)

( defun demo--flush-p ( &aux hand )
    ( format t ">>> Testing: flush-p~%" )
    ( setf hand '( ( 2 . club ) ( ace . club ) ( 10 . club ) ) )
    ( format t "~A " hand )
    ( if ( flush-p hand )
        ( format t "is a flush~%" )
        ( format t "is not a flush~%" )
    )
    ( setf hand '( ( jack . diamond ) ( 9 . diamond ) ( 5 . diamond ) ) )
    ( format t "~A " hand )
    ( if ( flush-p hand )
        ( format t "is a flush~%" )
        ( format t "is not a flush~%" )
    )
    ( setf hand '( ( jack . heart ) ( 10 . heart ) ( 9 . heart ) ) )
    ( format t "~A " hand )
    ( if ( flush-p hand )
        ( format t "is a flush~%" )
        ( format t "is not a flush~%" )
    )
    ( setf hand '( ( 2 . spade) ( 3 . spade ) ( ace . spade ) ) )
    ( format t "~A " hand )
    ( if ( flush-p hand )
        ( format t "is a flush~%" )
        ( format t "is not a flush~%" )
    )
    ( setf hand '( ( 10 . spade) ( 5 . diamond ) ( ace . spade ) ) )
    ( format t "~A " hand )
    ( if ( flush-p hand )
        ( format t "is a flush~%" )
        ( format t "is not a flush~%" )
    )
    ( setf hand '( ( 8 . club) ( 9 . diamond ) ( 10 . heart ) ) )
    ( format t "~A " hand )
    ( if ( flush-p hand )
        ( format t "is a flush~%" )
        ( format t "is not a flush~%" )
    )
)

( defun high-card ( cards )
    ( setf ranks '(2 3 4 5 6 7 8 9 10 jack queen king ace) )
    ( setf scores ( mapcar 'position ( mapcar 'car cards ) ( duplicate ( length cards ) ranks ) ) )
    ( setf pos ( position ( apply 'max scores ) scores ) )
    ( nth pos cards )
)

( defun demo--high-card ()
    ( format t ">>> Testing: high-card~%" )
    ( setf hand '( ( 10 . heart ) ( 5 . club ) ( queen . spade ) ( 7 . heart ) ) )
    ( format t "~A is the high card of~% ~A~%" ( high-card hand ) hand )
    ( setf hand '( ( 2 . diamond ) ( 2 . club ) ( 10 . heart ) ( 4 . diamond )
    ( ace . club ) ) )
    ( format t "~A is the high card of~% ~A~%" ( high-card hand ) hand )
    ( setf hand '( ( ace . diamond ) ( ace . club ) ( 5 . spade ) ) )
    ( format t "~A is the high card of~% ~A~%" ( high-card hand ) hand )
    nil
)

( defun straight-p ( cards )
    ( setf ranks '(2 3 4 5 6 7 8 9 10 jack queen king ace) )
    ( setf scores ( mapcar 'position ( mapcar 'car cards ) ( duplicate ( length cards ) ranks ) ) )
    ( setf sorted ( sort scores '< ) )
    ( setf difs ( rdc ( mapcar '- ( snoc '0 ( cdr sorted ) ) sorted ) ) )
    ( every 'equal difs ( rest difs ) )
)

( defun demo--straight-p ()
    ( format t ">>> Testing: straight-p~%" )
    ( setf hand '( ( 5 . spade) ( 3 . diamond ) ( 4 . spade ) ( 6 . club )) )
    ( format t "~A " hand )
    ( if ( straight-p hand )
        ( format t "is a straight~%" )
        ( format t "is not a straight~%" )
    )
    ( setf hand '( ( 5 . spade) ( 7 . diamond ) ( 4 . spade ) ( 8 . club ) ) )
    ( format t "~A " hand )
    ( if ( straight-p hand )
        ( format t "is a straight~%" )
        ( format t "is not a straight~%" )
    )
    ( setf hand '( ( king . heart ) ( queen . diamond ) ( ace . spade ) ( 10 . club )
    ( jack . diamond ) ) )
    ( format t "~A " hand )
    ( if ( straight-p hand )
        ( format t "is a straight~%" )
        ( format t "is not a straight~%" )
    )
    ( setf hand '( ( ace . club ) ( 2 . diamond ) ( 3 . spade ) ) )
    ( format t "~A " hand )
    ( if ( straight-p hand )
        ( format t "is a straight~%" )
        ( format t "is not a straight~%" )
    )
    nil
)

( defun analyze-hand ( cards ) 
    ( cond
        ( ( not ( flush-p cards ) )
            'BUST
        )
        ( ( straight-p cards )
            ( list ( car ( high-card cards ) ) 'HIGH 'STRAIGHT ( cdar cards ) 'FLUSH )
        )
        ( t
            ( list ( car ( high-card cards ) ) 'HIGH ( cdar cards ) 'FLUSH )
        )
    )
)

( defun demo--analyze-hand ()
    ( format t ">>> Testing: analyze-hand~%" )
    ( setf hand '( ( 5 . spade) ( 3 . diamond ) ( 4 . spade ) ) )
    ( format t "~A is a ~A~%" hand ( analyze-hand hand ) )
    ( setf hand '( ( 5 . club) ( 9 . club ) ( 4 . club ) ) )
    ( format t "~A is a ~A~%" hand ( analyze-hand hand ) )
    ( setf hand '( ( queen . heart ) ( ace . heart ) ( king . heart ) ) )
    ( format t "~A is a ~A~%" hand ( analyze-hand hand ) )
    nil
)

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; PART 5: Game State and End of Game Reporting
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

( defun analyze-game ()
    ( setf *game-state* ( list ( analyze-hand *hand1* ) ( analyze-hand *hand2* ) ) )
)

( defun demo--analyze-game ()
    ( format t ">>> Testing: analyze-game~%" )
    ; a couple of busts
    ( format t "Game 1 ... ~%" )
    ( setf *hand1* '( ( 2 . diamond ) ( 4 . diamond ) ( jack . heart ) ) )
    ( setf *hand2* '( ( 10 . spade ) ( king . heart ) ( queen . heart ) ) )
    ( analyze-game )
    ( format t "*hand1* = ~A~%" ( write-to-string *hand1* ) )
    ( format t "*hand2* = ~A~%" *hand2* )
    ( format t "*game-state* = ~A~%" *game-state* )
    ; an ordinary flush and a straight flush
    ( format t "Game 2 ... ~%" )
    ( setf *hand1* '( ( 10 . diamond ) ( jack . diamond ) ( 2 . diamond ) ) )
    ( setf *hand2* '( ( 3 . spade ) ( 5 . spade ) ( 4 . spade ) ) )
    ( analyze-game )
    ( format t "*hand1* = ~A~%" ( write-to-string *hand1* ) )
    ( format t "*hand2* = ~A~%" *hand2* )
    ( format t "*game-state* = ~A~%" *game-state* )
    nil
)

( defun report-the-result ()
    ( cond
        ( ( equal *game-state* '( bust bust ) )
            ( increment '*draw-count* )
            ( format t "--> The game is a draw. The deck is dead.~%")
        )
        ( ( and ( not ( equal ( first *game-state* ) 'bust ) ) ( equal ( second *game-state* ) 'bust ) )
            ( increment '*win1-count* )
            ( format t "--> Player 1 wins with ~A~%" ( first *game-state* ) )
        )
        ( ( and ( equal ( first *game-state* ) 'bust ) ( not ( equal ( second *game-state* ) 'bust ) ) )
            ( increment '*win2-count* )
            ( format t "--> Player 2 wins with ~A~%" ( second *game-state* ) )
        )
        ( ( and ( straight-p *hand1* ) ( not ( straight-p *hand2* ) ) )
            ( increment '*f1f2-count* )
            ( increment '*win1-count* )
            ( format t "!!! Both players found their way to a flush~%" )
            ( format t "--> Player 1 wins with ~A~%" ( first *game-state* ) )
        )
        ( ( and ( not ( straight-p *hand1* ) ) ( straight-p *hand2* ) )
            ( increment '*f1f2-count* )
            ( increment '*win2-count* )
            ( format t "!!! Both players found their way to a flush~%" )
            ( format t "--> Player 2 wins with ~A~%" ( second *game-state* ) )
        )
        ( ( card-greater ( high-card *hand1* ) ( high-card *hand2* ) )
            ( increment '*f1f2-count* )
            ( increment '*win1-count* )
            ( format t "!!! Both players found their way to a flush~%" )
            ( format t "--> Player 1 wins with ~A~%" ( first *game-state* ) )
        )
        ( ( card-greater ( high-card *hand2* ) ( high-card *hand1* ) )
            ( increment '*f1f2-count* )
            ( increment '*win2-count* )
            ( format t "!!! Both players found their way to a flush~%" )
            ( format t "--> Player 2 wins with ~A~%" ( second *game-state* ) )
        )
    )
    nil
)

( defun card-greater ( card1 card2 )
    ( setf ranks '(2 3 4 5 6 7 8 9 10 jack queen king ace) )
    ( setf suits '(club diamond heart spade) )
    ( cond
        ( ( > ( position ( car card1 ) ranks ) ( position ( car card2 ) ranks ) )
            t
        )
        ( ( < ( position ( car card1 ) ranks ) ( position ( car card2 ) ranks ) )
            nil
        )
        ( ( > ( position ( cdr card1 ) suits ) ( position ( cdr card2 ) suits ) )
            t
        )
        ( t
            nil
        )
    )
)

( defun demo--report-the-result ()
    ( format t ">>> Testing: report-the-result~%" )
    
    ; two busts
    
    ( format t "Game 1 ... ~%" )
    ( setf *hand1* '( ( 2 . diamond ) ( 4 . diamond ) ( jack . heart ) ) )
    ( setf *hand2* '( ( 10 . spade ) ( king . heart ) ( queen . heart ) ) )
    ( analyze-game )
    ( format t "v*hand1* = ~A~%" *hand1* )
    ( format t "v*hand2* = ~A~%" *hand2* )
    ( report-the-result )
    
    ; a flush and a bust
    
    ( format t "Game 2 ... ~%" )
    ( setf *hand1* '( ( 2 . diamond ) ( 4 . diamond ) ( jack . diamond ) ) )
    ( setf *hand2* '( ( 10 . spade ) ( king . heart ) ( queen . heart ) ) )
    ( analyze-game )
    ( format t "i*hand1* = ~A~%" *hand1* )
    ( format t "i*hand2* = ~A~%" *hand2* )
    ( report-the-result )
    
    ; a bust and a flush
    
    ( format t "Game 3 ... ~%" )
    ( setf *hand1* '( ( 2 . diamond ) ( 4 . heart ) ( jack . diamond ) ) )
    ( setf *hand2* '( ( 10 . heart ) ( king . heart ) ( queen . heart ) ) )
    ( analyze-game )
    ( format t "t*hand1* = ~A~%" *hand1* )
    ( format t "t*hand2* = ~A~%" *hand2* )
    ( report-the-result )
    
    ; a flush and a straight flush
    
    ( format t "Game 4 ... ~%" )
    ( setf *hand1* '( ( 10 . diamond ) ( jack . diamond ) ( 2 . diamond ) ) )
    ( setf *hand2* '( ( 3 . spade ) ( 5 . spade ) ( 4 . spade ) ) )
    ( analyze-game )
    ( format t "a*hand1* = ~A~%" *hand1* )
    ( format t "a*hand2* = ~A~%" *hand2* )
    ( report-the-result )
    
    ; a straight flush and a flush
    
    ( format t "Game 5 ... ~%" )
    ( setf *hand1* '( ( 10 . diamond ) ( jack . diamond ) ( 9 . diamond ) ) )
    ( setf *hand2* '( ( 3 . spade ) ( 7 . spade ) ( 4 . spade ) ) )
    ( analyze-game )
    ( format t "l*hand1* = ~A~%" *hand1* )
    ( format t "l*hand2* = ~A~%" *hand2* )
    ( report-the-result )
    
    ; two flushes with different high cards
    
    ( format t "Game 6 ... ~%" )
    ( setf *hand1* '( ( ace . diamond ) ( jack . diamond ) ( 4 . diamond ) ) )
    ( setf *hand2* '( ( 3 . spade ) ( 7 . spade ) ( 4 . spade ) ) )
    ( analyze-game )
    ( format t "i*hand1* = ~A~%" *hand1* )
    ( format t "i*hand2* = ~A~%" *hand2* )
    ( report-the-result )
    
    ; two flushes with different suits
    
    ( format t "Game 6 ... ~%" )
    ( setf *hand1* '( ( ace . diamond ) ( jack . diamond ) ( 4 . diamond ) ) )
    ( setf *hand2* '( ( ace . spade ) ( 7 . spade ) ( 4 . spade ) ) )
    ( analyze-game )
    ( format t "y*hand1* = ~A~%" *hand1* )
    ( format t "y*hand2* = ~A~%" *hand2* )
    ( report-the-result )
    
    nil
)

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; PART 6: Play Game
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

( defun hand-rep ( cards )
    ( setf ranks ( mapcar 'car cards ) )
    ( setf nranks ( mapcar #'(lambda ( old ) 
                ( cond
                    ( ( member old '(10 jack queen king ace) )
                        ( nth ( position old '(10 jack queen king ace) ) '(x j q k a) )
                    )
                    ( t
                        old
                    )
                )
            ) ranks
        )
    )
    ( setf suits ( mapcar 'cdr cards ) )
    ( setf nsuits ( mapcar #'(lambda ( old ) 
                ( cond
                    ( ( member old '(club diamond heart spade) )
                        ( nth ( position old '(club diamond heart spade) ) '(c d h s) )
                    )
                    ( t
                        old
                    )
                )
            ) suits
        )
    )
    ( mapcar 'cons nranks nsuits )
)

( defun demo--hand-rep ( &aux hand )
    ( shuffle-deck )
    ( setf internal ( list ( pop *deck* ) ( pop *deck* ) ( pop *deck* ) ) )
    ( setf external ( hand-rep internal ) )
    ( format t "~A --> ~A~%" internal external )
    nil
)

( defun play-game ()
    ( increment '*game-count* )
    ( deal-hands )
    ( make-moves )
    ( report-the-result )
)

( defun make-moves ()
    ( increment '*turn-count* )
    ( format t "~A ~A~%" ( hand-rep *hand1* ) ( hand-rep *hand2* ) )
    ( if ( not ( game-over-p ) )
        ( let ()
            ( players-each-take-a-turn )
            ( make-moves )
        )
    )
    nil
)

( defun game-over-p ()
    ( analyze-game )
    ( or
        ( not ( equal *game-state* '( bust bust ) ) )
        ( null *deck* )
    )
)

( defun demo--play-game ()
    ( format t ">>> Testing: play-game~%" )
    ( play-game )
)

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; PART 7: Computing Statistics
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

; Counter initialization -- initialize once so the game can be played regardless of
; whether or not statistics are being computed

( defun init-counters ()
    ( setf *win1-count* 0 )
    ( setf *win2-count* 0 )
    ( setf *draw-count* 0 )
    ( setf *turn-count* 0 )
    ( setf *f1f2-count* 0 )
    ( setf *game-count* 0 )
    nil
)

( init-counters )

; Flexible counter incrementation
( defun increment (name)
    ( set name ( + ( eval name ) 1 ) )
    nil
)

; The main statistics computation program
( defun compute-statistics ( n )
    ( init-counters )
    ( play-game-n-times n )
    ( format t "*vgame-count* = ~A~%" *game-count* )
    ( format t "*iturn-count* = ~A~%" *turn-count* )
    ( format t "*twin1-count* = ~A~%" *win1-count* )
    ( format t "*awin2-count* = ~A~%" *win2-count* )
    ( format t "*ldraw-count* = ~A~%" *draw-count* )
    ( format t "*if1f2-count* = ~A~%" *f1f2-count* )
    nil
)

; Program to play the game n times
( defun play-game-n-times ( n )
    ( cond
        ( (> n 0)
            ( play-game )
            ( play-game-n-times ( - n 1 ))
        )
    )
)

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; PART 8: The Heuristic Player
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

( defun players-each-take-a-turn ()
    ( randomly-heuristically-discard-cards )
    ( replace-cards )
    nil
)

( defun randomly-heuristically-discard-cards ()
    ( randomly-discard-card-from-hand1 )
    ( heuristic-discard-card-from-hand2 )
    nil
)

( defun heuristic-discard-card-from-hand2 ()
    ( setf ranks '(2 3 4 5 6 7 8 9 10 jack queen king ace) )
    ( setf suits '(club diamond heart spade) )
    ( setf rankscores ( mapcar 'position ( mapcar 'car *hand2* ) ( duplicate ( length *hand2* ) ranks ) ) )
    ( setf suitscores ( mapcar 'position ( mapcar 'cdr *hand2* ) ( duplicate ( length *hand2* ) suits ) ) )
    
    ( cond
        ( ( and ( equal ( first suitscores ) ( second suitscores ) ) ( equal ( first suitscores ) ( third suitscores ) ) )
            ( setf pos ( position ( apply 'min rankscores ) rankscores ) )
        )
        ( ( equal ( first suitscores ) ( second suitscores ) )
            ( setf pos 2 )
        )
        ( ( equal ( first suitscores ) ( third suitscores ) )
            ( setf pos 1 )
        )
        ( ( equal ( second suitscores ) ( third suitscores ) )
            ( setf pos 0 )
        )
        ( t
            ( setf pos ( position ( apply 'min rankscores ) rankscores ) )
        )
    )
    ( setf ( nth pos *hand2* ) '() )
)

( defun demo--randomly-heuristically-discard-cards ()
    ( format t ">>> Testing: randomly-discard-cards~%" )
    ( deal-hands )
    ( format t "--- *hand1* = ~A~%" *hand1* )
    ( format t "--- *hand2* = ~A~%" *hand2* )
    ( randomly-heuristically-discard-cards )
    ( format t "--- *hand1* = ~A~%" *hand1* )
    ( format t "--- *hand2* = ~A~%" *hand2* )
    nil
)