################################################################
#
#	Vitaliy Shydlonok
#	CSC 211-002
#	9/14/2017
#	vs_program 2
#
#	Prompts the user to input three integers,
#	then prints the largest of them on the
#	screen.
#
#	Begin
#		Print first prompt
#		Get first integer input
#		Print second prompt
#		Get second integer input
#		Print third prompt
#		Get third integer input
#		Print message
#		Determine largest input value
#		Print largest input value
#		CRLF
#	End
#
################################################################

	.text
	.globl main
main:
	#print_string using prompt1
	la $a0, prompt1
	li $v0, 4 		
	syscall
	
	#read_integer into $v0
	li $v0, 5		
	syscall	
	
	#move first input value from $v0 to $t0
	move $t0, $v0
	
	#print_string using prompt2
	la $a0, prompt2
	li $v0, 4 		
	syscall
	
	#read_integer into $v0
	li $v0, 5		
	syscall	
	
	#move second input value from $v0 to $t1
	move $t1, $v0
	
	#print_string using prompt3
	la $a0, prompt3
	li $v0, 4 		
	syscall
	
	#read_integer into $v0
	li $v0, 5		
	syscall	
	
	#move third input value from $v0 to $t2
	move $t2, $v0
	
	#print_string using msg
	la $a0, msg	
	li $v0, 4		
	syscall	
	
	#if (first number < second number), branch to second
	blt $t0, $t1, second
	
	#if (first number < third number), branch to third
	blt $t0, $t2, third
	
	#print_integer using the first number
	move $a0, $t0
	li $v0, 1
	syscall
	
	#jump to end of program
	j crlf
	
second:
	#if (second number < third number), branch to third
	blt $t1, $t2, third
	
	#print_integer using the second number
	move $a0, $t1
	li $v0, 1
	syscall
	
	#jump to end of program
	j crlf
	
third:
	#print_integer using the third number
	move $a0, $t2
	li $v0, 1
	syscall
	
crlf:
	#print_string using endl (CRLF)
	la $a0, endl	
	li $v0, 4		
	syscall	
	
	#exit
	li $v0, 10
	syscall	
	
	.data
	#null terminated strings
prompt1: .asciiz "Enter the first number: "
prompt2: .asciiz "Enter the second number: "
prompt3: .asciiz "Enter the third number: "
msg: .asciiz "The largest number you entered is: "
endl: .asciiz "\n"

################################################################
#Program Output:
################################################################
#
#Enter the first number: 15
#Enter the second number: 10
#Enter the third number: 27
#The largest number you entered is: 27
#
################################################################