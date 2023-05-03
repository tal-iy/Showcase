/********************************************************************** 
* Program Name:   Lab 8
* Author:         Vitaliy Shydlonok 
* Date:           November 5
* Course/Section: CSC 110-301W
* Program Description: 
*	This program will print out the initials "V", "X", and "S" as
*	capital block letters made out of "*" characters.
**********************************************************************/
/********************************************************************** 
* 
* Inputs: none
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
*		Print the initials
* 
*	Refined Algorithm
*	------- 
* 		Print the first initial
*		Print a new line
*		Print the second initial
*		Print a new line
*		Print the third initial
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
void printFirstInitial();
void printSecondInitial();
void printThirdInitial();

int main()
{
	printFirstInitial();
	
	cout << endl;
	
	printSecondInitial();
	
	cout << endl;
	
	printThirdInitial();
	
	system("pause");
	return 0; 
}//end main

/*
* Name: printFirstInitial
* Author: Vitaliy Shydlonok
* Date: 11/5/18
* Description:
*	Prints the first name initial, "V" as a capital block letter made out of stars.
*
* Pseudocode:
*	Print the letter "V" using stars.
*/
void printFirstInitial()
{
	cout << "**      **" << endl;
	cout << "**      **" << endl;
	cout << "**      **" << endl;
	cout << " **    ** " << endl;
	cout << " **    ** " << endl;
	cout << " **    ** " << endl;
	cout << "  **  **  " << endl;
	cout << "   ****   " << endl;
	cout << "    **    " << endl;
}// end printFirstInitial

/*
* Name: printSecondInitial
* Author: Vitaliy Shydlonok
* Date: 11/5/18
* Description:
*	Prints the middle name initial, "X" as a capital block letter made out of stars.
*
* Pseudocode:
*	Print the letter "X" using stars.
*/
void printSecondInitial()
{
	cout << "**      **" << endl;
	cout << "**      **" << endl;
	cout << " **    ** " << endl;
	cout << "  **  **  " << endl;
	cout << "   ****   " << endl;
	cout << "  **  **  " << endl;
	cout << " **    ** " << endl;
	cout << "**      **" << endl;
	cout << "**      **" << endl;
}// end printSecondInitial

/*
* Name: printThirdInitial
* Author: Vitaliy Shydlonok
* Date: 11/5/18
* Description:
*	Prints the last name initial, "S" as a capital block letter made out of stars.
*
* Pseudocode:
*	Print the letter "S" using stars.
*/
void printThirdInitial()
{
	cout << "  ********" << endl;
	cout << "**********" << endl;
	cout << "***       " << endl;
	cout << "****       " << endl;
	cout << " ******** " << endl;
	cout << "      ****" << endl;
	cout << "       ***" << endl;
	cout << "********** " << endl;
	cout << "********  " << endl;
}// end printThirdInitial
