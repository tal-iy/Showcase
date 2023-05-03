################################################################
#
#	Vitaliy Shydlonok
#	CSC 211-002
#	9/14/2017
#	vs_lab 3
#
#	Prompts the user to input the price of
#	an item, then prints the sales tax and
#	total price on the screen.
#
#	Begin
#		Print prompt
#		Get float input
#		Print first message
#		Calculate tax value
#		Print tax value
#		Print second message
#		Calculate total
#		Print total
#		CRLF
#	End
#
################################################################

	.text
	.globl main
main:
	#print_string using prompt
	la $a0, prompt
	li $v0, 4 		
	syscall
	
	#read_float into $f0
	li $v0, 6		
	syscall	
	
	#print_string using first message
	la $a0, msg1	
	li $v0, 4 		
	syscall
	
	#initialize float tax rate value
	li.s $f6, 0.08	
	
	#move input to $f2
	mov.s $f2, $f0
	
	#calculate tax value into $f4
	mul.s $f4, $f2, $f6
	
	#print_float using $f4 (tax value)
	mov.s $f12, $f4
	li $v0, 2
	syscall
	
	#print_string using second message
	la $a0, msg2	
	li $v0, 4 		
	syscall
	
	#calculate total price into $f4
	add.s $f4, $f4, $f2
	
	#print_float using $f4 (total price)
	mov.s $f12, $f4
	li $v0, 2
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
prompt: .asciiz "Enter the price of your item: "
msg1: .asciiz "The tax on that item is "
msg2: .asciiz "\nThe total cost of the item is "
endl: .asciiz "\n"

################################################################
#Program Output:
################################################################
#
#Enter the price of your item: 10
#The tax on that item is 0.79999995
#The total cost of the item is 10.80000019
#