#include "EmployeeInfo.h"

void DisplayEmployee(EmployeeInfo* const e)
{
	cout << setw(20) << e->GetName() 
		<< setw(20) << e->GetIdNumber()
		<< setw(20) << e->GetDepartment()
		<< setw(20) << e->GetPosition()
		<< endl;
}

int main()
{
	EmployeeInfo me;
	EmployeeInfo *you = new EmployeeInfo();
	EmployeeInfo tom("Tom Jones", 1234);
	EmployeeInfo mary("Mary Poppins", 6464, "Engineering", "Programmer");

	me.SetName("Vitaliy");
	//cout << "Employee name is " << me.GetName() << endl;
	//cout << me.GetIdNumber() << endl;

	you->SetName("James Brown");

	DisplayEmployee(&me);
	DisplayEmployee(you);
	DisplayEmployee(&tom);
	DisplayEmployee(&mary);

	/*cout << "Employee name is " << you->GetName() << endl;

	cout << "Employee name is " << tom.GetName() << endl;
	cout << tom.GetIdNumber() << endl;
	cout << "Department " << tom.GetDepartment() << endl;
	cout << "Position " << tom.GetPosition() << endl;

	cout << "Employee name is " << mary.GetName() << endl;
	cout << mary.GetIdNumber() << endl;
	cout << "Department " << mary.GetDepartment() << endl;
	cout << "Position " << mary.GetPosition() << endl;*/

	cout << "Program ending" << endl;
	delete you;
	system("pause");
	return 0;
}