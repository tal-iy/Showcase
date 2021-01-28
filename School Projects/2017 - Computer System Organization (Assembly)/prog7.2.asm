################################################################
#
#	Vitaliy Shydlonok
#	CSC 211-002
#	11/3/2017
#	vs_program 7.2
#
#	Mask and Rotate using a loop
#
#	Prints ascii of 0x706F6F4C, "Loop" backwards
#
################################################################

	.text
    .globl main
main:
	# Initialize index $t0 to 0
	li $t0, 0
	
	# Initialize ending index $t1 to 4
	li $t1, 4

	# Initialize word $t2 to 0x706F6F4C
    li $t2, 0x706F6F4C
	
loop: 
	# If index $t0 >= ending index $t1, branch to endLoop
	bge $t0, $t1, endLoop
	
	# Mask least most 8 bits of word $t2 into $a0
	and $a0, $t2, 0xff
	
	# Print $a0 as ascii character
	li $v0, 11
	syscall
	
	# Rotate right word $t2 by 8 bits
	ror $t2, $t2, 8

	# Increment index $t0 by 1
	add $t0, $t0, 1
	
	# Jump back to loop
	j loop
	
endLoop:
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
#Loop
#
################################################################
