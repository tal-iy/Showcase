/*
	Vitaliy Shydlonok
*/
#include "PersonData.h"

/*
	Initializes members: lastName, firstName, address, city, state, zip, and phone.
	Has default values for all parameters.
*/
PersonData::PersonData(string lastName, string firstName, string address, string city, string state, string zip, string phone)
{
	this->lastName = lastName;
	this->firstName = firstName;
	this->address = address;
	this->city = city;
	this->state = state;
	this->zip = zip;
	this->phone = phone;
}

void PersonData::SetLastName(string lastName)
{
	this->lastName = lastName;
}

void PersonData::SetFirstName(string firstName)
{
	this->firstName = firstName;
}

void PersonData::SetAddress(string address)
{
	this->address = address;
}

void PersonData::SetCity(string city)
{
	this->city = city;
}

void PersonData::SetState(string state)
{
	this->state = state;
}

void PersonData::SetZip(string zip)
{
	this->zip = zip;
}

void PersonData::SetPhone(string phone)
{
	this->phone = phone;
}

string PersonData::GetLastName()
{
	return lastName;
}

string PersonData::GetFirstName()
{
	return firstName;
}

string PersonData::GetAddress()
{
	return address;
}

string PersonData::GetCity()
{
	return city;
}

string PersonData::GetState()
{
	return state;
}

string PersonData::GetZip()
{
	return zip;
}

string PersonData::GetPhone()
{
	return phone;
}

/*
	Output stream operator, prints PersonData members into stream
*/
ostream & operator<<(ostream & strm, const PersonData & obj)
{
	strm << "Last Name: " << obj.lastName << endl
		<< "First Name: " << obj.firstName << endl
		<< "Address: " << obj.address << endl
		<< "City: " << obj.city << endl
		<< "State: " << obj.state << endl
		<< "ZIP: " << obj.zip << endl;

	return strm;
}

/*
	Input stream operator, prompts for data and populates PersonData members with it
*/
istream & operator>>(istream & strm, PersonData & obj)
{
	cout << "Enter Last Name: ";
	strm >> obj.lastName;
	cout << "Enter First Name: ";
	strm >> obj.firstName;
	cout << "Enter Address: ";
	strm.ignore();
	getline(strm, obj.address);
	cout << "Enter City: ";
	strm >> obj.city;
	cout << "Enter State: ";
	strm >> obj.state;
	cout << "Enter ZIP: ";
	strm >> obj.zip;

	return strm;
}
