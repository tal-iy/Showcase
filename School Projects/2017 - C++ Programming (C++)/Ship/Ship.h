#pragma once
#include <string>
#include <iostream>

using namespace std;

class Ship
{
	private:
		string name;
		int year;

	public:
		Ship();
		Ship(string name, int year);
		string GetName() const;
		int GetYear() const;
		void SetName(string name);
		void SetYear(int year);

		bool operator > (Ship other);
		friend ostream &operator << (ostream &strm, const Ship &obj);
};