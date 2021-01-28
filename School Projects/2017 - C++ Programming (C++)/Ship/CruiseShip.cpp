#include "CruiseShip.h"

CruiseShip::CruiseShip()
{
	SetPassengerCapacity(0);
}

CruiseShip::CruiseShip(string name, int year, int capacity) : Ship(name, year)
{
	SetPassengerCapacity(capacity);
}

int CruiseShip::GetPassengerCapacity() const
{
	return passengerCapacity;
}

void CruiseShip::SetPassengerCapacity(int capacity)
{
	this->passengerCapacity = capacity;
}

ostream & operator<<(ostream & strm, const CruiseShip & obj)
{
	return strm << "Cruise ship name: " << obj.GetName() << endl
				<< "Cruise ship year: " << obj.GetYear() << endl
				<< "Cruise ship passenger capacity: " << obj.GetPassengerCapacity() << endl;
}
