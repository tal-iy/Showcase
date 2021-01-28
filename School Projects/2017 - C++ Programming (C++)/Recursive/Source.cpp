#include <iostream>

using namespace std;

/*
	Method which returns a number raised
	to the power of an exponent.
*/
int Pow(int num, int exponent)
{
	int total = 0;

	if (exponent == 0)
		total = 1; // Base case
	else
		total = num*Pow(num, exponent - 1); // num * num^(exponent-1)

	return total;
}

/*
	Method which returns the sum from 1 to
	a given number.
*/
int Sum(int num)
{
	int total = 0;
	if (num <= 0)
		total = 0; // Check for invalid inputs
	else if (num == 1)
		total = 1; // Base case
	else
		total = num + Sum(num - 1); // num + (sum of numbers from 1 to num-1)
	return total;
}

/*
	Program tests the functionality of the
	recursive methods: Pow and Sum
*/
int main()
{
	// Testing power method
	cout << "2^5 = " << Pow(2, 5) << endl;
	cout << "1^1 = " << Pow(1, 1) << endl;
	cout << "8^0 = " << Pow(8, 0) << endl;
	cout << "6^3 = " << Pow(6, 3) << endl;
	cout << "5^2 = " << Pow(5, 2) << endl;

	// Testing summation method
	cout << "1 = " << Sum(1) << endl;
	cout << "1+2 = " << Sum(2) << endl;
	cout << "1+2+3 = " << Sum(3) << endl;
	cout << "1+2+3+4 = " << Sum(4) << endl;
	cout << "1+2+3+4+5 = " << Sum(5) << endl;

	system("pause");
	return 0;
}