;----------------------------------------------------------------------
; Perceptron Setup
;----------------------------------------------------------------------

( defmethod setup ()

    ; Read the image data file
    ( setf *num-file* ( open "numbers.txt" ) )
    ( setf *num0* ( read *num-file* ) )
    ( setf *num1* ( read *num-file* ) )
    ( setf *num2* ( read *num-file* ) )
    ( setf *num3* ( read *num-file* ) )
    ( setf *num4* ( read *num-file* ) )
    ( setf *num5* ( read *num-file* ) )
    ( setf *num6* ( read *num-file* ) )
    ( setf *num7* ( read *num-file* ) )
    ( setf *num8* ( read *num-file* ) )
    ( setf *num9* ( read *num-file* ) )
    ( close *num-file* )
    
    ( display-images )

    nil
)

( defmethod display-images () 
    ( format t "*num0* = ~A~%" *num0* )
    ( format t "*num1* = ~A~%" *num1* )
    ( format t "*num2* = ~A~%" *num2* )
    ( format t "*num3* = ~A~%" *num3* )
    ( format t "*num4* = ~A~%" *num4* )
    ( format t "*num5* = ~A~%" *num5* )
    ( format t "*num6* = ~A~%" *num6* )
    ( format t "*num7* = ~A~%" *num7* )
    ( format t "*num8* = ~A~%" *num8* )
    ( format t "*num9* = ~A~%" *num9* )
)

; Make a prediction based on an input and perceptron weights
( defmethod predict ( input weight-str &aux biased-input prediction )
    ( setf biased-input ( cons 1 input ) ) ; Add a 1 to the input list to make the first weight a bias
    ( setf prediction ( apply '+ ( mapcar '* biased-input weight-str ) ) ) ; Multiply inputs by weights and sum them together
    ( cond 
        ( ( >= prediction 0 )
            1
        )
        (  t 
            0
        )
    )
)

( setup )

;----------------------------------------------------------------------
; Task 1: Weight String
;----------------------------------------------------------------------

; The number of inputs to the perceptron ( 7 x 8 image pixels )
( defconstant *num-inputs* ( length *num0* ) )

; Generate a random weight between -100 and 100
( defmethod weight () 
	( - ( random 200 ) 100 )
)

; Generate a string of random weights
( defmethod weight-string ( &optional ( len 0 ) )
	( cond
		( ( equal len ( + *num-inputs* 1 ) )
			nil
		)
		( t
			( cons ( weight ) ( weight-string ( + 1 len ) ) )
		)
	)
)

;----------------------------------------------------------------------
; Task 2: Mutation
;----------------------------------------------------------------------

; Mutate a random weight to a new random number between -100 and 100
( defmethod mutation ( ( weight-str list ) &aux position)
	( setf position ( random ( length weight-str ) ) )
	( change weight-str ( weight ) position )
)

; Make a copy of a list and modify one element
( defmethod change ( weight-str chosen position &aux new )
	( setf new ( copy-list weight-str ) )
	( setf ( nth position new ) chosen )
	new
)

;----------------------------------------------------------------------
; Task 3: Crossover
;----------------------------------------------------------------------

; Combine subsets of two lists
( defmethod crossover ( ( m list ) ( f list ) &aux base rest start-pos end-pos)
    
    ( setf start-pos ( random ( length m ) ) ) ; Start at a random spot in the list
    ( setf end-pos ( + start-pos ( random ( - ( length m ) start-pos ) ) ) ) ; End at a random spot after the starting point
    
    ( setf base ( pick ( list m f ) ) ) ; Randomly choose which list to start and end with
    ( cond 
        ( ( equal base m )
            ( append ( first-n m start-pos ) ( middle-n f start-pos end-pos ) ( rest-n m end-pos ) )
        )
        ( t
            ( append ( first-n f start-pos ) ( middle-n m start-pos end-pos ) ( rest-n f end-pos ) )
        )
    )
	
)

; Returns the first n elements of a list
( defmethod first-n ( weight-str position &optional ( len 0 ) )
	( cond
		( ( equal len position )
			nil
		)
		( t
			( cons ( car weight-str ) ( first-n ( cdr weight-str ) position ( + 1 len ) ) )
		)
	)
)

; Returns the middle elements of a list starting at start-pos and ending at end-pos
( defmethod middle-n ( weight-str start-pos end-pos )
	( cond
		( ( equal start-pos end-pos )
			nil
		)
		( t
			( cons ( nth start-pos weight-str ) ( middle-n weight-str ( + start-pos 1 ) end-pos ) )
		)
	)
)

; Returns the last n elements of a list
( defmethod rest-n ( weight-str position )
	( cond
		( ( equal position ( length weight-str ) )
			nil
		)
		( t
			( cons ( nth position weight-str ) ( rest-n weight-str ( + 1 position ) ) )
		)
	)
)

; Pick a random element in a list
( defmethod pick ( l )
	( nth ( random ( length l ) ) l )
)


;----------------------------------------------------------------------
; Task 4: Demos for Mutation and Crossover
;----------------------------------------------------------------------

( defmethod mutation-demo (&aux s m)
	( setf s ( weight-string ) )
	( dotimes ( i 10 )
		( format t "s = ~A~%" s )
		( setf m ( mutation s ) )
		( format t "m = ~A~%~%" m )
	)
)

( defmethod crossover-demo (&aux m f x)
	( setf m ( weight-string ) )
	( setf f ( weight-string ) )
	( dotimes ( i 10 )
		( format t "m = ~A~%" m )
		( setf x ( crossover m f ) )
		( format t "x = ~A~%" x )
		( format t "f = ~A~%~%" f )
	)
)

;----------------------------------------------------------------------
; Task 5: The Fitness Metric
;----------------------------------------------------------------------

( defconstant *right-scale* 100 )
( defconstant *wrong-scale* 10 )

( defmethod fitness-0 ( weight-str &aux right wrong )
	( setf right ( * *right-scale* ( predict *num0* weight-str ) ) ) 
    ( setf wrong ( * *wrong-scale* ( predict *num1* weight-str ) ) )
    ( setf wrong ( + wrong ( * *wrong-scale* ( predict *num2* weight-str ) ) ) )
    ( setf wrong ( + wrong ( * *wrong-scale* ( predict *num3* weight-str ) ) ) )
    ( setf wrong ( + wrong ( * *wrong-scale* ( predict *num4* weight-str ) ) ) )
    ( setf wrong ( + wrong ( * *wrong-scale* ( predict *num5* weight-str ) ) ) )
    ( setf wrong ( + wrong ( * *wrong-scale* ( predict *num6* weight-str ) ) ) )
    ( setf wrong ( + wrong ( * *wrong-scale* ( predict *num7* weight-str ) ) ) )
    ( setf wrong ( + wrong ( * *wrong-scale* ( predict *num8* weight-str ) ) ) )
    ( setf wrong ( + wrong ( * *wrong-scale* ( predict *num9* weight-str ) ) ) )
    ( + right ( - wrong ) )
)

( defmethod fitness-1 ( weight-str &aux right wrong )
	( setf right ( * *right-scale* ( predict *num1* weight-str ) ) ) 
    ( setf wrong ( * *wrong-scale* ( predict *num0* weight-str ) ) )
    ( setf wrong ( + wrong ( * *wrong-scale* ( predict *num2* weight-str ) ) ) )
    ( setf wrong ( + wrong ( * *wrong-scale* ( predict *num3* weight-str ) ) ) )
    ( setf wrong ( + wrong ( * *wrong-scale* ( predict *num4* weight-str ) ) ) )
    ( setf wrong ( + wrong ( * *wrong-scale* ( predict *num5* weight-str ) ) ) )
    ( setf wrong ( + wrong ( * *wrong-scale* ( predict *num6* weight-str ) ) ) )
    ( setf wrong ( + wrong ( * *wrong-scale* ( predict *num7* weight-str ) ) ) )
    ( setf wrong ( + wrong ( * *wrong-scale* ( predict *num8* weight-str ) ) ) )
    ( setf wrong ( + wrong ( * *wrong-scale* ( predict *num9* weight-str ) ) ) )
    ( + right ( - wrong ) )
)

( defmethod fitness-2 ( weight-str &aux right wrong )
	( setf right ( * *right-scale* ( predict *num2* weight-str ) ) ) 
    ( setf wrong ( * *wrong-scale* ( predict *num1* weight-str ) ) )
    ( setf wrong ( + wrong ( * *wrong-scale* ( predict *num0* weight-str ) ) ) )
    ( setf wrong ( + wrong ( * *wrong-scale* ( predict *num3* weight-str ) ) ) )
    ( setf wrong ( + wrong ( * *wrong-scale* ( predict *num4* weight-str ) ) ) )
    ( setf wrong ( + wrong ( * *wrong-scale* ( predict *num5* weight-str ) ) ) )
    ( setf wrong ( + wrong ( * *wrong-scale* ( predict *num6* weight-str ) ) ) )
    ( setf wrong ( + wrong ( * *wrong-scale* ( predict *num7* weight-str ) ) ) )
    ( setf wrong ( + wrong ( * *wrong-scale* ( predict *num8* weight-str ) ) ) )
    ( setf wrong ( + wrong ( * *wrong-scale* ( predict *num9* weight-str ) ) ) )
    ( + right ( - wrong ) )
)

( defmethod fitness-3 ( weight-str &aux right wrong )
	( setf right ( * *right-scale* ( predict *num3* weight-str ) ) ) 
    ( setf wrong ( * *wrong-scale* ( predict *num1* weight-str ) ) )
    ( setf wrong ( + wrong ( * *wrong-scale* ( predict *num2* weight-str ) ) ) )
    ( setf wrong ( + wrong ( * *wrong-scale* ( predict *num0* weight-str ) ) ) )
    ( setf wrong ( + wrong ( * *wrong-scale* ( predict *num4* weight-str ) ) ) )
    ( setf wrong ( + wrong ( * *wrong-scale* ( predict *num5* weight-str ) ) ) )
    ( setf wrong ( + wrong ( * *wrong-scale* ( predict *num6* weight-str ) ) ) )
    ( setf wrong ( + wrong ( * *wrong-scale* ( predict *num7* weight-str ) ) ) )
    ( setf wrong ( + wrong ( * *wrong-scale* ( predict *num8* weight-str ) ) ) )
    ( setf wrong ( + wrong ( * *wrong-scale* ( predict *num9* weight-str ) ) ) )
    ( + right ( - wrong ) )
)

( defmethod fitness-4 ( weight-str &aux right wrong )
	( setf right ( * *right-scale* ( predict *num4* weight-str ) ) ) 
    ( setf wrong ( * *wrong-scale* ( predict *num1* weight-str ) ) )
    ( setf wrong ( + wrong ( * *wrong-scale* ( predict *num2* weight-str ) ) ) )
    ( setf wrong ( + wrong ( * *wrong-scale* ( predict *num3* weight-str ) ) ) )
    ( setf wrong ( + wrong ( * *wrong-scale* ( predict *num0* weight-str ) ) ) )
    ( setf wrong ( + wrong ( * *wrong-scale* ( predict *num5* weight-str ) ) ) )
    ( setf wrong ( + wrong ( * *wrong-scale* ( predict *num6* weight-str ) ) ) )
    ( setf wrong ( + wrong ( * *wrong-scale* ( predict *num7* weight-str ) ) ) )
    ( setf wrong ( + wrong ( * *wrong-scale* ( predict *num8* weight-str ) ) ) )
    ( setf wrong ( + wrong ( * *wrong-scale* ( predict *num9* weight-str ) ) ) )
    ( + right ( - wrong ) )
)

( defmethod fitness-5 ( weight-str &aux right wrong )
	( setf right ( * *right-scale* ( predict *num5* weight-str ) ) ) 
    ( setf wrong ( * *wrong-scale* ( predict *num1* weight-str ) ) )
    ( setf wrong ( + wrong ( * *wrong-scale* ( predict *num2* weight-str ) ) ) )
    ( setf wrong ( + wrong ( * *wrong-scale* ( predict *num3* weight-str ) ) ) )
    ( setf wrong ( + wrong ( * *wrong-scale* ( predict *num4* weight-str ) ) ) )
    ( setf wrong ( + wrong ( * *wrong-scale* ( predict *num0* weight-str ) ) ) )
    ( setf wrong ( + wrong ( * *wrong-scale* ( predict *num6* weight-str ) ) ) )
    ( setf wrong ( + wrong ( * *wrong-scale* ( predict *num7* weight-str ) ) ) )
    ( setf wrong ( + wrong ( * *wrong-scale* ( predict *num8* weight-str ) ) ) )
    ( setf wrong ( + wrong ( * *wrong-scale* ( predict *num9* weight-str ) ) ) )
    ( + right ( - wrong ) )
)

( defmethod fitness-6 ( weight-str &aux right wrong )
	( setf right ( * *right-scale* ( predict *num6* weight-str ) ) ) 
    ( setf wrong ( * *wrong-scale* ( predict *num1* weight-str ) ) )
    ( setf wrong ( + wrong ( * *wrong-scale* ( predict *num2* weight-str ) ) ) )
    ( setf wrong ( + wrong ( * *wrong-scale* ( predict *num3* weight-str ) ) ) )
    ( setf wrong ( + wrong ( * *wrong-scale* ( predict *num4* weight-str ) ) ) )
    ( setf wrong ( + wrong ( * *wrong-scale* ( predict *num5* weight-str ) ) ) )
    ( setf wrong ( + wrong ( * *wrong-scale* ( predict *num0* weight-str ) ) ) )
    ( setf wrong ( + wrong ( * *wrong-scale* ( predict *num7* weight-str ) ) ) )
    ( setf wrong ( + wrong ( * *wrong-scale* ( predict *num8* weight-str ) ) ) )
    ( setf wrong ( + wrong ( * *wrong-scale* ( predict *num9* weight-str ) ) ) )
    ( + right ( - wrong ) )
)

( defmethod fitness-7 ( weight-str &aux right wrong )
	( setf right ( * *right-scale* ( predict *num7* weight-str ) ) ) 
    ( setf wrong ( * *wrong-scale* ( predict *num1* weight-str ) ) )
    ( setf wrong ( + wrong ( * *wrong-scale* ( predict *num2* weight-str ) ) ) )
    ( setf wrong ( + wrong ( * *wrong-scale* ( predict *num3* weight-str ) ) ) )
    ( setf wrong ( + wrong ( * *wrong-scale* ( predict *num4* weight-str ) ) ) )
    ( setf wrong ( + wrong ( * *wrong-scale* ( predict *num5* weight-str ) ) ) )
    ( setf wrong ( + wrong ( * *wrong-scale* ( predict *num6* weight-str ) ) ) )
    ( setf wrong ( + wrong ( * *wrong-scale* ( predict *num0* weight-str ) ) ) )
    ( setf wrong ( + wrong ( * *wrong-scale* ( predict *num8* weight-str ) ) ) )
    ( setf wrong ( + wrong ( * *wrong-scale* ( predict *num9* weight-str ) ) ) )
    ( + right ( - wrong ) )
)

( defmethod fitness-8 ( weight-str &aux right wrong )
	( setf right ( * *right-scale* ( predict *num8* weight-str ) ) ) 
    ( setf wrong ( * *wrong-scale* ( predict *num1* weight-str ) ) )
    ( setf wrong ( + wrong ( * *wrong-scale* ( predict *num2* weight-str ) ) ) )
    ( setf wrong ( + wrong ( * *wrong-scale* ( predict *num3* weight-str ) ) ) )
    ( setf wrong ( + wrong ( * *wrong-scale* ( predict *num4* weight-str ) ) ) )
    ( setf wrong ( + wrong ( * *wrong-scale* ( predict *num5* weight-str ) ) ) )
    ( setf wrong ( + wrong ( * *wrong-scale* ( predict *num6* weight-str ) ) ) )
    ( setf wrong ( + wrong ( * *wrong-scale* ( predict *num7* weight-str ) ) ) )
    ( setf wrong ( + wrong ( * *wrong-scale* ( predict *num0* weight-str ) ) ) )
    ( setf wrong ( + wrong ( * *wrong-scale* ( predict *num9* weight-str ) ) ) )
    ( + right ( - wrong ) )
)

( defmethod fitness-9 ( weight-str &aux right wrong )
	( setf right ( * *right-scale* ( predict *num9* weight-str ) ) ) 
    ( setf wrong ( * *wrong-scale* ( predict *num1* weight-str ) ) )
    ( setf wrong ( + wrong ( * *wrong-scale* ( predict *num2* weight-str ) ) ) )
    ( setf wrong ( + wrong ( * *wrong-scale* ( predict *num3* weight-str ) ) ) )
    ( setf wrong ( + wrong ( * *wrong-scale* ( predict *num4* weight-str ) ) ) )
    ( setf wrong ( + wrong ( * *wrong-scale* ( predict *num5* weight-str ) ) ) )
    ( setf wrong ( + wrong ( * *wrong-scale* ( predict *num6* weight-str ) ) ) )
    ( setf wrong ( + wrong ( * *wrong-scale* ( predict *num7* weight-str ) ) ) )
    ( setf wrong ( + wrong ( * *wrong-scale* ( predict *num8* weight-str ) ) ) )
    ( setf wrong ( + wrong ( * *wrong-scale* ( predict *num0* weight-str ) ) ) )
    ( + right ( - wrong ) )
)

( defmethod fitness-demo (&aux x fitness)
	( setf x (weight-string) )
	( format t "x = ~A~%" x )
	( format t "Directly applying the fitness metrics ...~%" )
	( format t "fitness-0 = ~A~%" ( fitness-0 x ) )
	( format t "fitness-1 = ~A~%" ( fitness-1 x ) )
	( format t "fitness-2 = ~A~%" ( fitness-2 x ) )
	( format t "fitness-3 = ~A~%" ( fitness-3 x ) )
	( format t "fitness-4 = ~A~%" ( fitness-4 x ) )
	( format t "fitness-5 = ~A~%" ( fitness-5 x ) )
	( format t "fitness-6 = ~A~%" ( fitness-6 x ) )
	( format t "fitness-7 = ~A~%" ( fitness-7 x ) )
	( format t "fitness-8 = ~A~%" ( fitness-8 x ) )
	( format t "fitness-9 = ~A~%" ( fitness-9 x ) )
	( format t "Indirectly applying the fitness metrics ...~%" )
	( setf fitness #'fitness-0 )
	( format t "fitness-0 = ~A~%" ( funcall fitness x ) )
	( setf fitness #'fitness-1 )
	( format t "fitness-1 = ~A~%" ( funcall fitness x ) )
	( setf fitness #'fitness-2 )
	( format t "fitness-2 = ~A~%" ( funcall fitness x ) )
	( setf fitness #'fitness-3 )
	( format t "fitness-3 = ~A~%" ( funcall fitness x ) )
	( setf fitness #'fitness-4 )
	( format t "fitness-4 = ~A~%" ( funcall fitness x ) )
	( setf fitness #'fitness-5 )
	( format t "fitness-5 = ~A~%" ( funcall fitness x ) )
	( setf fitness #'fitness-6 )
	( format t "fitness-6 = ~A~%" ( funcall fitness x ) )
	( setf fitness #'fitness-7 )
	( format t "fitness-7 = ~A~%" ( funcall fitness x ) )
	( setf fitness #'fitness-8 )
	( format t "fitness-8 = ~A~%" ( funcall fitness x ) )
	( setf fitness #'fitness-9 )
	( format t "fitness-9 = ~A~%" ( funcall fitness x ) )
)

;----------------------------------------------------------------------
; Task 6: The Individual Class
;----------------------------------------------------------------------

( defclass individual ()
	(
		( weight-string :accessor individual-weight-string :initarg :weight-string )
		( fitness :accessor individual-fitness :initarg :fitness )
		( number :accessor individual-number :initarg :number )
	)
)

( defmethod random-individual ( &aux weights )
    ( setf weights ( weight-string ) )
	( make-instance 'individual
		:weight-string weights
		:fitness ( funcall *fitness* weights )
		:number 0
	)
)

( defmethod new-individual ( ( num number ) ( weights list ) )
	( make-instance 'individual
		:weight-string weights
		:fitness ( funcall *fitness* weights )
		:number num
	)
)

( defmethod display ( ( i individual ) )
	( display-nnl i ) ( terpri )
)

( defmethod display-nnl ( ( i individual ) )
	( prin1 ( individual-number i ) )
	( princ ( filler ( individual-number i ) ) )
	( prin1 ( individual-weight-string i ) )
	( princ " " )
	( prin1 ( individual-fitness i ) )
	( princ ( filler ( individual-fitness i ) ) )
)

( defmethod filler ( ( n number ) )
	( cond
		( ( < n 10 ) " " )
		( ( < n 100 ) " " )
		( ( < n 1000 ) " " )
		( ( < n 10000 ) " " )
		( ( < n 100000 ) " " )
	)
)

( defmethod fitness-0 ( ( i individual ) )
	( fitness-0 ( individual-weight-string i ) )
)

( defmethod fitness-1 ( ( i individual ) )
	( fitness-1 ( individual-weight-string i ) )
)

( defmethod fitness-2 ( ( i individual ) )
	( fitness-2 ( individual-weight-string i ) )
)

( defmethod fitness-3 ( ( i individual ) )
	( fitness-3 ( individual-weight-string i ) )
)

( defmethod fitness-4 ( ( i individual ) )
	( fitness-4 ( individual-weight-string i ) )
)

( defmethod fitness-5 ( ( i individual ) )
	( fitness-5 ( individual-weight-string i ) )
)

( defmethod fitness-6 ( ( i individual ) )
	( fitness-6 ( individual-weight-string i ) )
)

( defmethod fitness-7 ( ( i individual ) )
	( fitness-7 ( individual-weight-string i ) )
)

( defmethod fitness-8 ( ( i individual ) )
	( fitness-8 ( individual-weight-string i ) )
)

( defmethod fitness-9 ( ( i individual ) )
	( fitness-9 ( individual-weight-string i ) )
)

( defmethod individual-demo (&aux i0 i1 i2 i3 one two three)
	( setf *fitness* #'fitness-0 )
	( setf i0 ( random-individual ) )
	( display i0 )
	( setf one ( weight-string ) )
	( setf i1 ( new-individual 1 one ) )
	( display i1 )
	( setf two ( weight-string ) )
	( setf i2 ( new-individual 2 two ) )
	( display i2 )
	( setf three ( weight-string ) )
	( setf i3 ( new-individual 3 three ) )
	( display i3 )
	( format t "Fitness of i0 = ~A~%" ( funcall *fitness* i0 ) )
	( format t "Fitness of i1 = ~A~%" ( funcall *fitness* i1 ) )
	( format t "Fitness of i2 = ~A~%" ( funcall *fitness* i2 ) )
	( format t "Fitness of i3 = ~A~%" ( funcall *fitness* i3 ) )
	nil
)

( defconstant *population-size* 100 )
( defconstant *selection-size* 16 )
( setf *fitness* #'fitness-1 )

;----------------------------------------------------------------------
; Task 7: The Population Class
;----------------------------------------------------------------------

( defclass population ()
	(
		( individuals :accessor population-individuals :initarg :individuals )
		( generation :accessor population-generation :initform 0 )
	)
)

( defmethod size ( ( p population ) )
	( length ( population-individuals p ) )
)

( defmethod display ( ( p population ) )
	( terpri ) ( terpri )
	( princ "Generation " )
	( prin1 ( population-generation p ) )
	( princ " population ..." )
	( terpri ) ( terpri )
	( dolist ( i ( population-individuals p ) )
		( display i )
	)
	( terpri )
)

( defmethod initial-population ( &aux individuals )
	( setf individuals () )
	( dotimes ( i *population-size* )
		( push ( new-individual ( + i 1 ) ( weight-string ) ) individuals )
	)
	( make-instance 'population :individuals ( reverse individuals ) )
)

( defmethod average ( ( p population ) &aux ( sum 0 ) )
    ( setf sum ( apply '+ ( mapcar 'individual-fitness ( population-individuals p ) ) ) )
    ( float ( / sum ( length ( population-individuals p ) ) ) )
)

( setf *select-demo* nil )

( defmethod select-individual ( ( p population ) &aux i candidates rn )
	( setf candidates ( select-individuals p ) )
	( setf mfi ( most-fit-individual candidates ) )
	( if *select-demo* ( select-demo-helper candidates mfi) )
	mfi
)

( defmethod select-individuals ( ( p population ) &aux individuals candidates rn )
	( setf individuals ( population-individuals p ) )
	( setf candidates () )
	( dotimes ( i *selection-size* )
		( setf rn ( random *population-size* ) )
		( push ( nth rn individuals ) candidates )
	)
	candidates
)

( defmethod most-fit-individual ( ( l list ) &aux max-value max-individual )
    ( setf max-value ( apply 'max ( mapcar 'individual-fitness l ) ) )
    ( setf max-individual ( position max-value ( mapcar 'individual-fitness l ) ) )
    ( nth max-individual l )
)

( defmethod select-demo-helper ( ( l list ) ( i individual ) )
	( princ "the sample of individuals ..." ) ( terpri )
	( mapcar #'display l )
	( terpri )
	( princ "the most fit of the sample ... " ) ( terpri )
	( display i )
	( terpri )
	nil
)

( defmethod population-demo (&aux p)
	( setf p ( initial-population ) )
	( display p )
	( format t "Average fitness = ~A~%~%" ( average p ) )
	( setf *select-demo* t )
	( format t "Sampling ...~%~%" )
	( select-individual p ) ( terpri)
	( format t "Sampling ...~%~%" )
	( select-individual p ) ( terpri)
	( format t "Sampling ...~%~%" )
	( select-individual p ) ( terpri)
)

;----------------------------------------------------------------------
; Task 8: Incorporating Mutation
;----------------------------------------------------------------------

( defmethod mutate ( ( i individual ) &aux mutation )
    ( setf mutation ( mutation ( individual-weight-string i ) ) )
    ( make-instance 'individual
        :number ( individual-number i )
        :weight-string mutation
        :fitness ( funcall *fitness* mutation )
    )
)

( defconstant *pc-m* 15 ) ; Percent to mutate

( defmethod maybe-mutate ( ( i individual ) )
    ( if ( <= ( + 1 ( random 100 ) ) *pc-m* )
        ( mutate i )
        i
    )
)

( defmethod mutate-demo ()
    ( setf i ( random-individual ) )
    ( display i )
    ( dotimes ( x 20 )
        ( setf i ( mutate i ) )
        ( display i )
    )
)

( defmethod maybe-mutate-demo ()
    ( setf i ( random-individual ) )
    ( display i )
    ( dotimes ( x 20 )
        ( setf n ( maybe-mutate i ) )
        ( display-nnl n )
        ( if ( not ( equal n i ) ) ( princ " *" ) )
        ( terpri )
        ( setf i n )
    )
)

;----------------------------------------------------------------------
; Task 9: Copy
;----------------------------------------------------------------------

( setf *copy-demo* nil )
( defconstant *pc-c* 60 ) ; Percent to copy
    
( defmethod perform-copies ( ( cp population ) ( np population))
    ( dotimes ( i ( nr-copies ) )
        ( perform-one-copy cp np )
    )
)

( defmethod nr-copies ()
    ( * ( / *pc-c* 100 ) *population-size* )
)

( defmethod perform-one-copy ( ( cp population ) ( np population )
    &aux x m mm new-i )
    ( setf m ( select-individual cp ) )
    ( if *copy-demo* ( format t "Selected individual = ~%" ) )
    ( if *copy-demo* ( display m ) )
    ( setf mm ( maybe-mutate m ) )
    ( if *copy-demo* ( format t "Possibly muted individual = ~&" ) )
    ( if *copy-demo* ( display mm ) )
    ( setf ( individual-number mm ) ( + 1 ( size np ) ) )
    ( if *copy-demo* ( format t "Renumbered individual = ~&" ) )
    ( if *copy-demo* ( display mm ) )
    ( setf new-i ( new-individual ( + 1 ( size np ) ) ( individual-weight-string mm ) ) )
    ( setf
        ( population-individuals np )
        ( append ( population-individuals np ) ( list new-i ) )
    )
    nil
)

( defmethod empty-population ( ( cp population ) &aux np )
    ( setf np ( make-instance 'population ) )
    ( setf ( population-individuals np ) () )
    ( setf ( population-generation np ) ( + 1 ( population-generation cp ) ) )
    np
)

( defmethod perform-copies-demo ( &aux cp np )
    ( setf cp ( initial-population ) )
    ( setf np ( empty-population cp ) )
    ( format t "-------------------------------------------------------------------------~%~%~%" )
    ( display np )
    ( format t "~%~%-------------------------------------------------------------------------~%" )
    ( setf *select-demo* t )
    ( setf *copy-demo* t )
    ( dotimes ( i 10 )
        ( perform-one-copy cp np )
        ( format t "-------------------------------------------------------------------------~%~%~%" )
        ( display np )
        ( format t "~%~%-------------------------------------------------------------------------~%" )
    )
    ( setf *select-demo* nil )
    ( setf *copy-demo* nil )
    nil
)

;----------------------------------------------------------------------
; Task 10: Crossover
;----------------------------------------------------------------------

( setf *crossover-demo* nil )
    ( defconstant *pc-x* 60 )
    ( defmethod perform-crossovers ( ( cp population ) ( np population ) )
    ( dotimes ( i ( nr-crossovers ) )
        ( perform-one-crossover cp np )
    )
)

( defmethod nr-crossovers ()
    ( * ( / *pc-x* 100 ) *population-size* )
)

( defmethod perform-one-crossover ( ( cp population ) ( np population ) )
    ( let ( x m mm mother father new-i )
        ( setf mother ( select-individual cp ) )
        ( setf father ( select-individual cp ) )
        ( if *crossover-demo* ( format t "Selected mother = ~%" ) )
        ( if *crossover-demo* ( display mother ) )
        ( if *crossover-demo* ( format t "Selected father = ~&" ) )
        ( if *crossover-demo* ( display father ) )
        ( setf m ( crossover mother father ) )
        ( if *crossover-demo* ( format t "the crossover = ~&" ) )
        ( if *crossover-demo* ( display m ) )
        ( setf mm ( maybe-mutate m ) )
        ( if *crossover-demo* ( format t "the possiblly mutated individual = ~&" ) )
        ( if *crossover-demo* ( display mm ) )
        ( setf ( individual-number mm ) ( + 1 ( size np ) ) )
        ( if *crossover-demo* ( format t "the renumbered individual = ~&" ) )
        ( if *crossover-demo* ( display mm ) )
        ( setf new-i ( new-individual ( + 1 ( size np ) ) ( individual-weight-string mm ) ) )
        ( setf
            ( population-individuals np )
            ( append ( population-individuals np ) ( list new-i ) )
        )
    )
    nil
)

( defmethod crossover ( ( mother individual ) ( father individual ) &aux mi fi x i )
    ( setf mi (individual-weight-string mother ) )
    ( setf fi (individual-weight-string father ) )
    ( setf x ( crossover mi fi ) )
    ( setf i ( new-individual 0 x ) )
    i
)

( defmethod perform-crossovers-demo ( &aux cp np )
    ( setf cp ( initial-population ) )
    ( setf np ( empty-population cp ) )
    ( format t "-------------------------------------------------------------------------~%~%~%" )
    ( display np )
    ( format t "~%~%-------------------------------------------------------------------------~%" )
    ( setf *select-demo* t )
    ( setf *crossover-demo* t )
    ( dotimes ( i 10 )
        ( perform-one-crossover cp np )
        ( format t "-------------------------------------------------------------------------~%~%~%" )
        ( display np )
        ( format t "~%~%-------------------------------------------------------------------------~%" )
    )
    ( setf *select-demo* nil )
    ( setf *crossover-demo* nil )
    nil
)

;----------------------------------------------------------------------
; Task 11: The GA
;----------------------------------------------------------------------

( defmethod next-generation ( ( cp population ) &aux np )
    ( setf np ( empty-population cp ) )
    ( perform-copies cp np )
    ( perform-crossovers cp np )
    np
)

( defconstant *nr-generations* 25 )

( defmethod ga ( &aux p )
    ( format t "THE INPUT NUMBER IS 0 ~%~%" )
    ( setf *fitness* #'fitness-0 )
    ( setf p ( initial-population ) )
    ( terpri )
    ( summarize p )
    ( dotimes ( i *nr-generations* )
        ( setf p ( next-generation p ) )
        ( check-average p )
    )
    ( terpri )
    ( summarize p )
    ( format t "THE INPUT NUMBER IS 1 ~%~%" )
    ( setf *fitness* #'fitness-1 )
    ( dotimes ( i *nr-generations* )
        ( setf p ( next-generation p ) )
        ( check-average p )
    )
    ( terpri )
    ( summarize p )
    ( format t "THE INPUT NUMBER IS 2 ~%~%" )
    ( setf *fitness* #'fitness-2 )
    ( dotimes ( i *nr-generations* )
        ( setf p ( next-generation p ) )
        ( check-average p )
    )
    ( terpri )
    ( summarize p )
    ( format t "THE INPUT NUMBER IS 3 ~%~%" )
    ( setf *fitness* #'fitness-3 )
    ( dotimes ( i *nr-generations* )
        ( setf p ( next-generation p ) )
        ( check-average p )
    )
    ( terpri )
    ( summarize p )
    ( format t "THE INPUT NUMBER IS 4 ~%~%" )
    ( setf *fitness* #'fitness-4 )
    ( dotimes ( i *nr-generations* )
        ( setf p ( next-generation p ) )
        ( check-average p )
    )
    ( terpri )
    ( summarize p )
    ( format t "THE INPUT NUMBER IS 5 ~%~%" )
    ( setf *fitness* #'fitness-5 )
    ( dotimes ( i *nr-generations* )
        ( setf p ( next-generation p ) )
        ( check-average p )
    )
    ( terpri )
    ( summarize p )
    ( format t "THE INPUT NUMBER IS 6 ~%~%" )
    ( setf *fitness* #'fitness-6 )
    ( dotimes ( i *nr-generations* )
        ( setf p ( next-generation p ) )
        ( check-average p )
    )
    ( terpri )
    ( summarize p )
    ( format t "THE INPUT NUMBER IS 7 ~%~%" )
    ( setf *fitness* #'fitness-7 )
    ( dotimes ( i *nr-generations* )
        ( setf p ( next-generation p ) )
        ( check-average p )
    )
    ( terpri )
    ( summarize p )
    ( format t "THE INPUT NUMBER IS 8 ~%~%" )
    ( setf *fitness* #'fitness-8 )
    ( dotimes ( i *nr-generations* )
        ( setf p ( next-generation p ) )
        ( check-average p )
    )
    ( terpri )
    ( summarize p )
    ( format t "THE INPUT NUMBER IS 9 ~%~%" )
    ( setf *fitness* #'fitness-9 )
    ( dotimes ( i *nr-generations* )
        ( setf p ( next-generation p ) )
        ( check-average p )
    )
    ( terpri )
    ( summarize p )
)

( defmethod summarize ( ( p population ) )
    ( display p )
    ( check-average p )
    ( terpri )
)

( defmethod check-average ( ( p population ) )
    ( format t "average fitness of populatioon ~A = ~A~%"
        ( population-generation p )
        ( average p )
    )
)