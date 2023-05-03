################################################################
#
#	Vitaliy Shydlonok
#	CSC 211-002
#	9/21/2017
#	vs_lab 6
#
#	Prompts the user to input a string,
#	then counts the number of characters in that
#	string and prints that number.
#
#	Begin
#		Print prompt
#		Get string input
#		Calculate the string length of the input
#		Print answer
#		Print calculated length
#		CRLF
#	End
#
################################################################

	.text
	.globl main

main:
	#prompt for a string
	la $a0, prompt
	li $v0, 4
	syscall
	
	#get string input
	li $v0, 8
	syscall
	
	#move input into $t2
	move $t2, $a0
	
	#initialize the counter $t1
	li $t1, 0
	
while:
	#load current character into $t0
	lb $t0, ($t2)
	
	#branch to endwhile if end of string
	beqz $t0, endwhile
	
	#increment counter $t1
	add $t1, $t1, 1
	
	#go to the next character
	add $t2, 1
	
	#jump back to start of the loop
	j while
	
endwhile:
	#display answer string
	la $a0, ans
	li $v0, 4
	syscall
	
	#display counter
	move $a0, $t1
	li $v0, 1
	syscall
	
	#CRLF
	la $a0, endl
	li $v0, 4
	syscall
	
	#system return
	li $v0, 10
	syscall
	
	.data
prompt: .asciiz "Enter a string: "
ans: .asciiz "Length is "
endl: .asciiz "\n"

################################################################
#Program Output:
################################################################
#
#Enter a string: String test
#Length is 12
#
################################################################	