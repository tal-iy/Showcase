#include "NumDays.h"

NumDays::NumDays(int hours)
{
	this->hours = hours;
	days = 0;
	simplify();
}

void NumDays::simplify()
{
	if (hours >= 24)
	{
		days += (hours / 24);
		hours = hours % 24;
	}
	else if (hours < 0)
	{
		days -= ((abs(hours) / 24) + 1);
		hours = 24 - (abs(hours) % 24);
	}
}

void NumDays::SetHours(int hours)
{
	this->hours = hours;
	simplify();
}

void NumDays::SetDays(int days)
{
	this->days = days;
}

double NumDays::GetHours() const
{
	return this->hours;
}

double NumDays::GetDays() const
{
	return this->days;
}

NumDays NumDays::operator+(const NumDays & obj)
{
	NumDays temp(0);

	temp.SetDays(this->days + obj.GetDays());
	temp.SetHours(this->hours + obj.GetHours());
	
	return temp;
}

NumDays NumDays::operator-(const NumDays & obj)
{
	NumDays temp(0);

	temp.SetDays(this->days - obj.GetDays());
	temp.SetHours(this->hours - obj.GetHours());

	return temp;
}

NumDays NumDays::operator++()
{
	this->hours++;
	simplify();
	return *this;
}

NumDays NumDays::operator++(int)
{
	NumDays temp(0);

	temp.SetDays(this->days);
	temp.SetHours(this->hours);

	this->hours++;
	simplify();

	return temp;
}

NumDays NumDays::operator--()
{
	this->hours--;
	simplify();
	return *this;
}

NumDays NumDays::operator--(int)
{
	NumDays temp(0);

	temp.SetDays(this->days);
	temp.SetHours(this->hours);

	this->hours--;
	simplify();

	return temp;
}

ostream &operator << (ostream &strm, const NumDays &obj)
{
	return strm << obj.GetDays() << " days " << obj.GetHours() << " hours";
}