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


#include <cstdlib>
#include <iomanip>
#include <iostream> 
#include <string> 

using namespace std; 

/*********************** Global Data Declarations ********************/ 
void Pancake(int  x, int& y, int  z); 
void Waffle(int& x,int& y); 
int Doughnut(int y,  int z); 
//None in this program. 
 
int main() 
{ 
	/************************ Local Variables ************************/


	/*************** Begin main Function Executables *****************/ 
	
	//Clear the screen 
	system("cls"); 

    int a = 1;
    int b = 2;
    int c = 3;
    int d = 4;

    Pancake(a, b, c);
    

    Waffle(b, c);
    
    
    Pancake(d, c, b);
    
    
    d = Doughnut(b, a);
    
    cout << d;

	//Pause to read output 
	system ("pause"); 
	
	//Indicate to OS successful termination of program 
	return 0; 
}	//End main

void Pancake(int  x, int& y, int  z)
{
    y += 3;
    z =  x + y;
} 

void Waffle(int& x,int& y)
{
    int temp; 
    temp = x;
    x    = y;
    y    = temp;
}

int Doughnut(int y,  int z)
{
    int w;    
    w = y * 2;
    z = w + 3;
    w --; 
    return w;
}
