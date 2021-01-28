#pragma once

#include "Ship.h"

class CargoShip : public Ship
{
	private:
		int cargoCapacity;
	public:
		CargoShip();
		CargoShip(string name, int year, int capacity);
		int GetCargoCapacity() const;
		void SetCargoCapacity(int capacity);

		friend ostream &operator << (ostream &strm, const CargoShip &obj);
};