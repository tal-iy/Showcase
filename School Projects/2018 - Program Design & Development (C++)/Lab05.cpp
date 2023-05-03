/********************************************************************** 
* Program Name:   Phone Call
* Author:         Vitaliy Shydlonok 
* Date:           October 1
* Course/Section: CSC 110-301W
* Program Description: 
*	This program will prompt the user for a number of minutes. Then it
*	will determine and display a fee based on the number of minutes.
**********************************************************************/
/********************************************************************** 
* 
* Inputs:
*	Minutes: int minutes
*
* Outputs:
*	Fee: float fee
*
* Formulas:
*	fee = 0.99
*	fee = fee+((minutes-10)*0.1)
*
* Pseudocode: 
*	Initial Algorithm 
*	------- 
*		Get total minutes used
*		Calculate the fee
*		Display minutes used and the fee
* 
*	Refined Algorithm 
*	------- 
* 		Clear screen 
*		Display "Enter number of minutes: " 
*		Input minutes
*		IF minutes equals zero THEN
*			Display "ERROR: No minutes were entered!"
*		ELSE
*			Start the fee at 0.99 for the first 10 minutes
*				fee = 0.99
*			IF minutes is greater than 10 THEN
*				Add 0.10 to the fee for every minute over 10
*					fee = fee+((minutes-10)*0.1)
*			END IF
*			Display "For "
*			Display minutes
*			Display " minutes, you owe $"
*			Display fee
*			Display " in fees."
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
	int minutes = 0;
	float fee = 0;

	/*************** Begin main Function Executables *****************/ 
	
	//Clear the screen 
	system("cls"); 
	
	//Get the number of minutes
	cout << "Enter number of minutes: ";
	cin >> minutes;
	
	//Check if minutes equals 0
	if (minutes == 0)
	{
		//Display error
		cout << "ERROR: No minutes were entered!" << endl;
	}
	else
	{
		//Start the fee at 0.99 for the first 10 minutes
		fee = 0.99;
		
		//Check if minutes is over 10
		if (minutes > 10)
		{
			//Add 0.10 to the fee for every minute over 10
			fee = fee+((minutes-10)*0.10);
		}

		//Display the minutes and fee
		cout << "For " << minutes << " minutes, you owe $" << fixed << showpoint << setprecision(2) << fee << " in fees." << endl;
	}
	
	//Pause to read output 
	system ("pause"); 
	
	//Indicate to OS successful termination of program 
	return 0; 
}	//End main
