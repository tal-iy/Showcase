/*
	Vitaliy Shydlonok
*/
#pragma once
#include <iostream>
#include <string>

using namespace std;

/*
	Keeps track of information about a person: last name,
	first name, address, city, state, and phone number.
*/
class PersonData
{
	private:
		// Private class members
		string lastName;
		string firstName;
		string address;
		string city;
		string state;
		string zip;
		string phone;

	public:
		// Constructor, with empty strings as default arguments
		PersonData(string lastName = "", string firstName = "", string address = "", string city = "", string state = "", string zip = "", string phone = "");

		// Mutator functions
		void SetLastName(string lastName);
		void SetFirstName(string firstName);
		void SetAddress(string address);
		void SetCity(string city);
		void SetState(string state);
		void SetZip(string zip);
		void SetPhone(string phone);

		// Accessor functions
		string GetLastName();
		string GetFirstName();
		string GetAddress();
		string GetCity();
		string GetState();
		string GetZip();
		string GetPhone();

		// Stream operators
		friend ostream &operator << (ostream &strm, const PersonData &obj);
		friend istream &operator >> (istream &strm, PersonData &obj);
};