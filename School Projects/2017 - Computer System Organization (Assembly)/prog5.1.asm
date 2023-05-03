################################################################
#
#	Vitaliy Shydlonok
#	CSC 211-002
#	9/28/2017
#	vs_program 5
#
#	Prompts the user to input 10 integer values,
#	stores them in an array, displays the array,
#	and then calculates and displays the average
#	of the inputs.
#
################################################################
	
	.text
	.globl main

main:
	#Load address of the array into $t0
	la $t0, array
	
	#Initialize array size $t1 to 10
	li $t1, 10
	
	#Initialize counter $t3 to 0
	li $t3, 0
	
	#Initialize total $t4 to 0
	li $t4, 0
	
while:
	#Prompt for an integer
	la $a0, prompt
	li $v0, 4
	syscall
	
	#Get integer input $v0 and store it in ($t0)
	li $v0, 5
	syscall
	sw $v0, ($t0)
	
	#Increment total $t4 by input $v0
	add $t4, $t4, $v0
	
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
	
	#Initialize array index $t0 to 0
	li $t0, 0
	
	#Initialize counter $t3 to 0
	li $t3, 0
	
	#Load address of the array into $t0
	la $t0, array
	
print:
	#Load word value at array address ($t0) into $t2
	lw $t2, ($t0)
	
	#Print value in $t2
	move $a0, $t2
	li $v0, 1
	syscall
	
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
	
	#Print "Average: "
	la $a0, msg2
	li $v0, 4
	syscall
	
	#Calculate total $t4 divided by array size $t1
	div $t4, $t1
	
	#Move from low to $t5
	mflo $t5
	
	#Print average $t5
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
prompt: .asciiz "Enter a number: "
comma: .asciiz ", "
msg1: .asciiz "The numbers you entered were: "
msg2: .asciiz "Average: "
endl: .asciiz "\n"

.align 2
array: .space 40

################################################################
#Program Output:
################################################################
#
#Enter a number: 1
#Enter a number: 2
#Enter a number: 3
#Enter a number: 4
#Enter a number: 5
#Enter a number: 6
#Enter a number: 7
#Enter a number: 8
#Enter a number: 9
#Enter a number: 10
#The numbers you entered were: 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 
#Average: 5
#
################################################################
