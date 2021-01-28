#pragma once
#include <string>
#include <iostream>
#include <iomanip>
#include "Person.h"

using namespace std;

class EmployeeInfo
{
	private:
		Person person;
		int idNumber;
		string department;
		string position;

	public:
		//EmployeeInfo();
		//EmployeeInfo(string name = " ", int id = -1);
		EmployeeInfo(string name = " ", int id = -1, string dep = " ", string pos = " ");
		~EmployeeInfo();
		void SetName(string name);
		void SetIdNumber(int id);
		void SetDepartment(string dep);
		void SetPosition(string pos);
		string GetName() const;
		int GetIdNumber() const;
		string GetDepartment() const;
		string GetPosition() const;
};