/********************************************************************** 
* Program Name:   Counter-Controlled While Loop 
* Author:         Vitaliy Shydlonok 
* Date:           October 8
* Course/Section: CSC 110-301W
* Program Description: 
*	This program will prompt the user for a number of grades to enter. 
*	Then it will ask for a series of grades and calculate the average 
*	of those grades. Then display the average.
**********************************************************************/
/********************************************************************** 
* 
* Inputs:
*	Number of grades: int maxCount
*	Entered grade value: int grade
*
* Outputs:
*	Average grade: int average
*
* Additional Data:
*	Loop counter: int counter
*	Grade running total: int total
*
* Formulas:
*	counter = counter + 1
*	total = total + grade
*	average = total / counter
*
* Pseudocode: 
*	Initial Algorithm 
*	------- 
*		Get total number of grades to enter
*		Get and calculate the total sum of all grades
*		Calculate the average of the grades
*		Display the average of the grades
* 
*	Refined Algorithm 
*	------- 
* 		Clear screen 
*		Initialize counter to 0
*		Initialize total to 0
*		Display "Enter number of grades to enter: " 
*		Input maxCount
*		WHILE counter < maxCount
*			Display "Enter a grade: "
*			Input grade
*			Add grade to the total
*				total = total + grade
*			Increment counter
*				counter = counter + 1
*		END WHILE
*		IF counter equals zero THEN
*			Display "ERROR: No grades were entered!"
*		ELSE
*			Calculate the grade average
*				average = total / counter
*			Display "Your grade average is: "
*			Display average
*		END IF
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
	int maxCount = 0;
	int grade = 0;
	int average = 0;
	int counter = 0;
	int total = 0;

	/*************** Begin main Function Executables *****************/ 
	
	//Clear the screen 
	system("cls"); 
	
	//Get the number of minutes
	cout << "Enter number of grades to enter: ";
	cin >> maxCount;
	
	//Loop maxCount number of times
	while (counter < maxCount) 
	{
		//Prompt for a grade
		cout << "Enter a grade: ";
		cin >> grade;
		
		//Add the grade to the total
		total = total + grade;
		
		//Increment the counter
		counter = counter + 1;
	}
	
	//Check if the counter equals 0
	if (counter == 0)
	{
		//Display error
		cout << "ERROR: No grades were entered!" << endl;
	}
	else
	{
		//Calculate the grade average
		average = total / counter;

		//Display the grade average
		cout << "Your grade average is: " << average << endl;
	}
	
	//Pause to read output 
	system ("pause"); 
	
	//Indicate to OS successful termination of program 
	return 0; 
}	//End main
