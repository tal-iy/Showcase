#include <iostream>
#include <string>
#include "BinaryTree.h"
#include "EmployeeInfo.h"
using namespace std;

int main()
{
	string name;

	// Create the binary tree.
	BinaryTree<EmployeeInfo> tree;

	// Populate the binary tree
	EmployeeInfo emp1(1021, "John Williams");
	tree.insertNode(emp1);
	EmployeeInfo emp2(1057, "Bill Witherspoon");
	tree.insertNode(emp2);
	EmployeeInfo emp3(2487, "Jennifer Twain");
	tree.insertNode(emp3);
	EmployeeInfo emp4(3769, "Sophia Lancaster");
	tree.insertNode(emp4);
	EmployeeInfo emp5(1017, "Debbie Reece");
	tree.insertNode(emp5);
	EmployeeInfo emp6(1275, "George McMullen");
	tree.insertNode(emp6);
	EmployeeInfo emp7(1899, "Ashley Smith");
	tree.insertNode(emp7);
	EmployeeInfo emp8(4218, "Josh Plemmons");
	tree.insertNode(emp8);

	// Print database
	cout << "\nEmployees in the database:\n";
	tree.displayInOrder();
	cout << endl;

	string exit = "";
	while (exit != "n" && exit != "no" && exit != "N" && exit != "No" && exit != "NO")
	{
		// Prompt ofr ID
		int id;
		cout << "Enter an employee ID to search for: ";
		cin >> id;

		// Search for user
		EmployeeInfo searchExisting(id, "");
		EmployeeInfo *searchPtr = &searchExisting;
		cout << endl << "Searching for ID " << id << ".... " << endl;

		if (tree.searchNode(searchPtr))
			cout << "Found: " << *searchPtr << endl;
		else
			cout << "User with id " << searchPtr->GetID() << " was not found!" << endl;

		cout << endl << "Would you like to search for another employee? (Y/N) ";
		cin >> exit;
	}

	system("pause");
	return 0;
}