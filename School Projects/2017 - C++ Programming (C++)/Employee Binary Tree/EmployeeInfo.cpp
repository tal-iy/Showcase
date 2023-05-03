#include "EmployeeInfo.h"

EmployeeInfo::EmployeeInfo(int ID, string name)
{
	this->empID = ID;
	this->empName = name;
}

bool EmployeeInfo::operator<(EmployeeInfo &emp) const
{
	return this->empID < emp.empID;
}

bool EmployeeInfo::operator>(EmployeeInfo & emp) const
{
	return this->empID > emp.empID;
}

bool EmployeeInfo::operator==(EmployeeInfo & emp) const
{
	return this->empID == emp.empID;
}

int EmployeeInfo::GetID() const
{
	return this->empID;
}

void EmployeeInfo::SetID(int ID)
{
	this->empID = ID;
}

string EmployeeInfo::GetName() const
{
	return this->empName;
}

void EmployeeInfo::SetName(string name)
{
	this->empName = name;
}

/*
	Output stream operator, prints EmployeeInfo ID and name to the 
	stream and then returns the stream.
*/
ostream & operator<<(ostream & strm, const EmployeeInfo & obj)
{
	return strm << obj.GetID() << " " << obj.GetName();
}
