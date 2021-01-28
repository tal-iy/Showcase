################################################################
#
#	Vitaliy Shydlonok
#	CSC 211-002
#	9/14/2017
#	vs_program 1
#
#	Prompts the user to input five integers,
#	then calculates and prints the equation
#	(a+b)-(c+d)*e, substituting the letters
#	with the five input values.
#
#	Begin
#		Print first prompt
#		Get first integer input
#		Print second prompt
#		Get second integer input
#		Print third prompt
#		Get third integer input
#		Print fourth prompt
#		Get fourth integer input
#		Print fifth prompt
#		Get fifth integer input
#		Calculate result of (a+b)-(c+d)*e
#		Print "(a+b)-(c+d)*e=" using inputs
#		Print result
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
	
	#print_string using prompt4
	la $a0, prompt4
	li $v0, 4 		
	syscall
	
	#read_integer into $v0
	li $v0, 5		
	syscall	
	
	#move fourth input value from $v0 to $t3
	move $t3, $v0
	
	#print_string using prompt5
	la $a0, prompt5
	li $v0, 4 		
	syscall
	
	#read_integer into $v0
	li $v0, 5		
	syscall	
	
	#move fifth input value from $v0 to $t4
	move $t4, $v0
	
	#calculate result
	add $t5, $t0, $t1	# a+b
	add $t6, $t2, $t3	# c+d
	mul $t7, $t6, $t4	# (c+d) * e
	sub $t8, $t5, $t7	# (a+b) - (c+d)*e
	
	#print_string "("
	la $a0, leftp	
	li $v0, 4		
	syscall	
	
	#print_integer the first input: a
	move $a0, $t0
	li $v0, 1
	syscall
	
	#print_string "+"
	la $a0, plus	
	li $v0, 4		
	syscall	
	
	#print_integer the second input: b
	move $a0, $t1
	li $v0, 1
	syscall
	
	#print_string ")"
	la $a0, rightp	
	li $v0, 4		
	syscall	
	
	#print_string "-"
	la $a0, minus	
	li $v0, 4		
	syscall	
	
	#print_string "("
	la $a0, leftp	
	li $v0, 4		
	syscall	
	
	#print_integer the third input: c
	move $a0, $t2
	li $v0, 1
	syscall
	
	#print_string "+"
	la $a0, plus	
	li $v0, 4		
	syscall	
	
	#print_integer the fourth input: d
	move $a0, $t3
	li $v0, 1
	syscall
	
	#print_string ")"
	la $a0, rightp	
	li $v0, 4		
	syscall	
	
	#print_string "*"
	la $a0, star	
	li $v0, 4		
	syscall	
	
	#print_integer the fifth input: e
	move $a0, $t4
	li $v0, 1
	syscall
	
	#print_string "="
	la $a0, equals	
	li $v0, 4		
	syscall	
	
	#print_integer the result: (a+b)-(c+d)*e
	move $a0, $t8
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
prompt1: .asciiz "Enter the first number: "
prompt2: .asciiz "Enter the second number: "
prompt3: .asciiz "Enter the third number: "
prompt4: .asciiz "Enter the fourth number: "
prompt5: .asciiz "Enter the fifth number: "
plus: .asciiz "+"
minus: .asciiz "-"
star: .asciiz "*"
equals: .asciiz "="
leftp: .asciiz "("
rightp: .asciiz ")"
endl: .asciiz "\n"

################################################################
#Program Output:
################################################################
#
#Enter the first number: 1
#Enter the second number: 2
#Enter the third number: 3
#Enter the fourth number: 4
#Enter the fifth number: 5
#(1+2)-(3+4)*5=-32
#