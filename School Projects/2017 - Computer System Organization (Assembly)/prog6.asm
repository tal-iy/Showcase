################################################################
#
#	Vitaliy Shydlonok
#	CSC 211-002
#	10/26/2017
#	vs_program 6
#
#	minMax.asm with bubble sort
#
#	Prompts the user for 10 integer values,
#	stores them in an array, and then sorts and 
#	displays the sorted array.
#
################################################################

	.text
    .globl main
main:
	# Initialize array index $t0 to 0
    li $t0, 0
	 
inputLoop:  
	# If index $t0 >= size, branch to endInputLoop
	lw $t1, size
	bge $t0, $t1, endInputLoop
	
	# Prompt for an integer
	la $a0, prompt
	li $v0, 4
	syscall
	
	# Get integer input and store it in array[index $t0]
	li $v0, 5
	syscall
	sw $v0, array($t0)
	
	# Increment array index $t0 by sizeof(int): 4
	add $t0, $t0, 4
	
	# Jump back to inputLoop
	j inputLoop
	
endInputLoop:

	# Set array index $t0 back to 0
    li $t0, 0
	
sortLoop:
	# If array index $t0 >= (size-1), branch to endSortLoop
	lw $t1, size
	sub $t1, $t1, 4
	bge $t0, $t1, endSortLoop
	
	# Initialize compareLoop index $t2 to sizeof(int): 4
	li $t2, 4

compareLoop:

	# If compareLoop index $t2 >= size, branch to endCompareLoop
	lw $t1, size
	bge $t2, $t1, endCompareLoop

	# If array[index $t2] >= array[index $t2 - 1], branch to isGreater
	move $t1, $t2
	sub $t1, $t1, 4
	lw $t3, array($t2)
	lw $t4, array($t1)
	bge $t3, $t4, isGreater
	
	# Swap values of array[index $t2] and array[index $t2 - 1]
	sw $t3, array($t1)
	sw $t4, array($t2)

isGreater:

	# Increment compareLoop index $t2 by sizeof(int): 4
	add $t2, $t2, 4
	
	# Jump back to compareLoop
	j compareLoop

endCompareLoop:

	# Increment array index $t0 by sizeof(int): 4
	add $t0, $t0, 4
	
	# Jump back to sortLoop
	j sortLoop

endSortLoop:

	# Display "Sorted inputs: "
	la $a0,msg         
	li $v0,4            
	syscall

	# Set array index $t0 back to 0
    li $t0, 0

printLoop:

	# If index $t0 >= size, branch to endPrintLoop
	lw $t1, size
	bge $t0, $t1, endPrintLoop
	
	# Display array[index $t0]
	lw $a0, array($t0)
	li $v0, 1
	syscall
	
	# Display a space
	la $a0,space         
	li $v0,4            
	syscall

	# Increment array index $t0 by sizeof(int): 4
	add $t0, $t0, 4
	
	# Jump back to printLoop
	j printLoop

endPrintLoop:

	# Display "cr/lf"
	la $a0,crlf         
	li $v0,4            
	syscall
	
	# End Of Program
	li $v0,10           
	syscall 
        
	.data
array: .space 40 # array[10*sizeof(int)]
size: .word 40 # int size = 40

prompt: .asciiz "Enter a number: "
msg:	.asciiz "\nSorted inputs: "
space:  .asciiz " "
crlf:   .asciiz "\n" 

################################################################
# Program Output:
################################################################
#
#Enter a number: 5
#Enter a number: 4
#Enter a number: 3
#Enter a number: 2
#Enter a number: 1
#Enter a number: 10
#Enter a number: 9
#Enter a number: 8
#Enter a number: 7
#Enter a number: 6
#
#Sorted inputs: 1 2 3 4 5 6 7 8 9 10 
#
################################################################
