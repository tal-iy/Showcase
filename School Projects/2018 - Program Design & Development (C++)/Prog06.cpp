/********************************************************************** 
* Program Name:   Prog 6
* Author:         Vitaliy Shydlonok 
* Date:           December 14
* Course/Section: CSC 110-301W
* Program Description: 
*	This program will generate an array of 20 random integers between.
*	1 and 10. Then, will use the bubble sort algorithm to sort the
*	array. Then will print both the unsorted and sorted array.
**********************************************************************/
/********************************************************************** 
* 
* Inputs: none
*
* Outputs:
*	Array of random numbers: int randomArray[20]
*
* Additional Data: none
*
* Formulas: none
*
* Pseudocode: 
*	Initial Algorithm 
*	------- 
*		Fill an array with 20 random numbers
*		Print the generated array
*		Use bubble sort to sort the array
*		Print the sorted array
* 
*	Refined Algorithm
*	------- 
*		Seed the random number generator using system time
*		Clear the screen
*		Fill the array with random numbers using fillArray
*		Print the array of random numbers using printInts
*		Bubble sort the array using bubbleSort
*		Print the sorted array using printInts
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
void bubbleSort(int intArray[], int size);

int main()
{
	/************************ Local Variables ************************/
	int randomArray[20];
	
	/************************* Bubble Sort ***************************/
	
	//Seed the random number generator using system time
	srand(static_cast<unsigned int>(time(0)));
	
	//Clear the screen 
	system("cls"); 
	
	//Fill the array with random numbers
	fillArray(randomArray, 20);
	
	//Print the unsorted array of random numbers
	cout << "The random numbers generated are: ";
	printInts(randomArray, 20);
	
	//Use bubble sort to sort the array
	bubbleSort(randomArray, 20);
	
	//Print the sorted array of random numbers
	cout << "The random numbers sorted are: ";
	printInts(randomArray, 20);
	
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

/*
* Name: bubbleSort
* Author: Vitaliy Shydlonok
* Date: 12/14/18
* Description:
*	Uses the bubble sort algorithm to sort an array.
*
* Pseudocode:
*	Initialize temporary variable
*	Use the bubble sort algorithm
*/
void bubbleSort(int intArray[], int size)
{
	//Initialize a temporary variable
	int temp = 0;
	
	//Outer loop
	for(int i = 0;i < size-1;i++)
	{
		//Inner loop
		for(int j = 1;j < size;j++)
		{
			//Compare the current cell with the adjacent cell
			if (intArray[j] < intArray[j-1])
			{
				//Switch the cell values if the current one is smaller than the previous
				temp = intArray[j];
				intArray[j] = intArray[j-1];
				intArray[j-1] = temp;
			}
		}
	}

} //end bubbleSort

