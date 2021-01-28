#include "MinMax.h"
#include "DataNotSet.h"
#include <iostream>

using namespace std;

int main()
{
	MinMax<int> temp1;
	MinMax<double> temp2(2.34, 5.67);

	try 
	{
		cout << temp1.Maximum() << endl;
		cout << temp1.Minimum() << endl;
	}
	catch (DataNotSet ex)
	{
		cout << ex.GetMessage() << endl;
	}

	temp1.SetValue1(88);
	temp1.SetValue2(56);

	cout << temp2.Maximum() << endl;
	cout << temp2.Minimum() << endl;

	cout << temp1.Maximum() << endl;
	cout << temp1.Minimum() << endl;

	system("pause");
	return 0;
}