#include <iostream>
#include <fstream>
#include <string>

using namespace std;

struct StudentRecord
{
	char name[30];
	int id;
	double gpa;
};

int main()
{
	ifstream inFile;
	ofstream outFile;

	StudentRecord stu1 = { "Bob",123,3.45 };
	StudentRecord stu2 = { "John",456,3.23 };
	StudentRecord stu3 = { "Mary",789,4.0 };
	StudentRecord stu4 = { "Tom",101,3.5 };
	StudentRecord temp;

	outFile.open("records.dat", ios::out | ios::binary);

	if (outFile)
	{
		outFile.write(reinterpret_cast<char*>(&stu1), sizeof(stu1));
		outFile.write(reinterpret_cast<char*>(&stu2), sizeof(stu2));
		outFile.write(reinterpret_cast<char*>(&stu3), sizeof(stu3));
	}
	else
	{
		cout << "Could not open output file" << endl;
	}

	outFile.close();
	inFile.open("records.dat", ios::in | ios::binary);

	int offset = 2;
	if (inFile)
	{
		inFile.seekg(sizeof(temp)*offset, ios::beg);
		inFile.read(reinterpret_cast<char*>(&temp), sizeof(temp));
		cout << temp.name << endl << temp.id << endl << temp.gpa << endl << endl;
	}
	else
	{
		cout << "Could not open input file" << endl;
	}

	inFile.close();

	outFile.open("records.dat", ios::out | ios::binary);

	if (outFile)
	{
		outFile.seekp(sizeof(stu4)*offset, ios::beg);
		outFile.write(reinterpret_cast<char*>(&stu4), sizeof(stu4));
	}
	else
	{
		cout << "Could not open output file" << endl;
	}

	outFile.close();

	inFile.open("records.dat", ios::in | ios::binary);

	if (inFile)
	{
		inFile.seekg(sizeof(temp)*offset, ios::beg);
		inFile.read(reinterpret_cast<char*>(&temp), sizeof(temp));
		cout << temp.name << endl << temp.id << endl << temp.gpa << endl << endl;
	}
	else
	{
		cout << "Could not open input file" << endl;
	}

	inFile.close();

	system("pause");
	return 0;
}