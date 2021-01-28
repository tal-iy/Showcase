#pragma once
#include <string>

using namespace std;

class Person
{
	private: 
		string name;
	public:
		Person();
		Person(string name);
		string GetName() const;
		void SetName(string name);
};