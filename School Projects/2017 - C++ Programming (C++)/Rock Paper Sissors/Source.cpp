/*
	Vitaliy Shydlonok
*/

#include "RockPaperSissors.h"

/*
	Entry point, contains the game loop
*/
int main()
{
	bool exit = false;

	//loop the game until the user chooses to exit
	while (!exit)
	{
		//prompt the user for a choice
		int user = GetUserChoice();

		//generate a random choice for the computer
		int comp = GetComputerChoice();

		//display the computers choice
		cout << "\nThe Computer chose: ";
		DisplayChoice(comp);

		//display the winner and ask to play again if there is no tie
		if (DetermineWinner(user, comp))
		{
			//ask the user to continue playing
			char answer;
			cout << "\n\nWould you like to play again? (Y/N)\n>> ";
			cin >> answer;
			cout << endl;

			//exit the loop if the user chose not to play again
			if (answer == 'N' || answer == 'n')
				exit = true;
		}
		else
		{
			//cleans up the output to a new line
			cout << endl << endl;
		}
	}

	return 0;
}