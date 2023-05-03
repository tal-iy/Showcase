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
void EditItem(fstream &file);int ShowMenu();struct Record{
	char name[21] = "                    ";	int quantity;	double wholesale;	double retail;};