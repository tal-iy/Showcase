/********************************************************************** 
* Program Name:   Shydlonok_Lab02
* Author:         Vitaliy Shydlonok 
* Date:           September 10
* Course/Section: CSC 110-301W
* Program Description: 
*    This program will display my name, title and author of the last book I read, and my favorite animal.
**********************************************************************/
/********************************************************************** 
* 
* Pseudocode: 
*    Initial Algorithm 
*    ------- 
*        Display name
*		 Display title and author
*		 Display favorite animal 
* 
*    Refined Algorithm 
*    ------- 
*        Clear screen 
*        Display "My name is Vitaliy Shydlonok." 
*        Display "The last book I read is Techne by Crystal L. Etzel"
*        Display "My favorite animal is the tiger"
**********************************************************************/ 

/************************** Compiler Directives **********************/ 

#include <cstdlib> 
#include <iostream> 
#include <string> 

using namespace std; 

/*********************** Global Data Declarations ********************/ 

//None in this program. 

int main() 
{ 
    /*************** Begin main Function Executables *****************/ 

    //Clear the screen 
    system("cls"); 
    
    //Display name 
    cout << "My name is Vitaliy Shydlonok." << endl;
    
    //Display title and author
    cout << "The last book I read is Techne by Crystal L. Etzel." << endl;
    
    //Display favorite animal
    cout << "My favorite animal is the tiger." << endl;

    //Pause to read output 
    system ("pause"); 

    //Indicate to OS successful termination of program 
    return 0; 
}   //End main
