/*
	Vitaliy Shydlonok
*/
#pragma once

#include <iostream>
#include <fstream>
#include <string>

using namespace std;

void AddItem(fstream &file);
void DisplayItem(fstream &file);
void EditItem(fstream &file);
	char name[21] = "                    ";