################################################################
#
#	Vitaliy Shydlonok
#	CSC 211-002
#	9/5/2017
#	vs_lab 1
#
#	This is my first assembly language program for this class.
#	It will print a message on the screen.
#
#		Store message in str
#		Load message address
#		System call to print message
#		System call to exit program
#
################################################################

	.text
	.globl main
main:
	la $a0, str 	#load adress of str into $a0
	li $v0, 4 		#load immediate constant 4 into $v0
	syscall 		#call print_string
	
	li $v0, 10 		#load immediate constant 10 into $v0
	syscall			#call end of program

	.data
str: .asciiz "Hello World!\nI'm finally learning SOMETHING!
\nAnd it works!\n" 	#define str as null terminating string data