	.text
    .globl main
main:

	# initialize index $t0 to 0
	li $t0, 0

inputLoop: 
	# while index $t0 < size
	lw $t1, size
	bge $t0, $t1, endInputLoop
	
	# print prompt
	la $a0, prompt
	li $v0, 4
	syscall
	
	# get integer into array[$t0]
	li $v0, 5
	syscall
	sw $v0, array($t0)
	
	# increment index $t0 by int size 4
	add $t0, $t0, 4
	
	# jump back to loop start
	j inputLoop
	
endInputLoop:

	# print answer
	la $a0, answer
	li $v0, 4
	syscall

	# re-initialize index $t0 back to 0
	li $t0, 0
	
outputLoop:
	# while index $t0 < size
	lw $t1, size
	bge $t0, $t1, endOutputLoop
	
	# print array[$t0]
	lw $a0, array($t0)
	li $v0, 1
	syscall
	
	# print space
	la $a0, space
	li $v0, 4
	syscall
	
	# increment index $t0 by int size 4
	addi $t0, 4
	
	# jump back to loop start
	j outputLoop
	
endOutputLoop:

	# print crlf
	la $a0, crlf
	li $v0, 4
	syscall
	
	# end program
	li $v0, 10
	syscall
	
	.data
array: .space 40
size: .word 40

prompt: .asciiz "Enter a number: "
answer: .asciiz "You entered: "
space: .asciiz " "
crlf: .asciiz "\n"
	
	
	