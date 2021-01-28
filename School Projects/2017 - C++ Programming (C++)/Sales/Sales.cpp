#include "Sales.h"

/*
	returns total of all sales records
*/
double GetTotal(double records[][COLS])
{
	//add together every sales data in records
	double total = 0;
	for (int x = 0; x < ROWS-1; x++)
		for (int y = 0; y < COLS-1; y++)
			total += records[x][y];
	return total;
}

/*
	returns average of all sales records
*/
double GetAverage(double records[][COLS])
{
	//add each sales record together
	double total = 0;
	double num = 0.0;
	for (int x = 0; x < ROWS-1; x++)
		for (int y = 0; y < COLS-1; y++)
		{
			total += records[x][y];
			num++;
		}

	//divide by total number of records to get average
	total /= num;
	return total;
}

/*
	returns row total
*/
double GetRowTotal(double records[][COLS], int row)
{
	//add together every sales record in the row
	double total = 0;
	for (int i = 0; i < COLS-1; i++)
		total += records[row][i];

	//store the total in the last column
	records[row][COLS - 1] = total;
	return total;
}

/*
	returns column total
*/
double GetColumnTotal(double records[][COLS], int col)
{
	//add together every sales record in the column
	double total = 0;
	for (int i = 0; i < ROWS-1; i++)
		total += records[i][col];

	//store the total in the last row
	records[ROWS-1][col] = total;
	return total;
}

/*
	returns the highest sales record
*/
double GetHighest(double records[][COLS], int& row, int& col)
{
	double largest = 0;
	int largRow = 0;
	int largCol = 0;

	//go through each record and compare with largest found
	for (int x=0;x<ROWS-1;x++)
		for (int y=0;y<COLS-1;y++)
			if (records[x][y] > largest)
			{
				largest = records[x][y];
				largRow = x;
				largCol = y;
			}

	row = largRow;
	col = largCol;
	return largest;
}

/*
	returns the lowest sales record
*/
double GetLowest(double records[][COLS], int& row, int& col)
{
	double lowest = records[0][0];
	int largRow = 0;
	int largCol = 0;

	//go through each record and compare with lowest found
	for (int x = 0; x<ROWS-1; x++)
		for (int y = 0; y<COLS-1; y++)
			if (records[x][y] < lowest)
			{
				lowest = records[x][y];
				largRow = x;
				largCol = y;
			}

	row = largRow;
	col = largCol;
	return lowest;
}