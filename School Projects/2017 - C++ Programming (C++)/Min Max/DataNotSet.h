#pragma once
#include <string>

using namespace std;

class DataNotSet
{
	private:
		string msg;
	public:
		DataNotSet(string msg);
		string GetMessage();
};