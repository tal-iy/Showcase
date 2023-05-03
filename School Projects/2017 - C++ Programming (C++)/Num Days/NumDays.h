#pragma once
#include <cmath>
#include <iostream>

using namespace std;

class NumDays
{
private:
	int hours; 
	int days;

	void simplify();

public:
	NumDays(int hours);
	void SetHours(int hours);
	void SetDays(int days);
	double GetHours() const;
	double GetDays() const;

	NumDays operator+(const NumDays& obj);
	NumDays operator-(const NumDays& obj);
	NumDays operator++();
	NumDays operator++(int);
	NumDays operator--();
	NumDays operator--(int);
	friend ostream &operator << (ostream &strm, const NumDays &obj);
};