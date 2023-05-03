################################################################
#
#	Vitaliy Shydlonok
#	CSC 211-002
#	9/7/2017
#	vs_lab 2
#
#	Prompts the user to input an integer value,
#	then prints the entered value to the screen.
#
#	Begin
#		Print prompt
#		Get integer input
#		Print message
#		Print entered value
#		CRLF
#	End
#
################################################################

	.text
	.globl main
main:
	la $a0, prompt 	#load adress of prompt into $a0
	li $v0, 4 		#load immediate constant 4 into $v0
	syscall 		#call print_string 
	
	li $v0, 5		#load immediate constant 5 into $v0
	syscall			#call get_integer
	move $t0, $v0	#move int value to temporary register
	
	la $a0, msg		#load adress of msg into $a0
	li $v0, 4		#load immediate constant 4 into $v0
	syscall			#call print_string
	
	move $a0, $t0	#move int value from temporary register to $a0
	li $v0, 1		#load immediate constant 1 into $v0
	syscall			#call print_integer
	
	la $a0, endl	#load adress of endl into $a0
	li $v0, 4		#load immediate constant 4 into $v0
	syscall			#call print_string
	
	li $v0, 10 		#load immediate constant 10 into $v0
	syscall			#call end of program

	.data
prompt: .asciiz "Enter an integer value: " 	#null terminated prompt string
msg: .asciiz "The value you entered was: "	#null terminated message string
endl: .asciiz "\n"							#null terminated CRLF string

################################################################
#Program Output:
################################################################
#Enter an integer value: 5
#The value you entered was: 5

