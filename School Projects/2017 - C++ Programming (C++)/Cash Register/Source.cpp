#include "CashRegister.h"

int main()
{
	CashRegister reg;
	int item = 0, units = 0;
	string choice = "y";

	// add a bunch of items to work with
	reg.addItem("Adjustable Wrench", 5.80, 10);
	reg.addItem("Screwdriver", 2.25, 20);
	reg.addItem("Pliers", 4.99, 35);
	reg.addItem("Ratchet", 7.00, 10);
	reg.addItem("Socket Wrench", 8.50, 7);

	// loop until the user chooses to not purchase another item
	while (choice == "y" || choice == "Y" || choice == "yes" || choice == "Yes" || choice == "YES")
	{
		reg.displayItems();

		cout << "Which item above is being purchased? ";
		cin >> item;

		// validate item # input
		if (item < 0 || item > reg.getAvailable())
			cout << "Item #" << item << " doesn't exist!" << endl;
		else if (reg.getUnits(item) == 0)
			cout << "The item \"" << reg.getDescription(item) << "\" is out of stock!" << endl;
		else
		{
			cout << "How many units? ";
			cin >> units;

			// validate units input
			if (units < 1)
				cout << "You must purchase at least one unit!" << endl;
			else if (units > reg.getUnits(item))
				cout << "There are only " << reg.getUnits(item) << " units left in stock!" << endl;
			else
				reg.purchaseItem(item, units);
		}

		cout << "Do you want to purchase another item? " << endl;
		cin >> choice;
	}
	
	system("pause");
	return 0;
}