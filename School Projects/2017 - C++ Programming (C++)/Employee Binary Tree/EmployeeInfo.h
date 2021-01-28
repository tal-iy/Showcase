#pragma once
#include <string>
#include <iostream>

using namespace std;

class EmployeeInfo
{
	private:
		int empID;
		string empName;

	public:

		EmployeeInfo(int ID=0, string name="");

		// Overloaded operators
		bool operator<(EmployeeInfo &emp) const;
		bool operator>(EmployeeInfo &emp) const;
		bool operator==(EmployeeInfo &emp) const;
		friend ostream &operator << (ostream &strm, const EmployeeInfo &obj);

		// Getters and Setters
		int GetID() const;
		void SetID(int ID);
		string GetName() const;
		void SetName(string name);
};