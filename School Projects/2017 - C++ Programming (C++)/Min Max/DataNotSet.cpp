#include "DataNotSet.h"

DataNotSet::DataNotSet(string msg)
{
	this->msg = msg;
}

string DataNotSet::GetMessage()
{
	return msg;
}