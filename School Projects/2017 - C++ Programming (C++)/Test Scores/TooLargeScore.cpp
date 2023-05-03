/*
	Vitaliy Shydlonok
*/
#include "TooLargeScore.h"

/*
	Initializes exception message
*/
TooLargeScore::TooLargeScore(string msg)
{
	message = msg;
}

/*
	Returns exception message
*/
string TooLargeScore::GetMessage() const
{
	return message;
}