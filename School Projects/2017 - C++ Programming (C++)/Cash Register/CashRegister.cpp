#include "CashRegister.h"

CashRegister::CashRegister() 
{
	numItems = 0;
}

void CashRegister::displayItems()
{
	cout << left << setw(5) << "#" << setw(22) << "Item" << setw(15) << "qty on Hand" << endl;
	cout << "--------------------------------------------------------------" << endl;
	for (int i = 0; i < numItems; i++)
		cout << left << setw(5) << (i + 1) << setw(22) << items[i].getDescription() << setw(15) << items[i].getUnits() << endl;
}

void CashRegister::purchaseItem(int item, int num)
{
	double subtotal = items[item - 1].getCost() * 1.3 * num;
	double tax = subtotal*0.06;
	double total = subtotal + tax;

	cout << "Subtotal:  $" << fixed << setprecision(2) << subtotal << endl;
	cout << "Sales Tax: $" << fixed << setprecision(2) << tax << endl;
	cout << "Total:     $" << fixed << setprecision(2) << total << endl;

	items[item - 1].setUnits(items[item - 1].getUnits() - num);
}

void CashRegister::addItem(string desc, double c, int u)
{
	if (numItems < MAX_ITEMS)
	{
		InventoryItem item(desc, c, u);
		items[numItems++] = item;
	}
	else
		cout << "Attempted to add too many items to the cash register!" << endl;
}

int CashRegister::getAvailable() const
{
	return numItems;
}

int CashRegister::getUnits(int item) const
{
	int result = 0;
	if (item > 0 && item <= numItems)
		result = items[item-1].getUnits();
	return result;
}

string CashRegister::getDescription(int item) const
{
	string result = "";
	if (item > 0 && item <= numItems)
		result = items[item - 1].getDescription();
	return result;
}