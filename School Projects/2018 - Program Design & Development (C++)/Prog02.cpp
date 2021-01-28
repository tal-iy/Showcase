/********************************************************************** 
* Program Name:   In Order
* Author:         Vitaliy Shydlonok 
* Date:           October 1
* Course/Section: CSC 110-301W
* Program Description: 
*	This program will prompt the user for three numbers, then determine
*	the smallest, middle, and largest of those numbers, and displays
*	them in ascending order.
**********************************************************************/
/********************************************************************** 
* 
* Inputs:
*	First number: int firstNumber
*	Second number: int secondNumber
*	Third number: int thirdNumber
*
* Outputs:
*	Smallest number: int smallest
*	Middle number: int middle
*	Largest number: int largest
*
* Formulas: NONE
*
* Pseudocode: 
*	Initial Algorithm 
*	------- 
*		Get the first number
*		Get the second number
*		Get the third number
*		Determine the smallest, middle, and largest numbers
*		Display the numbers in ascending order
* 
*	Refined Algorithm 
*	------- 
* 		Clear screen 
*		Display "Enter the first number: " 
*		Input the first number
*		Display "Enter the second number: " 
*		Input the second number
*		Display "Enter the third number: " 
*		Input the third number
*		IF first number < second number THEN
*			IF first number < third number THEN
*				smallest = first number
*				IF second number < third number THEN
*					middle = second number
					largest = third number
				ELSE
					middle = third number
					largest = second number
				END IF
*			ELSE
*				smallest = third number
*				middle = first number
*				largest = second number
*			END IF
*		ELSE
*			IF second number < third number THEN
*				smallest = secondN number
*				IF first number < third number THEN
					middle = first number
					largest = third number
				ELSE
					middle = third number
					largest = first number
				END IF
*			ELSE
*				smallest = third number
*				middle = second number
*				largest = first number
*			END IF
*		END IF
*		Display "The numbers in ascending order are: "
*		Display the smallest, middle, and largest numbers
**********************************************************************/ 

/************************** Compiler Directives **********************/ 

#include <cstdlib>
#include <iomanip>
#include <iostream> 
#include <string> 

using namespace std; 

/*********************** Global Data Declarations ********************/ 

//None in this program. 

int main() 
{ 
	/************************ Local Variables ************************/
	int firstNumber = 0;
	int secondNumber = 0;
	int thirdNumber = 0;
	int smallest = 0;
	int middle = 0;
	int largest = 0;

	/*************** Begin main Function Executables *****************/ 
	
	//Clear the screen 
	system("cls"); 
	
	//Get the first number
	cout << "Enter the first number: ";
	cin >> firstNumber;
	
	//Get the second number
	cout << "Enter the second number: ";
	cin >> secondNumber;
	
	//Get the third number
	cout << "Enter the third number: ";
	cin >> thirdNumber;
	
	//Check if the first number is less than the second number
	if (firstNumber < secondNumber)
	{
		//Check if the first number is less than the third number
		if (firstNumber < thirdNumber)
		{
			smallest = firstNumber;
			
			//Check if the second number is less than the third number
			if (secondNumber < thirdNumber)
			{
				middle = secondNumber;
				largest = thirdNumber;
			}
			else
			{
				middle = thirdNumber;
				largest = secondNumber;
			}
		}
		else
		{
			smallest = thirdNumber;
			middle = firstNumber;
			largest = secondNumber;
		}
	}
	else
	{
		//Check if the second number is less than the third number
		if (secondNumber < thirdNumber)
		{
			smallest = secondNumber;
			
			//Check if the first number is less than the third number
			if (firstNumber < thirdNumber)
			{
				middle = firstNumber;
				largest = thirdNumber;
			}
			else
			{
				middle = thirdNumber;
				largest = firstNumber;
			}
		}
		else
		{
			smallest = thirdNumber;
			middle = secondNumber;
			largest = firstNumber;
		}
	}
	
	//Display the three numbers in ascending order
	cout << "The numbers in ascending order are: " << smallest << ", " << middle << ", and " << largest << endl;
	
	//Pause to read output 
	system ("pause"); 
	
	//Indicate to OS successful termination of program 
	return 0; 
}	//End main
