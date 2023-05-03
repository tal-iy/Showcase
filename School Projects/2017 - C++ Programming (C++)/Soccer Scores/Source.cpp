/*
	Vitaliy Shydlonok
*/

#include "SoccerScores.h"

/*
	Program prompts for a name, jersey, and score for a number
		of soccer players. Then displays the data in a table.
*/
int main()
{
	Player team[PLAYERS];

	//prompt for player info
	for (int i = 0; i < PLAYERS; i++)
		GetPlayerInfo(team[i]);

	//print a table header
	cout << "\n" << setw(SPACING) << "Name" << setw(SPACING) << "Jersey" << setw(SPACING) << "Score" << "\n";

	//print player info for every player
	for (int i = 0; i < PLAYERS; i++)
		ShowInfo(team[i]);

	//print teams total
	cout << "\nTeam total: " << GetTotalPoints(team, PLAYERS) << " points\n";

	//print highest scoring player
	ShowHighest(team, PLAYERS);

	string pause;
	getline(cin, pause);
	return 0;
}