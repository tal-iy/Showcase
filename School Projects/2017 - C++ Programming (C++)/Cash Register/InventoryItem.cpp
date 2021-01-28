#include "InventoryItem.h"

// Constructor #1
InventoryItem::InventoryItem()
{
    // Initialize cost and units.
    cost = 0.0;
    units = 0;
    description = "";
}

// Constructor #2
InventoryItem::InventoryItem(string desc)
{
    // Initialize cost and units.
    cost = 0.0;
    units = 0;
    description = desc;
}

// Constructor #3
InventoryItem::InventoryItem(string desc, double c, int u)
{
    // Assign values to cost and units.
    cost = c;
    units = u;
	description = desc;
}

// Destructor
InventoryItem::~InventoryItem()
{

}

// Mutator functions
void InventoryItem::setDescription(string d)
{
    description = d;
}

void InventoryItem::setCost(double c)
{
    cost = c;
}

void InventoryItem::setUnits(int u)
{
    units = u;
}

// Accessor functions
string InventoryItem::getDescription() const
{
    return description;
}

double InventoryItem::getCost() const
{
    return cost;
}

int InventoryItem::getUnits() const
{
    return units;
}