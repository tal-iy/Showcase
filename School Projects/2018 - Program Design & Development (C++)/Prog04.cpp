/********************************************************************** 
* Program Name:   Prog 4
* Author:         Vitaliy Shydlonok 
* Date:           November 5
* Course/Section: CSC 110-301W
* Program Description: 
*	This program will ask for a begining and ending times using military
*	format, then will determine and print the total elapsed minutes between 
*	the ending time and the begining time.
**********************************************************************/
/********************************************************************** 
* 
* Inputs:
*	Begining time: int begining
*	Ending time: int ending
*
* Outputs:
*	Elapsed time in minutes: int elapsed
*
* Formulas:
*	hours = ((ending - (ending % 100)) - (begining - (begining % 100))) / 100
*	minutes = (ending % 100) - (begining % 100)
*	totalMinutes = (hours * 60) + minutes
*
* Pseudocode: 
*	Initial Algorithm 
*	------- 
*		Prompt for the ending and begining times
*		Error check the inputs
*		Determine the elapsed time
*		Print the elapsed time
*		Ask to run again
* 
*	Refined Algorithm
*	------- 
*		DO
*	 		Clear the screen
*			Prompt for the begining time
*			Prompt for the ending time
*			Use isEndingGreater() to check IF the ending time is greater than the begining time THEN
*				Use elapsedTime() to determine the time elapsed in minutes
*				Print the time elapsed
*			ELSE
*				Print "Error: the ending time must be greater than the begining time!"
*			END IF
*			Prompt to run the program again
*		WHILE user enters 'Y' to run again
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
bool isEndingGreater(int ending, int begining);
int elapsedTime(int ending, int begining);

int main() 
{ 
	/************************ Local Variables ************************/
	int begining = 0;
	int ending = 0;
	int elapsed = 0;
	char again = ' ';

	do
	{
		//clear the screen
		system("cls"); 
		
		//prompt for the begining time
		cout << "Enter the begining time using military time and in this format (HHMM): ";
		cin >> begining;
		
		//prompt for the ending time
		cout << "Enter the ending time using military time and in this format (HHMM): ";
		cin >> ending;
		
		//error check the input values to make sure the ending time is greater than the begining time
		if (isEndingGreater(ending, begining))
		{
			//determine the time elapsed in minutes
			elapsed = elapsedTime(ending, begining);
			
			//print the time elapsed
			cout << "The elapsed time is: " << elapsed << " minutes" << endl;
		}
		else
		{
			//tell the user that the times entered were invalid
			cout << "Error: the ending time must be greater than the begining time!" << endl;
		}
		
		//prompt to run the program again
		cout << endl << "Would you like to run this program again? (Y/N): ";
		cin >> again;
	
	} while(again == 'Y'); //loop as long as the user enters 'Y' to run again
	
	//Pause to read output 
	system ("pause"); 
	
	//Indicate to OS successful termination of program 
	return 0; 
}	//End main

/*
* Name: isEndingGreater
* Author: Vitaliy Shydlonok
* Date: 11/5/18
* Description:
*	Returns true if the ending time is greater than the
*	begining time, false otherwise.
*
* Pseudocode:
*	Deternine whether ending is greater than begining
*	Return true if ending is greater or false if not
*/
bool isEndingGreater(int ending, int begining)
{
	bool result = false;
	if (ending > begining)
		result = true;
	return result;
}

/*
* Name: elapsedTime
* Author: Vitaliy Shydlonok
* Date: 11/5/18
* Description:
*	Returns the total minutes elapsed between a begining and an
*	ending time.
*
* Pseudocode:
*	Determine the total elapsed minutes between ending and begining
*	Return the total elapsed minutes
*/
int elapsedTime(int ending, int begining)
{
	/************************ Local Variables ************************/
	int hours = 0;
	int minutes = 0;
	int totalMinutes = 0;
	
	//determine the difference in hours
	hours = ((ending - (ending % 100)) - (begining - (begining % 100))) / 100;
	
	//determine the difference in minutes
	minutes = (ending % 100) - (begining % 100);
	
	//convert the hours into minutes and combine it with the minute difference
	totalMinutes = (hours * 60) + minutes;
	
	//return the total minutes elapsed
	return totalMinutes;
}
