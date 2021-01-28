#pragma once

#include <iostream>
#include <iomanip>
#include <string>

using namespace std;

const int PLAYERS = 12;
const int SPACING = 10;

//player information structure
struct Player
{
	string name;
	int jersey;
	int score;
};

void GetPlayerInfo(Player& person);

void ShowInfo(const Player& person);

int GetTotalPoints(const Player team[], int size);

void ShowHighest(const Player team[], int size);