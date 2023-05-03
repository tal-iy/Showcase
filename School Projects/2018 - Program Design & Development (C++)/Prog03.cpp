/********************************************************************** 
* Program Name:   Conversion Chart
* Author:         Vitaliy Shydlonok 
* Date:           October 22
* Course/Section: CSC 110-301W
* Program Description: 
*	This program will prompt the user for a range for a conversion
*	table to display. Then will display a table of inches converted
*	to centimeters using that range and in increments of 6. Will also
*	make sure that the entered range is valid, where the ending value
*	is more than 6 inches greater than the begining value, and the
*	ending value is no more than 36 inches greater than the begining
*	value.
**********************************************************************/
/********************************************************************** 
* 
* Inputs:
*	Start of the range: int begining
*	End of the range: int ending
*	Run again: char again
*
* Outputs:
*	Inches: int counter
*	Centimeter conversion: float conversion
*
* Formulas: 
*	conversion = counter * 2.54
*
* Pseudocode: 
*	Initial Algorithm 
*	------- 
*		Prompt for a range for the table
*		Check for errors in the entered range
*		Display the table titles
*		Display the inches and centimeters in the table
*		Prompt whether to run again
*		Loop if the user answers yes
* 
*	Refined Algorithm 
*	------- 
* 		DO
*			Clear screen 
*			Display "Enter the begining value of the range: " 
*			Input begining
*			Display "Enter the ending value of the range: " 
*			Input ending
*			IF (ending >= begining+6) AND (ending - begining <= 36) THEN
*				Display the table title and column titles
*				FOR counter = begining To ending Step 6
*					Convert inches to centimeters
*						conversion = counter * 2.54
*					Display counter and conversion
*				NEXT counter
*			ELSE
*				Display "That is an invalid range!"
*			END IF
*			Display "Would you like to run the program again? (y/n)"
*			Input an answer
*		WHILE answer is yes
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
	
	int begining = 0;
	int ending = 0;
	int counter = 0;
	float conversion = 0.0;
	char again = ' ';

	/*************** Begin main Function Executables *****************/ 
	
	//Keep looping as long as the user enters a 'y'
	do
	{
		//Clear the screen 
		system("cls"); 
		
		//Prompt for the begining value of the range
		cout << "Enter the begining value of the range: ";
		cin >> begining;
		
		//Prompt for the ending value of the range
		cout << "Enter the ending value of the range: ";
		cin >> ending;
		
		//Error check the ending and begining values
		if ((ending >= begining+6) && (ending - begining <= 36))
		{
			//Print the table title and column titles
			cout << endl;
			cout << "         Conversion Chart" << endl << endl;
			cout << "      Inches         Centimeters" << endl;
			cout << "      ******         ***********" << endl;
			
			//Loop through the range using increments of 6
			for (int counter = begining; counter <= ending; counter += 6)
			{
				//Convert the inches to centimeters
				conversion = counter * 2.54;
				
				//Display the inches and centimeters portion of the table
				cout << "      " << setw(4) << counter << "           " << setw(10) << fixed << showpoint << setprecision(2) << conversion << endl;
			}
		}
		else
		{
			//Display an error message
			cout << "That is an invalid range!" << endl;
		}
		
		//Prompt user whether to run again
		cout << endl << "Would you like to run the program again? (y/n) ";
		cin >> again;
	}
	while (again == 'y');
	
	//Pause to read output 
	system ("pause"); 
	
	//Indicate to OS successful termination of program 
	return 0; 
}	//End main
