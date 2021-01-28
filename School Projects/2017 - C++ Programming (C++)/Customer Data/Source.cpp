/*
	Vitaliy Shydlonok
*/
#include "Utils.h"

/*
	Program creates two customers, then prints
	data associated with them. Then prompts to 
	input data for a third customer and prints 
	it as well.
*/
int main()
{
	// Default constructor
	CustomerData customer1;

	// Constructor with populated parameters
	CustomerData customer2("Jones", "Tom", "123 Windy Rd.", "Place", "NY", "11111", "(123)456-789", 12345, true);

	// Populating data members using mutators
	customer1.SetLastName("Smith");
	customer1.SetFirstName("Mary");
	customer1.SetAddress("456 Bright St.");
	customer1.SetCity("Area");
	customer1.SetState("CA");
	customer1.SetZip("22222");
	customer1.SetPhone("(987)654-321");
	customer1.SetCustomerNumber(54321);
	customer1.SetMailingList(false);

	// Printing customer data
	cout << "Customer #1" << endl << "----------------------" << endl;
	DisplayCustomer(&customer1);
	cout << "Customer #2" << endl << "----------------------" << endl;
	DisplayCustomer(&customer2);
	
	// Testing input and output stream operators
	CustomerData customer3;
	cin >> customer3;
	cout << "\nCustomer #3" << endl 
		<< "----------------------" << endl 
		<< customer3 << endl;

	system("pause");
	return 0;
}