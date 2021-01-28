################################################################
#
#	Vitaliy Shydlonok
#	CSC 211-002
#	9/21/2017
#	vs_program 4
#
#	Prompts the user to input an integer value,
#	then prints the digits from 1 to that number
#	in a right justified columns format.
#
#	Begin
#		Print prompt
#		Get integer input
#		Calculate the string length of the input
#		Begin main loop
#			Calculate the string length of the current number printing
#			Subtract current length from input length to get number of spaces
#			Print space character the appropriate number of times
#			Print the current number
#			Print CRLF if current number is a multiple of 10
#		End main loop
#		CRLF
#	End
#
################################################################

	.text
	.globl main

main:
	#prompt for an integer value
	la $a0, prompt
	li $v0, 4
	syscall
	
	#get integer input
	li $v0, 5
	syscall
	
	#move input into $t0
	move $t0, $v0
	
	#initialize size counter
	li $t3, 1
	
	#copy input into $t4
	move $t4, $t0

	#calculate number of times you can divide input
	#by 10 and put it in $t3
count:
	#calculate ($t4 / 10) and put it into $t4
	li $t8, 10
	div $t4, $t8
	mflo $t4

	#branch to endCount, if $t4 equals zero
	beqz $t4, endCount
	
	#increment size counter
	add $t3, $t3, 1
	
	#jump back to start of loop
	j count
	
endCount:	
	#print endl
	la $a0, endl
	li $v0, 4
	syscall

	#initialize the main counter
	li $t1, 1
	
print:
	#initialize current size counter
	li $t6, 1

	#copy main counter to $t5
	move $t5, $t1
size:
	#calculate ($t5 / 10) and put it into $t5
	li $t8, 10
	div $t5, $t8
	mflo $t5

	#branch to endSize, if $t5 equals zero
	beqz $t5, endSize
	
	#increment current size counter
	add $t6, $t6, 1
	
	#jump back to start of size loop
	j size
	
endSize:
	#put ($t3-$t6) into $t6 
	#(size of largest number - size of current number printing) into number of spaces counter
	sub $t6, $t3, $t6
	
spaces:
	#branch to endSpaces if $t6 equals zero
	beqz $t6, endSpaces
	
	#print space
	la $a0, space
	li $v0, 4
	syscall
	
	#decrement spaces counter
	sub $t6, $t6, 1
	
	#jump back to start of spaces loop
	j spaces
	
endSpaces:
	#print counter
	move $a0, $t1
	li $v0, 1
	syscall
	
	#print dspace
	la $a0, dspace
	li $v0, 4
	syscall
	
	#branch to endPrint, if counter equals initial input
	beq $t1, $t0, endPrint
	
	#calculate (counter mod 10) and move it to $t2
	li $t2, 10
	div $t1, $t2
	mfhi $t2
	
	#branch to crlf if (counter mod 10) == 0
	beqz $t2, crlf
	
	#jump over crlf
	j crlfRet
	
crlf:
	#print endl
	la $a0, endl
	li $v0, 4
	syscall
	
crlfRet:
	#increment counter $t1
	add $t1, $t1, 1
	
	#jump back to start of the print loop
	j print	

endPrint:
	#print last endl
	la $a0, endl
	li $v0, 4
	syscall

	#system return
	li $v0, 10
	syscall
	
	.data
prompt: .asciiz "Enter a number greater than zero: "
space: .asciiz " "
dspace: .asciiz "  "
endl: .asciiz "\n"

################################################################
#Program Output:
################################################################
#
#Enter a number greater than zero: 110
#
#  1    2    3    4    5    6    7    8    9   10  
# 11   12   13   14   15   16   17   18   19   20  
# 21   22   23   24   25   26   27   28   29   30  
# 31   32   33   34   35   36   37   38   39   40  
# 41   42   43   44   45   46   47   48   49   50  
# 51   52   53   54   55   56   57   58   59   60  
# 61   62   63   64   65   66   67   68   69   70  
# 71   72   73   74   75   76   77   78   79   80  
# 81   82   83   84   85   86   87   88   89   90  
# 91   92   93   94   95   96   97   98   99  100  
#101  102  103  104  105  106  107  108  109  110  
#
################################################################
