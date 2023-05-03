/*
	Vitaliy Shydlonok
*/
#include "NegativeScore.h"

/*
	Initializes exception message
*/
NegativeScore::NegativeScore(string msg)
{
	message = msg;
}

/*
	Returns exception message
*/
string NegativeScore::GetMessage() const
{
	return message;
}