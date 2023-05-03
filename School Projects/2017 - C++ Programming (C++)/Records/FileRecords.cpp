/*
	Vitaliy Shydlonok
*/
#include "FileRecords.h"

/*
	Shows a menu of choices, and prompts for
	an integer input until a correct choice is made.
*/
int ShowMenu()
{
	int answer = 0;

	//loop until answer is changed to a a correct option
	while (answer == 0)
	{
		cout << "What would you like to do?" << endl
			<< "1: Add Item" << endl
			<< "2: Display Item" << endl
			<< "3: Edit Item" << endl
			<< "4: Exit" << endl
			<< "Choice: ";
		cin >> answer;
		cin.ignore();

		//make sure answer is 1 through 4
		if (answer < 1 || answer > 4)
		{
			cout << "Enter a number between 1 and 4!" << endl;
			answer = 0;
		}
	}

	return answer;
}

/*
	Prompts for record information, then
	add the record to the end of the file.
*/
void AddItem(fstream &file)
{
	//prompt for record information
	Record rec;
	cout << "Enter the items name: " << endl;
	cin >> rec.name;
	cout << "Enter the items quantity: " << endl;
	cin >> rec.quantity;
	cout << "Enter the items wholesale cost: " << endl;
	cin >> rec.wholesale;
	cout << "Enter the items retail cost: " << endl;
	cin >> rec.retail;
	cin.ignore();

	//seek to end of file to append
	file.seekp(0, ios::end);

	//append record
	file.write(reinterpret_cast<char*>(&rec), sizeof(rec));

	cout << "Record added!" << endl;
}

/*
	Prompts for a record number, then displays
	the record information if it is found
*/
void DisplayItem(fstream &file)
{
	Record rec;
	int offset = 0;

	//get which record to display
	cout << "Which record would you like to display? (Enter a number, first record is 1)" << endl;
	cin >> offset;
	cin.ignore();
	
	//decrement offset to start at 0 instead of 1
	offset--;

	//attempt to read the record
	file.seekg(sizeof(rec)*offset, ios::beg);
	file.read(reinterpret_cast<char*>(&rec), sizeof(rec));

	//check if the record was properly read, and display it if it was
	if (rec.name[0] == ' ')
		cout << "That record doesn't exist!" << endl;
	else
		cout << "Item name: " << rec.name << endl
		<< "Item quantity: " << rec.quantity << endl
		<< "Wholesale value: " << rec.wholesale << endl
		<< "Retail value: " << rec.retail << endl << endl;

}

/*
	Prompts for a record number, then prompts for
	the record information, and stores the new record
	if one is found with that record number.
*/
void EditItem(fstream &file){	Record rec;
	int offset = 0;

	//get which record to edit
	cout << "Which record would you like to edit? (Enter a number, first record is 1)" << endl;
	cin >> offset;
	cin.ignore();

	//decrement offset to start at 0 instead of 1
	offset--;

	//attempt to read the record
	file.seekg(sizeof(rec)*offset, ios::beg);
	file.read(reinterpret_cast<char*>(&rec), sizeof(rec));

	//check to see if the record was properly read
	if (rec.name[0] == ' ')
		cout << "That record doesn't exist!" << endl;
	else
	{
		//get new record information
		cout << "Enter the items name: " << endl;
		cin >> rec.name;
		cout << "Enter the items quantity: " << endl;
		cin >> rec.quantity;
		cout << "Enter the items wholesale cost: " << endl;
		cin >> rec.wholesale;
		cout << "Enter the items retail cost: " << endl;
		cin >> rec.retail;
		cin.ignore();

		//overwrite the chosen record with the new information
		file.seekp(sizeof(rec)*offset, ios::beg);
		file.write(reinterpret_cast<char*>(&rec), sizeof(rec));		cout << "Record updated!" << endl;
	}}