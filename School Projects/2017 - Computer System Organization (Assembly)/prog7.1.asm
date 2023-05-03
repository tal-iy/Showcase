################################################################
#
#	Vitaliy Shydlonok
#	CSC 211-002
#	11/3/2017
#	vs_program 7.1
#
#	Mask and Rotate
#
#	Prints ascii of 0x44434241
#
################################################################

	.text
    .globl main
main:
	# Initialize word $t2 to 0x44434241
    li $t2, 0x44434241
	
	# Mask least most 8 bits of word $t2 into $a0
	and $a0, $t2, 0xff
	
	# Print $a0 as ascii character
	li $v0, 11
	syscall
	
	# Rotate right word $t2 by 8 bits
	ror $t2, $t2, 8
	
	# Mask least most 8 bits of word $t2 into $a0
	and $a0, $t2, 0xff
	
	# Print $a0 as ascii character
	li $v0, 11
	syscall
	
	# Rotate right word $t2 by 8 bits
	ror $t2, $t2, 8
	
	# Mask least most 8 bits of word $t2 into $a0
	and $a0, $t2, 0xff
	
	# Print $a0 as ascii character
	li $v0, 11
	syscall
	
	# Rotate right word $t2 by 8 bits
	ror $t2, $t2, 8
	
	# Mask least most 8 bits of word $t2 into $a0
	and $a0, $t2, 0xff
	
	# Print $a0 as ascii character
	li $v0, 11
	syscall

	# Display "cr/lf"
	la $a0,crlf         
	li $v0,4            
	syscall
	
	# End Of Program
	li $v0,10           
	syscall 
        
	.data
crlf:   .asciiz "\n" 

################################################################
# Program Output:
################################################################
#
#ABCD
#
################################################################
