#include "Ship.h"

Ship::Ship()
{
	this->name = "";
	this->year = 0;
}

Ship::Ship(string name, int year)
{
	this->name = name;
	this->year = year;
}

string Ship::GetName() const
{
	return name;
}

int Ship::GetYear() const
{
	return year;
}

void Ship::SetName(string name)
{
	this->name = name;
}

void Ship::SetYear(int year)
{
	this->year = year;
}

bool Ship::operator>(Ship other)
{
	return year < other.GetYear();
}

ostream &operator << (ostream &strm, const Ship &obj)
{
	return strm << "Ship name: " << obj.GetName() << endl
				<< "Ship year: " << obj.GetYear() << endl;
}