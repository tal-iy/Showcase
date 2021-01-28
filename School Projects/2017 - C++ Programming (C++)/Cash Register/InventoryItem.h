#ifndef INVENTORYITEM_H
#define INVENTORYITEM_H
#include <string>
using namespace std;

// Constant for the description's default size
const int DEFAULT_SIZE = 51;

class InventoryItem
{
private:
   string description;  // The item description
   double cost;        // The item cost
   int units;          // Number of units on hand

public:
   // Constructor #1
   InventoryItem();

   // Constructor #2
   InventoryItem(string desc);

   // Constructor #3
   InventoryItem(string desc, double c, int u);

   // Destructor
   ~InventoryItem();

   // Mutator functions
   void setDescription(string d);


   void setCost(double c);


   void setUnits(int u);


   // Accessor functions
   string getDescription() const;


   double getCost() const;

   int getUnits() const;

};

#endif