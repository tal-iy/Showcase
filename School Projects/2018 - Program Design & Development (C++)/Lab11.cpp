/********************************************************************** 
* Program Name:   Lab 11
* Author:         Vitaliy Shydlonok 
* Date:           December 14
* Course/Section: CSC 110-301W
* Program Description: 
*	This program will generate an array of 20 random integers between.
*	1 and 10. Then, will generate another integer and count the number
*	of times it occurs within the array. Then will print the array,
*	the target number, and the number of occurences. Will repeat as
*	long as the user enters 'Y' when asked to generate another array.
**********************************************************************/
/********************************************************************** 
* 
* Inputs:
*	Whether to run again: char again;
*
* Outputs:
*	Array of random numbers: int randomArray[20]
*	Target random number: int randomNumber
*	Number of occurences: int count
*
* Additional Data: none
*
* Formulas: none
*
* Pseudocode: 
*	Initial Algorithm 
*	------- 
*		Fill an array with 20 random numbers
*		Generate a target number
*		Count the number of occurences within the array
*		Print the generated array
*		Print the target number
*		Print the number of occurences
*		Prompt to run again and loop back to the begining
* 
*	Refined Algorithm
*	------- 
*		Seed the random number generator using system time
*		DO
*			Clear the screen
*			Fill the array with random numbers
*			Generate a random target number
*			Reset running total of occurences within the array
*			FOR index = 0 TO 20
*				IF cell at index contains target number THEN
*					Increment the number of occurences
*				END IF
*			END FOR
*			Print the array of random numbers
*			Print the target random number
*			IF number of occurences is 0 THEN
*				Print "The target number did not occur within the array!"
*			ELSE
*				Print "The target number occured "
*				Print the number of occurences
*				Print " time(s) within the array!"
*			END IF
*			Prompt the user to run again
*		WHILE user enters 'y' or 'Y' at prompt to run again
*
**********************************************************************/ 

/************************** Compiler Directives **********************/ 

#include <cstdlib>
#include <ctime>
#include <iomanip>
#include <iostream> 
#include <string> 

using namespace std; 

/*********************** Global Data Declarations ********************/ 

//function prototypes
int getRandom(int begining, int ending);
void fillArray(int intArray[], int size);
void printInts(int intArray[], int size);

int main()
{
	/************************ Local Variables ************************/
	int randomArray[20];
	int randomNumber = 0;
	int count = 0;
	char again = ' ';
	
	/************************ Random Numbers  *************************/
	
	//Seed the random number generator using system time
	srand(static_cast<unsigned int>(time(0)));
	
	do
	{
		//Clear the screen 
		system("cls"); 
		
		//Fill the array with random numbers
		fillArray(randomArray, 20);
		
		//Generate a random target number
		randomNumber = getRandom(1, 10);
		
		//Reset running total of occurences within the array
		count = 0;
		
		//Loop through the random numbers array
		for (int index = 0; index < 20; index++)
		{
			//Increment the running total if the current cell contains the target number
			if (randomArray[index] == randomNumber)
				count ++;
		}
		
		//Print the array of random numbers
		cout << endl << "The random numbers generated are: ";
		printInts(randomArray, 20);
		
		//Print the target random number
		cout << "The target number is: " << randomNumber << endl;
		
		//Print the number of times that the target number occured within the array
		if (count == 0)
			cout << "The target number did not occur within the array!" << endl;
		else
			cout << "The target number occured " << count << " time(s) within the array!" << endl;
			
		//Prompt whether to run again
		cout << endl << "Do you want to enter another set of numbers? (Y/N) ";
		cin >> again;
		
	} while (again == 'y' || again == 'Y');
	
	//Pause to read output 
	system("pause");
	
	//Indicate to OS successful termination of program 
	return 0; 
}//end main

/*
* Name: getRandom
* Author: Vitaliy Shydlonok
* Date: 12/14/18
* Description:
*	Generates a random integer within a specified range.
*
* Pseudocode:
*	Seed the random number generator
*	Generate a random number within a range
*	Return the random number
*/
int getRandom(int begining, int ending)
{
	//Initialize the random number
	int random = 0;
	
	//Generate a random number within a range
	random = (rand() % (ending - begining + 1)) + begining;
	
	//Return the random number
	return random;
} //end getRandom

/*
* Name: fillArray
* Author: Vitaliy Shydlonok
* Date: 12/14/18
* Description:
*	Fills an array with random integers between 1 and 10.
*
* Pseudocode:
*	Loop through the given array
*	Fill each array cell with a random number
*/
void fillArray(int intArray[], int size)
{
	//Loop through the array
	for(int index = 0;index < size;index++)
	{
		//Fill the current array cell with a random number between 1 and 10
		intArray[index]= getRandom(1, 10);;
	}
	
} //end fillArray

/*
* Name: printInts
* Author: Vitaliy Shydlonok
* Date: 12/14/18
* Description:
*	Prints the contents of an array.
*
* Pseudocode:
*	Loop through the given array
*	Print the value of each cell in the array
*/
void printInts(int intArray[], int size)
{
	//Loop through the array
	for(int index = 0;index < size;index++)
	{
		//Print the value in the current array cell
		cout << intArray[index] << " ";
	}
	
	//Print a new line character
	cout << endl;
} //end printInts

