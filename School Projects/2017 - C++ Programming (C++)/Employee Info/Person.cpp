#include "Person.h"

Person::Person()
{
	SetName("");
}

Person::Person(string name)
{
	SetName(name);
}

string Person::GetName() const
{
	return name;
}

void Person::SetName(string name)
{
	this->name = name;
}