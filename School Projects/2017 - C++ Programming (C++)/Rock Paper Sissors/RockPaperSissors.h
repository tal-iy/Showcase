/*
	Vitaliy Shydlonok
*/

#pragma once

#include <iostream>
#include <string>
#include <cstdlib>
#include <ctime>

using namespace std;

void DisplayChoice(int choice);

int GetUserChoice();

int GetComputerChoice();

bool DetermineWinner(int userChoice, int compChoice);