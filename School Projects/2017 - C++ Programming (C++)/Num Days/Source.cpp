#include "NumDays.h"
#include <iostream>

using namespace std;

int main()
{
	NumDays days1(26);
	NumDays days2(23);
	NumDays days3(0);
	NumDays days4(24);

	days3 = days1 + days2;

	cout << days1.GetDays() << " " << days1.GetHours() << endl;
	cout << days2.GetDays() << " " << days2.GetHours() << endl;
	cout << days3.GetDays() << " " << days3.GetHours() << endl;

	cout << days1 << endl;
	cout << ++days1 << endl;
	cout << days1++ << endl;
	cout << days1 << endl;

	cout << days4 << endl;
	days4--;
	cout << days4 << endl;

	system("pause");
	return 0;
}