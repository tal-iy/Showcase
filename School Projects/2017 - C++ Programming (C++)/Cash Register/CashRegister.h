#pragma once
#include <iostream>
#include <iomanip>
#include "InventoryItem.h"

using namespace std;

class CashRegister
{
	private:
		static const int MAX_ITEMS = 50; // maximum size of the array
		InventoryItem items[MAX_ITEMS]; // array of InventoryItem objects
		int numItems; // number of items in the array

	public:
		CashRegister();

		// displays a list of available items
		void displayItems();

		// purchases an item and displays the cost information
		void purchaseItem(int item, int num);

		// adds a new item to be available for sale
		void addItem(string desc, double c, int u);

		// returns how many items are available
		int getAvailable() const;

		// returns the quantity of an item
		int getUnits(int item) const;

		// returns the description of an item
		string getDescription(int item) const;

};