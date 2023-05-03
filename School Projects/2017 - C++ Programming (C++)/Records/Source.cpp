/*
	Vitaliy Shydlonok
*/
#include "FileRecords.h"

/*
	Keeps track of a records file, allowing the user
	to add a new record to the end, display any record,
	and edit any record.
*/
int main()
{
	//open new binary file stream for input and output
	fstream file;
	
	bool exit = false;
	//loop until user chooses to exit
	while (!exit)
	{
		//get users choice of action
		int choice = ShowMenu();

		if (choice == 1)
		{
			//open file for appending to add a new item
			file.open("records.txt", ios::binary | ios::app);
			if (file)
			{
				AddItem(file);
				file.close();
			}
			else
				cout << "Error opening file for writing!" << endl;
		}
		else if (choice == 2)
		{
			//open file for reading to display an item
			file.open("records.txt", ios::in | ios::binary);
			if (file)
			{
				DisplayItem(file);
				file.close();
			}
			else
				cout << "Error opening file for reading!" << endl;
		}
		else if (choice == 3)
		{
			//open file for reading and writing to edit an existing item
			file.open("records.txt", ios::ate | ios::out | ios::in | ios::binary);
			if (file)
			{
				EditItem(file);
				file.close();
			}
			else
				cout << "Error opening file for writing!" << endl;
		}
		else if (choice == 4)
			exit = true;
	}
	return 0;
}