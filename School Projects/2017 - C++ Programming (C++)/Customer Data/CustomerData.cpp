/*
	Vitaliy Shydlonok
*/
#include "CustomerData.h"

/*
	Initializes customerNumber and mailingList members, and calls parent constructor.
	Has default values for all parameters.
*/
CustomerData::CustomerData(string lastName, string firstName, string address, string city, string state, string zip, string phone, int customerNumber, bool mailingList) : PersonData(lastName, firstName, address, city, state, zip, phone)
{
	this->customerNumber = customerNumber;
	this->mailingList = mailingList;
}

void CustomerData::SetCustomerNumber(int customerNumber)
{
	this->customerNumber = customerNumber;
}

void CustomerData::SetMailingList(bool mailingList)
{
	this->mailingList = mailingList;
}

int CustomerData::GetCustomerNumber()
{
	return customerNumber;
}

bool CustomerData::GetMailingList()
{
	return mailingList;
}

/*
	Output stream operator, prints CustomerData information into stream
*/
ostream & operator<<(ostream & strm, const CustomerData & obj)
{
	// Call base class output stream operator
	strm << (PersonData&)obj;

	// Print derived class members
	strm << "Customer Number: " << obj.customerNumber << endl << "Mailing List? ";
	if (obj.mailingList)
		strm << "Yes" << endl;
	else
		strm << "No" << endl;

	return strm;
}

/*
	Input stream operator, prompts for data and populates CustomerData members with it
*/
istream & operator>>(istream & strm, CustomerData & obj)
{
	// Call base class input stream operator
	strm >> (PersonData&)obj;

	// Prompt for and populate derived class members
	string answer;

	cout << "Enter Customer Number: ";
	strm >> obj.customerNumber;
	cout << "Mailing List (Yes/No)? ";
	strm >> answer;

	obj.mailingList = (answer == "Yes" || answer == "YES" || answer == "Y" || answer == "y");

	return strm;
}
