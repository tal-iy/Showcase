#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>

/*
	char* reverse: Returns a reversed string
*/
char* reverse(char* str)
{
	// Temporary variables
	char temp;
	int i=0;
	
	// Get the string length
	int length = strlen(str);
	
	// Loop through half of the string
	for(i=0; i<length/2; i++)
	{
		// Switch the current character with one from the other side
		temp = str[i];
		str[i] = str[strlen(str)-1-i];
		str[strlen(str)-1-i] = temp;
	}
	
	return str;
}

/*
	void dec2bin: Prints the binary representation of a decimal integer within a string
*/
void dec2bin(char* dec)
{
	// Make sure the string only contains numbers
	if (strtok(dec, "1234567890") != NULL)
	{
		printf("[Error] Please enter the command correctly!\n"); // Invalid character in decimal string
	}
	// Handle the special case of 0
	else if (strcmp(dec, "0") == 0)
	{
		printf("0\n");
	}
	// Handle the special case of 1
	else if (strcmp(dec, "1") == 0)
	{
		printf("1\n");
	}
	else
	{
		// Convert the string to an integer number
		int num = atoi(dec);
		
		// Temporary variables
		char binary[32];
		int i;
		
		// Repeat until the current number is zero to build the binary string
		for(i=0; num>0; i++)
		{
			// Add a '1' to the string if the current number is odd, '0' if the current number is even
			binary[i] = (num % 2 == 1 ? '1' : '0');
			// Divide the current number by 2 without remainder
			num /= 2;
		}
		
		// Reverse the binary string
		reverse(binary);
		
		// Print the result
		printf("%s\n", binary);
	}
}

/*
	void bin2dec: Prints the decimal representation of a binary number within a string
*/
void bin2dec(char* bin)
{
	// Make sure the string only contains 1s and 0s
	if (strtok(bin, "10") != NULL)
	{
		printf("[Error] Please enter the command correctly!\n");
	}
	else
	{
		// Temporary variables
		int decimal = 0;
		int power = 1;
		int i;
		
		// Get the string length
		int length = strlen(bin);
		
		// Loop through every character from right to left
		for(i=length-1; i >= 0; i--) 
		{
			// Convert the current character to an integer (1 or 0)
			int bit = (bin[i] == '0' ? 0 : 1);
			
			// Tally the current power of 2 multiplied by the current character
			decimal += bit*power;
			
			// Increase the current power of 2
			power *= 2;
		}
		
		// Print the result
		printf("%d\n", decimal);
	}
}

/*
	void unarycode: Prints the unary encoded version of a decimal integer within a string
*/
void unarycode(char* dec)
{
	// Make sure the string only contains numbers
	if (strtok(dec, "1234567890") != NULL)
	{
		printf("[Error] Please enter the command correctly!\n"); // Invalid character in decimal string
	}
	else
	{
		// Convert the string to an integer number
		int num = atoi(dec);
		
		// Loop a given number of times based on the input number
		int i;
		for(i=0; i<num; i++)
		{
			// Print a 1 every loop cycle
			printf("1");
		}
		
		// Print the final 0 and end-line character
		printf("0\n");
	}
}

/*
	void gammacode: Prints the gamma encoded version of a decimal integer within a string
*/
void gammacode(char* dec)
{
	// Make sure the string only contains numbers
	if (strtok(dec, "1234567890") != NULL)
	{
		printf("[Error] Please enter the command correctly!\n"); // Invalid character in decimal string
	}
	// Handle the special cases of 0 or 1
	else if (strcmp(dec, "1") == 0 || strcmp(dec, "0") == 0)
	{
		printf("0\n");
	}
	else
	{
		// Convert the string to an integer number
		int num = atoi(dec);
		
		// Temporary variables
		char binary[32];
		char gamma[100];
		int i, j;
		
		// Repeat until the current number is zero to build the binary string
		for(i=0; num>0; i++)
		{
			// Add a '1' to the string if the current number is odd, '0' if the current number is even
			binary[i] = num % 2 == 1 ? '1' : '0';
			// Divide the current number by 2 without remainder
			num /= 2;
		}
		
		// Reverse the binary string
		reverse(binary);
		
		// Get the length of the binary string
		int length = strlen(binary);
		
		// Convert the length minus 1 to unary
		for(i=0; i<length-1; i++)
			gamma[i] = '1';
		gamma[i] = '0';
		
		// Add the binary offset to the unary length
		for(j=1; j<length; j++)
		{
			i++;
			gamma[i] = binary[j];
		}
		
		// Print the result
		printf("%s\n", gamma);
	}
}

/*
	void main: Repeatedly asks the user to input a command until the user chooses to exit
*/
int main()
{
	char input[100];
	char* delimeters = "()\n";
	
	int running = 1;
	
	// Loop until the user exits
	while(running)
	{
		// Print the user ID
		printf("%s $ ", getlogin());
		
		// Read user input
		fgets(input, 100, stdin);
		
		// Check for exit command
		if (strcmp(input, "exit\n") == 0)
		{
			running = 0;
		}
		// Make sure there is an ending paranthesis
		else if (input[strlen(input)-2] != ')')
		{
			printf("[Error] Please enter the command correctly!\n"); // No ending paranthesis
		}
		else
		{
			// Get the first three tokens of the input string
			char* command = strtok(input, delimeters);
			char* parameter = strtok(NULL, delimeters);
			char* empty = strtok(NULL, delimeters);
			
			// Make sure only one parameter exists
			if (parameter == NULL || empty != NULL)
			{
				printf("[Error] Please enter the command correctly!\n"); // No parameter or more than one parameter
			}
			else 
			{
				// Determine which command is used
				if (strcmp(command, "dec2bin") == 0)
				{
					dec2bin(parameter);
				}
				else if (strcmp(command, "bin2dec") == 0)
				{
					bin2dec(parameter);
				}
				else if (strcmp(command, "unarycode") == 0)
				{
					unarycode(parameter);
				}
				else if (strcmp(command, "gammacode") == 0)
				{
					gammacode(parameter);
				}
				else
				{
					printf("[Error] Please enter the command correctly!\n"); // Unknown command
				}
			}
		}
	}
	
	return 0;
}
