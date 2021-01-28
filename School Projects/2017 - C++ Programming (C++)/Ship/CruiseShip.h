#pragma once

#include "Ship.h"

class CruiseShip : public Ship
{
	private:
		int passengerCapacity;

	public:
		CruiseShip();
		CruiseShip(string name, int year, int capacity);
		int GetPassengerCapacity() const;
		void SetPassengerCapacity(int capacity);

		friend ostream &operator << (ostream &strm, const CruiseShip &obj);
};