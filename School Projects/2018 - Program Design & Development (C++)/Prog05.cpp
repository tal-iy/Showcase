/********************************************************************** 
* Program Name:   Prog 5
* Author:         Vitaliy Shydlonok 
* Date:           November 19
* Course/Section: CSC 110-301W
* Program Description: 
*	This program will ask for a height in feet and inches. Then will
*	convert the height to meters and print it.
**********************************************************************/
/********************************************************************** 
* 
* Inputs:
*	Feet: int feet
*	Inches: float inches
*
* Outputs:
*	Height in meters: float meters
*
* Formulas:
*	meters = (feet*0.3048) + (inches*0.0254)
*
* Pseudocode: 
*	Initial Algorithm 
*	------- 
*		Prompt for the height in feet and inches
*		Convert the height to meters
*		Print the height
*		Ask to run again
* 
*	Refined Algorithm
*	------- 
*		DO
*	 		Clear the screen
*			Use promptInput to get the height in feet and inches
*			Use convertToMetric to convert the feet and inches to meters
*			Use printOutput to print the height in meters
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

//function prototypes
void promptInput(int& feet, float& inches);
float convertToMetric(int feet, float inches);
void printOutput(float meters);

int main() 
{ 
	/************************ Local Variables ************************/
	int feetMain = 0;
	float inchesMain = 0.0;
	float metersMain = 0.0;
	char again = ' ';

	/************************ The While Loop *************************/ 
	do
	{
		//Clear the screen
		system("cls"); 
		
		//Get the input
		promptInput(feetMain, inchesMain);
		
		//Convert feet and inches to metric
		metersMain = convertToMetric(feetMain, inchesMain);
		
		//Print the resulting height value
		printOutput(metersMain);
		
		//Prompt to run the program again
		cout << endl << "Would you like to run this program again? (Y/N): ";
		cin >> again;
	
	} while(again == 'Y' || again == 'y'); //Loop as long as the user enters 'Y' to run again
	
	//Pause to read output 
	system ("pause"); 
	
	//Indicate to OS successful termination of program 
	return 0; 
}	//End main

/*
* Name: promptInput
* Author: Vitaliy Shydlonok
* Date: 11/19/18
* Description:
*	Prompts for a height in feet and inches.
*
* Pseudocode:
*	Prompt for the feet
*	Prompt for the inches
*/
void promptInput(int& feet, float& inches)
{
	//Prompt for the feet
	cout << "Enter feet portion of the height: ";
	cin >> feet;
	
	//Prompt for the inches
	cout << "Enter the inches portion of the height: ";
	cin >> inches;
}

/*
* Name: convertToMetric
* Author: Vitaliy Shydlonok
* Date: 11/19/18
* Description:
*	Returns a height in meters.
*
* Pseudocode:
*	Convert feet and inches to meters
*	Return the result
*/
float convertToMetric(int feet, float inches)
{
	//Initialize meters to 0
	float meters = 0;
	
	//Convert feet and inches to meters
	meters = (feet*0.3048) + (inches*0.0254);
	
	//Return the result
	return meters;
}

/*
* Name: printOutput
* Author: Vitaliy Shydlonok
* Date: 11/19/18
* Description:
*	Prints a height value in meters.
*
* Pseudocode:
*	Print the converted height
*
*/
void printOutput(float meters)
{
	//Print the converted height
	cout << "The height in metric is " << meters << " meters." << endl;
}
