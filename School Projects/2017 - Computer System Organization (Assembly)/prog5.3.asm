################################################################
#
#	Vitaliy Shydlonok
#	CSC 211-002
#	10/16/2017
#	vs_program 5.3
#
#	minMax.asm using indexed addressing
#
#	Prompts the user for 10 integer values,
#	stores them in an array, and then finds and 
#	displays the smallest and largest of the 
#	inputs.
#
################################################################

	.text
    .globl main
main:
	# Initialize array index $t0 to 0
    li $t0, 0
	
	# Initialize min $t2 to highest possible integer 2,147,483,647
	li $t2, 2147483647
	
	# Initialize max $t3 to lowest possible integer -2,147,483,648
	li $t3, -2147483648
	 
inputLoop:  
	# If index $t0 >= size, end loop
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
	
	# Jump back to loop start
	j inputLoop
	
endInputLoop:

	# Set array index $t0 back to 0
    li $t0, 0

minMaxLoop:
	# If index $t0 >= size, end loop
	lw $t1, size
	bge $t0, $t1, endMinMaxLoop
	
	# Load array[index $t0] into current integer $t4
	lw $t4, array($t0)
	
	# If current int $t4 >= min $t2, branch to notMin
	bge $t4, $t2, notMin
	
	# Copy current int $t4 to min $t2
	move $t2, $t4
	
	# Jump to notMax
	j notMax
	
notMin:
	# If current int $t4 <= max $t3, branch to notMax;
	ble $t4,$t3,notMax
	
	# Copy current int $t4 to max $t3
	move $t3,$t4

notMax:
	# Increment array index $t0 by sizeof(int): 4
	add $t0, $t0, 4 

	# Jump back to loop start
	j minMaxLoop

endMinMaxLoop:
	# Display "The minimum number is "
	la $a0,p1           
	li $v0,4
	syscall
	
	# Display the minimum number 
	move $a0,$t2        
	li $v0,1
	syscall
	
	# Display "The maximum number is "
	la $a0,p2           
	li $v0,4           
	syscall      

	# Display the maximum number
	move $a0,$t3         
	li $v0,1
	syscall
	
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
p1:     .asciiz "The minimum number is "
p2:     .asciiz "\nThe maximum number is "
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
#Enter a number: 9
#Enter a number: 10
#Enter a number: 8
#Enter a number: 7
#Enter a number: 6
#The minimum number is 1
#The maximum number is 10
#
################################################################
