################################################################
#
#	Vitaliy Shydlonok
#	CSC 211-002
#	9/14/2017
#	vs_hw 2
#
#	Prompts the user to input a Fahrenheit value, then 
#	prints the value converted to Celsius on the screen.
#
#	Begin
#		Print prompt
#		Get float input
#		Convert input to Celcius
#		Print message
#		Print entered value
#		Print second message
#		Print converted value
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
	
	#initialize float values
	li.s $f6, 9.0	
	li.s $f8, 5.0	
	li.s $f10, 32.0	
	
	sub.s $f2, $f0, $f10	#temperature - 32
	mul.s $f2, $f2, $f8		#5 * (temperature - 32)
	div.s $f2, $f2, $f6		#5 * (temperature - 32) / 9
	
	#print_string using ans1
	la $a0, ans1
	li $v0, 4	
	syscall	
	
	#print_float using $f0 (original temperature)
	mov.s $f12, $f0	
	li $v0, 2		
	syscall
	
	#print_string using ans2
	la $a0, ans2	
	li $v0, 4		
	syscall
	
	#print_float using $f2 (converted temperature)
	mov.s $f12, $f2	
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
prompt: .asciiz "Enter temperature (Fahrenheit): "
ans1: .asciiz "The temperature "
ans2: .asciiz " Fahrenheit converted to Celcius is "
endl: .asciiz "\n"

################################################################
#Program Output:
################################################################
#
#Enter temperature (Fahrenheit): 32
#The temperature 32.00000000 Fahrenheit converted to Celcius is 0.00000000
#
#Enter temperature (Fahrenheit): 212
#The temperature 212.00000000 Fahrenheit converted to Celcius is 100.00000000
#