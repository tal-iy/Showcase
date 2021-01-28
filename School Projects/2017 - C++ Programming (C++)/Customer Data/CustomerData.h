/*
	Vitaliy Shydlonok
*/
#pragma once
#include "PersonData.h"

/*
	Keeps track of PersonData information along with a customer number and 
	whether the customer is on a mailing list.
*/
class CustomerData : public PersonData
{
	private:
		// Private class members
		int customerNumber;
		bool mailingList;

	public:
		// Constructor, with empty strings, 0, and false as default parameters
		CustomerData(string lastName = "", string firstName = "", string address = "", string city = "", string state = "", string zip = "", string phone = "", int customerNumber = 0, bool mailingList = false);

		// Mutator functions
		void SetCustomerNumber(int customerNumber);
		void SetMailingList(bool mailingList);

		// Accessor functions
		int GetCustomerNumber();
		bool GetMailingList();

		// Stream operators
		friend ostream &operator << (ostream &strm, const CustomerData &obj);
		friend istream &operator >> (istream &strm, CustomerData &obj);
};