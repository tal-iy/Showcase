/********************************************************************** 
* Program Name:   Lab 10
* Author:         Vitaliy Shydlonok 
* Date:           December 3
* Course/Section: CSC 110-301W
* Program Description: 
*	This program will ask for 10 grades, then will print the entered
*	grades, compute the average of the grades, and print the average.
*	Then, will generate 20 random integers between 1 and 50, print
*	the generated numbers, and then calculate and print the average
*	value of the generated numbers.
**********************************************************************/
/********************************************************************** 
* 
* Inputs:
*	Array of grades: int grades[10]
*
* Outputs:
*	Average of the grades: float averageGrade
*	Average of the random numbers: float averageNumber
*
* Additional Data: none
*
* Formulas:
*	average = total/size
*	random = (rand() % (ending - begining + 1)) + begining
*
* Pseudocode: 
*	Initial Algorithm 
*	------- 
*		Ask for 10 grades
*		Print the entered grades
*		Calculate and print the average grade
*
*		Fill an array with 20 random numbers
*		Print the generated numbers
*		Calculate and print the average number
* 
*	Refined Algorithm
*	------- 
*		Clear the screen
*		Prompt for 10 grades using getGrades
*		Print the entered grades using printInts
*		Calculate the average grade using computeAverage
*		Print the average grade
*	
*		Seed the random number generator using system time
*		FOR index = 0 TO 20
*			Fill the current array cell with a random number using getRandom
*		END FOR
*		Print the random numbers using printInts
*		Calculate the average number using computeAverage
*		Print the average number
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
void getGrades(int gradeArray[], int size);
float computeAverage(int numbers[], int size);
void printInts(int intArray[], int size);

int getRandom(int begining, int ending);

int main()
{
	/************************ Local Variables ************************/
	int grades[10];
	int randomArray[20];
	
	float averageGrade;
	float averageNumber;
	
	/**************************** Grades *****************************/ 
	
	//Clear the screen 
	system("cls"); 
	
	//Prompt for grades
	getGrades(grades, 10);
	
	//Print the list of grades entered
	cout << endl << "The grades entered are: ";
	printInts(grades, 10);
	
	//Calculate the average of the grades entered
	averageGrade = computeAverage(grades, 10);
	
	//Print the average grade value
	cout << "The average of the grades entered is: " << averageGrade << endl;
	
	/************************ Random Numbers  *************************/
	
	//Seed the random number generator using system time
	srand(static_cast<unsigned int>(time(0)));
	
	//Loop through the random numbers array
	for (int index = 0; index < 20; index++)
	{
		//Fill the current array cell with a random number between 1 and 50
		randomArray[index] = getRandom(1, 50);
	}
	
	//Print the list of random numbers
	cout << endl << "The random numbers generated are: ";
	printInts(randomArray, 20);
	
	//Calculate the average of the random numbers
	averageNumber = computeAverage(randomArray, 20);
	
	//Print the average of the random numbers
	cout << "The average of the random numbers is: " << averageNumber << endl << endl;
	
	//Pause to read output 
	system("pause");
	
	//Indicate to OS successful termination of program 
	return 0; 
}//end main

/*
* Name: getGrades
* Author: Vitaliy Shydlonok
* Date: 12/3/18
* Description:
*	Fills an array with grade values given by the user.
*
* Pseudocode:
*	Loop through the given array
*	At each array cell, prompt for a grade
*	Input the grade into the array
*/
void getGrades(int gradeArray[], int size)
{
	//Loop through the array
	for(int index = 0; index < size; index++)
	{
		//Prompt for a grade
		cout << "Enter grade number " << index+1 << ": ";
		
		//Input the grade into the array
		cin >> gradeArray[index];
	}
} //end getGrades

/*
* Name: computeAverage
* Author: Vitaliy Shydlonok
* Date: 12/3/18
* Description:
*	Determines the average value stored in an array.
*
* Pseudocode:
*	Loop through the given array
*	Add each cell to a running total
*	Calculate the average based on the running total and array size
*	Return the average
*/
float computeAverage(int numbers[], int size)
{
	//Initialize average
	float average = 0.0;
	
	//Initialize running total
	int total = 0;
	
	//Loop through the array
	for(int index = 0;index < size;index++)
	{
		//Add array cell to the running total
		total += numbers[index];
	}
	
	//Compute the average
	average = total/size;
	
	//Return the average
	return average;
	
} //end computeAverage

/*
* Name: printInts
* Author: Vitaliy Shydlonok
* Date: 12/3/18
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

/*
* Name: getRandom
* Author: Vitaliy Shydlonok
* Date: 12/3/18
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
}

