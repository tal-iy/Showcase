#pragma once
#include "DataNotSet.h"

template <class T>
class MinMax
{
	private:
		T value1;
		T value2;

	public:
		MinMax(T value1 = -1, T value2 = -1);

		void SetValue1(T value1);
		void SetValue2(T value2);
		
		T Maximum();
		T Minimum();
};

template <class T>
MinMax<T>::MinMax(T value1, T value2)
{
	this->value1 = value1;
	this->value2 = value2;
}

template <class T>
void MinMax<T>::SetValue1(T value1)
{
	this->value1 = value1;
}

template <class T>
void MinMax<T>::SetValue2(T value2)
{
	this->value2 = value2;
}

template <class T>
T MinMax<T>::Maximum()
{
	if (value1 == -1 || value2 == -1)
		throw DataNotSet("One of the values not set (max)");

	T temp;

	if (value1 > value2)
		temp = value1;
	else
		temp = value2;

	return temp;
}

template <class T>
T MinMax<T>::Minimum()
{
	if (value1 == -1 || value2 == -1)
		throw DataNotSet("One of the values not set (max)");

	T temp;

	if (value1 < value2)
		temp = value1;
	else
		temp = value2;

	return temp;
}