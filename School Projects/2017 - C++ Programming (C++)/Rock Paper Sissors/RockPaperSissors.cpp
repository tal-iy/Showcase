/*
	Vitaliy Shydlonok
*/

#include "RockPaperSissors.h"

/*
	Displays either Rock, Paper, or Sissors on the console based on (int) choice
*/
void DisplayChoice(int choice)
{
	switch (choice)
	{
	case(1):
		cout << "Rock";
		break;
	case(2):
		cout << "Paper";
		break;
	case(3):
		cout << "Sissors";
		break;
	}
}

/*
	Asks the user to input a number 1 to 3 and returns it
*/
int GetUserChoice()
{
	//ask for a menu choice
	int choice = 0;
	cout << "Choose one of the following numbers to play:\n1. Rock\n2. Paper\n3. Sissors\n>> ";
	cin >> choice;
	cout << endl;

	//make sure that the chosen number is valid
	if (choice == 1 || choice == 2 || choice == 3)
	{
		cout << "You chose: ";
		DisplayChoice(choice);
	}
	else
	{
		//use recursion to ask again until a valid value is entered
		cout << "\nInvalid Choice!\n";
		choice = GetUserChoice();
	}

	return choice;
}

/*
	Generates a random number 1 to 3 and returns it
*/
int GetComputerChoice()
{
	//give the random number generator a seed
	srand((unsigned int)time(0));
	
	//use (rand() % (a - b)) + a to get a value between 1 and 3 inclusively
	int choice = (rand() % 3)+1;

	return choice;
}

/*
	Takes two choice inputs, plays Rock-Paper-Sissors using those inputs,
	and displays which choice wins
*/
bool DetermineWinner(int userChoice, int compChoice)
{
	bool winner = false;

	//"if tree" to account for every choice combination

	if (userChoice == compChoice)
	{
		cout << "\nIt's a tie!";
	}
	else if (userChoice == 1)
	{
		if (compChoice == 2)
		{
			cout << "\nPaper beats Rock, Computer wins!";
			winner = true;
		}
		else if (compChoice == 3)
		{
			cout << "\nRock beats Sissors, You win!";
			winner = true;
		}
	}
	else if (userChoice == 2)
	{
		if (compChoice == 1)
		{
			cout << "\nPaper beats Rock, You win!";
			winner = true;
		}
		else if (compChoice == 3)
		{
			cout << "\nSissors beats Paper, Computer wins!";
			winner = true;
		}
	}
	else if (userChoice == 3)
	{
		if (compChoice == 1)
		{
			cout << "\nRock beats Sissors, Computer wins!";
			winner = true;
		}
		else if (compChoice == 2)
		{
			cout << "\nSissors beats Paper, You win!";
			winner = true;
		}
	}

	return winner;
}