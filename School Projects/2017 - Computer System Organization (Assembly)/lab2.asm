################################################################
#
#	Vitaliy Shydlonok
#	CSC 211-002
#	9/14/2017
#	vs_hw 3
#
#	Prompts the user to input two integers, then 
#	prints the sum of the two inputs.
#
#	Begin
#		Print first prompt
#		Get first integer input
#		Print second prompt
#		Get second integer input
#		Sum the two inputs
#		Print first input
#		Print plus sign
#		Print second input
#		Print equals sign
#		Print sum of both inputs
#		CRLF
#	End
#
################################################################

	.text
	.globl main
main:
	#print_string using fist prompt
	la $a0, prompt1	
	li $v0, 4 		
	syscall
	
	#read_int into $v0
	li $v0, 5		
	syscall	
	
	#move first input to $t0
	move $t0, $v0
	
	#print_string using second prompt
	la $a0, prompt2	
	li $v0, 4 		
	syscall
	
	#read_int into $v0
	li $v0, 5		
	syscall	
	
	#move second input to $t1
	move $t1, $v0
	
	#print_int using $t0 (first input)
	move $a0, $t0
	li $v0, 1	
	syscall	
	
	#print_string using plus sign string
	la $a0, plus	
	li $v0, 4 		
	syscall
	
	#print_int using $t1 (second input)
	move $a0, $t1
	li $v0, 1	
	syscall	
	
	#print_string using equals sign string
	la $a0, equals	
	li $v0, 4 		
	syscall
	
	#calculate the sum of $t0 and $t1 (both inputs) into $t2
	add $t2, $t0, $t1
	
	#print_int using $t2 (sum of both inputs)
	move $a0, $t2
	li $v0, 1	
	syscall	
	
	#print_string using endl (CRLF)
	la $a0, endl	
	li $v0, 4		
	syscall	
	
	#exit
	li $v0, 10
	syscall	
	
	.data
	#null terminated strings
prompt1: .asciiz "Enter the first integer: "
prompt2: .asciiz "Enter the second integer: "
plus: .asciiz " + "
equals: .asciiz " = "
endl: .asciiz "\n"

################################################################
#Program Output:
################################################################
#
#Enter the first integer: 4
#Enter the second integer: 6
#4 + 6 = 10
#