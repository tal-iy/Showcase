#include "SoccerScores.h"

//prompts for and gets a players information and stores it in person
void GetPlayerInfo(Player& person)
{
	cout << "Enter players name: ";
	getline(cin, person.name);
	cout << "Enter " << person.name << "s jersey number: ";
	cin >> person.jersey;
	cout << "Enter points scored by " << person.name << ": ";
	cin >> person.score;
	cin.ignore();
}

//prints a players information in a row
void ShowInfo(const Player& person)
{
	cout << setw(SPACING) << person.name << setw(SPACING) << person.jersey << setw(SPACING) << person.score << "\n";
}

//counts the total points of a team
int GetTotalPoints(const Player team[], int size)
{
	int total = 0;
	for (int i = 0; i < size; i++)
		total += team[i].score;
	return total;
}

//finds the highest scoring player
void ShowHighest(const Player team[], int size)
{
	Player highest = team[0];
	for (int i = 1; i < size; i++)
		if (team[i].score > highest.score)
			highest = team[i];

	cout << "Highest scoring: " << highest.name << " with " << highest.score << " points!\n";
}