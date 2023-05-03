/********************************************************************** 
* Program Name:   Lab 9
* Author:         Vitaliy Shydlonok 
* Date:           November 19
* Course/Section: CSC 110-301W
* Program Description: 
*	This program will ask for two numbers, then determine whether the
*	first number is a multiple of the second number. It will loop as
*	long as the user enters a 'Y' or 'y' when asked to run again.
**********************************************************************/
/********************************************************************** 
* 
* Inputs:
*	First number: int xMain
*	Second number: int yMain
*	Run again: char again
*
* Outputs: none
*
* Additional Data: none
*
* Formulas:
*	(x % y == 0)
*
* Pseudocode: 
*	Initial Algorithm 
*	------- 
*		Ask for two numbers
*		Check for multiple
*		Print result
*		Ask to run again
*		Loop if the answer is yes
* 
*	Refined Algorithm
*	------- 
*		DO
* 			Prompt for the first number
*			Prompt for the second number
*			If the first number is a multiple of the second number THEN
*				Print that the numbers are multiples
*			ELSE
*				Print that the numbers aren't multiples
*			END IF
*			Prompt whether to run again
*		WHILE prompt was answered with a 'Y' or 'y'
*
**********************************************************************/ 

/************************** Compiler Directives **********************/ 

#include <cstdlib>
#include <iomanip>
#include <iostream> 
#include <string> 

using namespace std; 

/*********************** Global Data Declarations ********************/ 

//function prototype
bool isMultiple(int x, int y);

int main()
{
	/************************ Local Variables ************************/
	int xMain = 0;
	int yMain = 0;
	char again = ' ';
	
	/************************ The While Loop *************************/ 
	do
	{
		//Clear the screen 
		system("cls"); 
		
		//Prompt for the first number
		cout << "Enter the first number (x): ";
		cin >> xMain;
		
		//Prompt for the second number
		cout << "Enter the second number (y): ";
		cin >> yMain;
		
		//Determine whether the first number is a multiple of the second number
		if (isMultiple(xMain, yMain))
			cout << xMain << " is a multiple of " << yMain << endl; //Print that the numbers are multiples
		else
			cout << xMain << " is not a multiple of " << yMain << endl; //Print that the numbers aren't multiples
		
		//Prompt whether to run again
		cout << "Do you want to enter another set of numbers? (Y/N) ";
		cin >> again;
		
	} while (again == 'Y' || again == 'y');
	
	//Pause to read output 
	system("pause");
	
	//Indicate to OS successful termination of program 
	return 0; 
}//end main

/*
* Name: isMultiple
* Author: Vitaliy Shydlonok
* Date: 11/19/18
* Description:
*	Returns true if x is a multiple of y, and false otherwise.
*
* Pseudocode:
*	Determine if dividing x by y produces a remainder
*	Return true if there is no remainder
*/
bool isMultiple(int x, int y)
{
	return x % y == 0;
}// end isMultiple

