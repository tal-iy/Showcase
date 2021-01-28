/********************************************************************** 
* Program Name:   Shydlonok_Prog01
* Author:         Vitaliy Shydlonok 
* Date:           September 17
* Course/Section: CSC 110-301W
* Program Description: 
*	This program will prompt the user for their weight and height,
*	calculate their BMI, and print out the BMI.
**********************************************************************/
/********************************************************************** 
* 
* Inputs:
*	User's weight: float userWeight
*	User's height, inches: float userHeightIn
*	User's height, feet: int userHeightFt
*
* Outputs:
*	User's BMI: float userBMI
*
* Formulas:
*	userHeightTotal = (userHeightFt * 12) + userHeightIn
*	userBMI = (userWeight * 703) / (userHeightTotal * userHeightTotal)
*
* Pseudocode: 
*	Initial Algorithm 
*	------- 
*		Get user's weight
*		Get user's height, feet portion
*		Get user's height, inches portion
*		Calculate user's total height in inches
*		Calculate user's BMI
*		Display user's BMI
*		Display BMI chart
* 
*	Refined Algorithm 
*	------- 
* 		Clear screen 
*		Display "Enter your weight: " 
*		Input user's weight
*		Display "Enter your height in feet and inches: "
*		Display "Feet: "
*		Input user's height, feet portion
*		Display "Inches: "
*		Input user's height, inches portion
*		Calculate user's total height in inches
*			userHeightTotal = (userHeightFt * 12) + userHeightIn
*		Calculate user's BMI
*			userBMI = (userWeight * 703) / (userHeightTotal * userHeightTotal)
*		Display "Your BMI is: "
*		Display user's BMI
*		Display "\nBMI          Weight Status\n---          -------------\nBelow  18.5    Underweight\n18.5 - 24.9    Normal\n25.0 - 29.9    Overweight\n30  &  Above   Obese\n\n"
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
	float userWeight = 0;
	float userHeightIn = 0;
	int userHeightFt = 0;
	float userHeightTotal = 0;
	float userBMI = 0;

	/*************** Begin main Function Executables *****************/ 
	
	//Clear the screen 
	system("cls"); 
	
	//Get user's weight
	cout << "Enter your weight: ";
	cin >> userWeight;
	
	//Get user's height
	cout << "Enter your height in feet and inches: " << endl;
	cout << "  Feet: ";
	cin >> userHeightFt;
	cout << "  Inches: ";
	cin >> userHeightIn;
	
	//Calculate user's total height in inches
	userHeightTotal = (userHeightFt * 12) + userHeightIn;
	
	//Calculate user's BMI
	userBMI = (userWeight * 703) / (userHeightTotal * userHeightTotal);
	
	//Display user's BMI
	cout << "Your BMI is: " << fixed << showpoint << setprecision(2) << userBMI << endl;
	
	//Display BMI chart
	cout << "\nBMI          Weight Status\n---          -------------\nBelow  18.5    Underweight\n18.5 - 24.9    Normal\n25.0 - 29.9    Overweight\n30  &  Above   Obese\n\n";
	
	//Pause to read output 
	system ("pause"); 
	
	//Indicate to OS successful termination of program 
	return 0; 
}	//End main
