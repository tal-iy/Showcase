################################################################
#
#	Vitaliy Shydlonok
#	CSC 211-002
#	10/3/2017
#	vs_program 6
#
#	Prompts the user for how many numbers to enter,
#	prompt for that many integer values,
#	stores them in an array, displays the array,
#	and then find and displays the smallest and largest
#	of the inputs.
#
################################################################
	
	.text
	.globl main

main:
	#Prompt for an integer
	la $a0, prompt1
	li $v0, 4
	syscall
	
	#Get integer input $v0 and move it to $t1
	li $v0, 5
	syscall
	move $t1, $v0

	#Load array address into $t0
	la $t0, array
	
	#Initialize counter $t3 to 0
	li $t3, 0
	
while:
	#Prompt for an integer
	la $a0, prompt2
	li $v0, 4
	syscall
	
	#Get integer input $v0 and store it in ($t0)
	li $v0, 5
	syscall
	sw $v0, ($t0)
	
	#Increment array address $t0 by 4 bytes
	add $t0, $t0, 4
	
	#Increment counter $t3 by 1
	add $t3, $t3, 1
	
	#If counter $t3 is less than $t1, branch to start of While loop
	blt $t3, $t1, while
	
endWhile:
	#Print "The numbers you entered were: "
	la $a0, msg1
	li $v0, 4
	syscall 
	
	#Reset array address $t0 to 0
	li $t0, 0
	
	#Load array address into $t0
	la $t0, array
	
	#Initialize counter $t3 to 0
	li $t3, 0
	
	#Initialize smallest $t4 and largest $t5
	lw $t4, ($t0)
	lw $t5, ($t0)
	
print:
	#Load word value at array address array($t0) into $t2
	lw $t2, ($t0)
	
	#Print value in $t2
	move $a0, $t2
	li $v0, 1
	syscall
	
	#If current number $t2 is greater than or equal to smallest $t4, branch to checkedSmallest
	bge $t2, $t4, checkedSmallest
	
	#Copy current number $t2 to smallest $t4
	move $t4, $t2
	
checkedSmallest:
	#If current number $t2 is less than or equal to largest $t5, branch to checkedLargest
	ble $t2, $t5, checkedLargest
	
	#Copy current number $t2 to largest $t5
	move $t5, $t2

checkedLargest:
	#Print ", "
	la $a0, comma
	li $v0, 4
	syscall
	
	#Increment array address $t0 by 4 bytes
	add $t0, $t0, 4
	
	#Increment counter $t3 by 1
	add $t3, $t3, 1
	
	#If counter $t3 is less than $t1, branch to start of Print loop 
	blt $t3, $t1, print
	
endPrint:
	#Print crlf
	la $a0, endl
	li $v0, 4
	syscall
	
	#Print "Smallest: "
	la $a0, msg2
	li $v0, 4
	syscall
	
	#Print smallest $t4
	move $a0, $t4
	li $v0, 1
	syscall
	
	#Print crlf
	la $a0, endl
	li $v0, 4
	syscall
	
	#Print "Largest: "
	la $a0, msg3
	li $v0, 4
	syscall
	
	#Print largest $t5
	move $a0, $t5
	li $v0, 1
	syscall
	
	#Print crlf
	la $a0, endl
	li $v0, 4
	syscall

	#system return
	li $v0, 10
	syscall
	
	.data
prompt1: .asciiz "How many number will you enter? "
prompt2: .asciiz "Enter a number: "
comma: .asciiz ", "
msg1: .asciiz "The numbers you entered were: "
msg2: .asciiz "Smallest: "
msg3: .asciiz "Largest: "
endl: .asciiz "\n"

.align 2
array: .space 100

################################################################
#Program Output:
################################################################
#
#How many number will you enter? 10
#Enter a number: 4
#Enter a number: 5
#Enter a number: 6
#Enter a number: 1
#Enter a number: 2
#Enter a number: 7
#Enter a number: 8
#Enter a number: 9
#Enter a number: 3
#Enter a number: 5
#The numbers you entered were: 4, 5, 6, 1, 2, 7, 8, 9, 3, 5, 
#Smallest: 1
#Largest: 9
#
################################################################
