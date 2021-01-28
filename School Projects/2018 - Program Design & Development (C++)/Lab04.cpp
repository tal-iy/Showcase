/********************************************************************** 
* Program Name:   Shydlonok_Lab04
* Author:         Vitaliy Shydlonok 
* Date:           September 24
* Course/Section: CSC 110-301W
* Program Description: 
*	This program will prompt the user for the number of minutes parked
*	in a parking garage, convert the time to hours and minutes,
*	calculate the parking fee, and display the time and parking fee.
**********************************************************************/
/********************************************************************** 
* 
* Inputs:
*	Total minutes parked: int totalMinutes
*
* Outputs:
*	Hours parked: int hoursParked
*	Leftover minutes parked: int minutesParked
*	Parking fee: float parkingFee
*
* Formulas:
*	hoursParked = totalMinutes / 60
*	minutesParked = totalMinutes % 60
*	parkingFee = hoursParked * 1.00
*	parkingFee = parkingFee + 1.00
*
* Pseudocode: 
*	Initial Algorithm 
*	------- 
*		Get total minutes parked
*		Calculate time parked in hours and minutes
*		Calculate parking fee
*		Display time parked
*		Display parking fee
* 
*	Refined Algorithm 
*	------- 
* 		Clear screen 
*		Display "Enter number of minutes the car was parked: " 
*		Input totalMinutes
*		IF totalMinutes equals zero THEN
*			Display "ERROR: The number of minutes is invalid!"
*		ELSE
*			Compute total hours parked
*				hoursParked = totalMinutes / 60
*			Compute leftover minutes parked
*				minutesParked = totalMinutes % 60
*			Compute the parking fee
*				parkingFee = hoursParked * 1.00
*			IF minutesParked is greater than 0 THEN
*				parkingFee = parkingFee + 1.00
*			END IF
*			Display "The car was parked for "
*			Display total hours parked
*			Display " hours and "
*			Display leftover minutes parked
*			Display " minutes."
*			Display "You owe $"
*			Display parking fee
*			Display " in parking fees."
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
	int totalMinutes = 0;
	int hoursParked = 0;
	int minutesParked = 0;
	float parkingFee = 0;

	/*************** Begin main Function Executables *****************/ 
	
	//Clear the screen 
	system("cls"); 
	
	//Get triangle width
	cout << "Enter number of minutes the car was parked: ";
	cin >> totalMinutes;
	
	//Check if totalMinutes equals 0
	if (totalMinutes == 0)
	{
		//Display error
		cout << "ERROR: The number of minutes entered is invalid!" << endl;
	}
	else
	{
		//Compute total hours parked
		hoursParked = totalMinutes / 60;
		
		//Compute leftover minutes parked
		minutesParked = totalMinutes % 60;
		
		//Compute the parking fee
		parkingFee = hoursParked * 1.00;
		
		//Check if minutesParked is greater than 0 
		if (minutesParked > 0)
		{
			//Add an hour to the parking fee
			parkingFee = parkingFee + 1.00;
		}
			
		//Display the hours and minutes parked
		cout << "The car was parked for " << hoursParked << " hours and " << minutesParked << " minutes." << endl;
		
		//Display the parking fee
		cout << "You owe $" << fixed << showpoint << setprecision(2) << parkingFee << " in parking fees." << endl;
	}
	
	//Pause to read output 
	system ("pause"); 
	
	//Indicate to OS successful termination of program 
	return 0; 
}	//End main
