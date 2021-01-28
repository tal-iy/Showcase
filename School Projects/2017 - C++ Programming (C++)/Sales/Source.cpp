#include "Sales.h"

int main()
{
	double records[ROWS][COLS];
	string names[] = { "North","South","East","West" };

	//get input for sales data
	for(int row = 0; row < 4; row++)
		for (int col = 0; col < 4; col++)
		{
			cout << "Enter record for the " << names[row] << " division Quarter " << col << ": ";
			cin >> records[row][col];
		}

	//calculate totals for each row and column
	for (int i = 0; i < 4; i++)
	{
		GetRowTotal(records, i);
		GetColumnTotal(records, i);
	}

	//variables to store rows and columns of highest and lowest record
	int highRow = 0;
	int highCol = 0;
	int lowRow = 0;
	int lowCol = 0;

	//calculate highest and lowest positions in the records
	GetHighest(records, highRow, highCol);
	GetLowest(records, lowRow, lowCol);

	//print first four rows of the sales data
	cout << "\tQuarter 1    Quarter 2    Quarter 3    Quarter 4    Division Total" << endl;
	for (int i = 0; i < 4; i++)
	{
		cout << names[i];
		cout << "\t\t";
		for (int j = 0; j <= 4; j++)
			cout << records[i][j] << "          ";
		cout << endl;
	}

	//print last row of column totals
	cout << "Quarter Total\t";
	for (int j = 0; j < 4; j++)
		cout << records[4][j] << "          ";

	//print highest and lowest sales records
	cout << endl;
	cout << endl << "Highest: " << names[highRow] << " Division, Quarter " << (highCol + 1);
	cout << endl << "Lowest: " << names[lowRow] << " Division, Quarter " << (lowCol + 1);

	//pause program
	int x;
	cin >> x;

	return 0;
}