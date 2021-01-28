/********************************************************************** 
* Program Name:   Looping Lab with both types of loops
* Author:         Vitaliy Shydlonok 
* Date:           October 15
* Course/Section: CSC 110-301W
* Program Description: 
*	This program will repeatedly prompt the user for a number between 
*	0 and 100 until a valid number is entered. Then display that a
*	valid number was entered.
**********************************************************************/
/********************************************************************** 
* 
* Inputs:
*	Score: int score
*
* Outputs: none
*
* Additional Data: none
*
* Formulas: none
*
* Pseudocode: 
*	Initial Algorithm 
*	------- 
*		Repeatedly prompt for a score until a valid one is entered
*		Display a message saying the score was valid
* 
*	Refined Algorithm 1
*	------- 
* 		Clear screen
*		Display "Enter a score between 0 and 100 (inclusive): " 
*		Input score
*		WHILE score is less than 0 or greater than 100
*			Display "Enter a score between 0 and 100 (inclusive): "
*			Input score
*		END WHILE
*		Display "The score you entered is valid!"

*	Refined Algorithm 2
*	------- 
*		Clear screen
*		DO
*			Display "Enter a score between 0 and 100 (inclusive): "
*			Input score
*		WHILE score is less than 0 or greater than 100
*		Display "The score you entered is valid!"
*
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
	int score = 0;

	/************************ The While Loop *************************/ 
	
	//Clear the screen 
	system("cls"); 
	
	//Prompt for a score between 0 and 100
	cout << "Enter a score between 0 and 100 (inclusive): ";
	cin >> score;
	
	//Loop until a valid score is entered
	while (score < 0 || score > 100) 
	{
		//Prompt for a score between 0 and 100
		cout << "Enter a score between 0 and 100 (inclusive): ";
		cin >> score;
	}
	
	//Notify the user that the score was valid
	cout << "The score you entered is valid!" << endl;
	
	//Pause to read output 
	system ("pause"); 
	
	/********************** The Do While Loop ************************/ 
	
	//Clear the screen 
	system("cls"); 
	
	//Loop until a valid score is entered
	do
	{
		//Prompt for a score between 0 and 100
		cout << "Enter a score between 0 and 100 (inclusive): ";
		cin >> score;
	}
	while (score < 0 || score > 100);
	
	//Notify the user that the score was valid
	cout << "The score you entered is valid!" << endl;
	
	//Pause to read output 
	system ("pause"); 
	
	//Indicate to OS successful termination of program 
	return 0; 
}	//End main
