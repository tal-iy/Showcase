#include "CargoShip.h"

CargoShip::CargoShip()
{
	SetCargoCapacity(0);
}

CargoShip::CargoShip(string name, int year, int capacity) : Ship(name, year)
{
	SetCargoCapacity(capacity);
}

int CargoShip::GetCargoCapacity() const
{
	return cargoCapacity;
}

void CargoShip::SetCargoCapacity(int capacity)
{
	this->cargoCapacity = capacity;
}

ostream & operator<<(ostream & strm, const CargoShip & obj)
{
	return strm << "Cargo ship name: " << obj.GetName() << endl
				<< "Cargo ship year: " << obj.GetYear() << endl
				<< "Cargo ship cargo capacity: " << obj.GetCargoCapacity() << endl;
}
