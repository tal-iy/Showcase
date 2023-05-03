/*
	Vitaliy Shydlonok
*/
#pragma once
#include <string>
using namespace std;

/*
	Exception to be thrown when a score is less than 0
*/
class NegativeScore
{
	private:
		string message;

	public:
		NegativeScore(string msg);
		string GetMessage() const;
};