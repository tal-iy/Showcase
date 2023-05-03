/********************************************************************** 
* Program Name:   Shydlonok_Lab03
* Author:         Vitaliy Shydlonok 
* Date:           September 17
* Course/Section: CSC 110-301W
* Program Description: 
*	This program will prompt the user for the width and height of a triangle,
*	calculate the area of the triangle, and print out the area.
**********************************************************************/
/********************************************************************** 
* 
* Inputs:
*	Base of triangle: float triBase
*	Height of triangle: float triHeight
*
* Outputs:
*	Area of triangle: float triArea
*
* Formulas:
*	triArea = (triBase * triHeight) / 2
*
* Pseudocode: 
*	Initial Algorithm 
*	------- 
*		Get triangle width
*		Get triangle height
*		Calculate triangle area
*		Display triangle area
* 
*	Refined Algorithm 
*	------- 
* 		Clear screen 
*		Display "Enter triangle width: " 
*		Input triangle width
*		Display "Enter triangle height: "
*		Input triangle height
*		Calculate triangle area
*			triArea = (triBase * triHeight) / 2
*		Display "The triangle area is: "
*		Display triangle area
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
	float triBase = 0;
	float triHeight = 0;
	float triArea = 0;
	const int two = 2;

	/*************** Begin main Function Executables *****************/ 
	
	//Clear the screen 
	system("cls"); 
	
	//Get triangle width
	cout << "Enter triangle width: ";
	cin >> triBase;
	
	//Get triangle height
	cout << "Enter triangle height: ";
	cin >> triHeight;
	
	//Calculate triangle area
	triArea = (triBase * triHeight) / two;
	
	//Display triangle area
	cout << "The triangle area is: " << triArea << endl;
	
	//Pause to read output 
	system ("pause"); 
	
	//Indicate to OS successful termination of program 
	return 0; 
}	//End main
