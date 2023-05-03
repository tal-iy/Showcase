#include "EmployeeInfo.h"

/*EmployeeInfo::EmployeeInfo()
{
	name = " ";
	idNumber = -1;
	SetDepartment(" ");
	SetPosition(" ");
}*/

/*EmployeeInfo::EmployeeInfo(string name, int id)
{
	this->name = name;
	this->idNumber = id;
	SetDepartment(" ");
	SetPosition(" ");
}*/

EmployeeInfo::~EmployeeInfo()
{
	cout << "Bye bye class" << endl;
}

EmployeeInfo::EmployeeInfo(string name, int id, string dep, string pos)
{
	person.SetName(name);
	this->idNumber = id;
	SetDepartment(dep);
	SetPosition(pos);
}

void EmployeeInfo::SetName(string name)
{
	person.SetName(name);
}

void EmployeeInfo::SetIdNumber(int id)
{
	this->idNumber = id;
}

void EmployeeInfo::SetDepartment(string dep)
{
	this->department = dep;
}

void EmployeeInfo::SetPosition(string pos)
{
	this->position = pos;
}

string EmployeeInfo::GetName() const
{
	return person.GetName();
}

int EmployeeInfo::GetIdNumber() const
{
	return idNumber;
}

string EmployeeInfo::GetDepartment() const
{
	return department;
}

string EmployeeInfo::GetPosition() const
{
	return position;
}