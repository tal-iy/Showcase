/********************************************************************** 
* Program Name:   Shydlonok_Lab01
* Author:         Vitaliy Shydlonok 
* Date:           August 27
* Course/Section: CSC 110-301W
* Program Description: 
*    This program will prompt the User for a nickname and display messages 
**********************************************************************/
/********************************************************************** 
* 
* Pseudocode: 
*    Initial Algorithm 
*    ------- 
*        Prompt User for nickname  
*        Display message 
* 
*    Refined Algorithm 
*    ------- 
*        Prompt User for nickname 
*        Clear screen 
*        Display "Hello World" 
*        Display User's nickname and welcoming message 
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
    //Local variables 
    string nickName;  //name of User 

    /*************** Begin main Function Executables *****************/ 

    //Clear the screen 
    system("cls"); 
    
    //Prompt User for name 
    cout << "Please enter your nick name: "; 
    cin >> nickName; 

    //Display message 
    cout << "Hello World" << endl; 
    cout << "Hello " << nickName << ".  " << "Welcome to CSC 110"; 
    cout << "\n\n"; 

    //Pause to read output 
   system ("pause"); 

    //Indicate to OS successful termination of program 
    return 0; 
}   //End main
