/*
	Vitaliy Shydlonok
*/
#pragma once
#include <string>
using namespace std;

/*
	Exception to be thrown when a score is greater than 100
*/
class TooLargeScore
{
	private:
		string message;

	public:
		TooLargeScore(string msg);
		string GetMessage() const;
};