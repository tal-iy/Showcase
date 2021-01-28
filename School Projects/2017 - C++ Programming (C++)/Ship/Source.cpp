#include <iostream>
#include "Ship.h"
#include "CargoShip.h"
#include "CruiseShip.h"

using namespace std;

int main()
{
	Ship ship1("First", 1999);
	CargoShip ship2("Second", 2005, 1000);
	CruiseShip ship3("Third", 1980, 500);

	cout << ship1;
	cout << ship2;
	cout << ship3;

	if (ship1 > ship2)
		cout << "Ship " << ship1.GetName() << " is older than ship " << ship2.GetName() << endl;
	else if (ship2 > ship1)
		cout << "Ship " << ship2.GetName() << " is older than ship " << ship1.GetName() << endl;
	else
		cout << "Ship " << ship2.GetName() << " and ship " << ship1.GetName() << " are the same age" << endl;

	if (ship2 > ship3)
		cout << "Ship " << ship2.GetName() << " is older than ship " << ship3.GetName() << endl;
	else if (ship3 > ship2)
		cout << "Ship " << ship3.GetName() << " is older than ship " << ship2.GetName() << endl;
	else
		cout << "Ship " << ship3.GetName() << " and ship " << ship2.GetName() << " are the same age" << endl;

	if (ship1 > ship3)
		cout << "Ship " << ship1.GetName() << " is older than ship " << ship3.GetName() << endl;
	else if (ship3 > ship1)
		cout << "Ship " << ship3.GetName() << " is older than ship " << ship1.GetName() << endl;
	else
		cout << "Ship " << ship3.GetName() << " and ship " << ship1.GetName() << " are the same age" << endl;

	ship1.SetName("First Changed");
	ship1.SetYear(1950);

	ship2.SetName("Second Changed");
	ship2.SetYear(2000);
	ship2.SetCargoCapacity(999);

	ship3.SetName("Third Changed");
	ship3.SetYear(2009);
	ship3.SetPassengerCapacity(10);
	
	cout << ship1;
	cout << ship2;
	cout << ship3;

	system("pause");
	return 0;
}