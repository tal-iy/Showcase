/*
	Vitaliy Shydlonok
*/

#include "Scores.h"

/*
	Prompts for a number of scores based on the size 
		parameter and stores them in the scores array.
*/
void GetGrades (double* scores, int size)
{
	int grade = 0;
	for(int i=0;i<size;i++)
	{
		cout << "Enter grade: ";
		cin >> grade;

		//validate score input
		if (grade < 0)
		{
			cout << "Grade must be positive!\n";
			//undo increment to stay on the same element
			i--;
		}
		else
		{
			//store input in the array if valid
			*(scores + i) = grade;
		}
	}
}

/*
	Calculates the average of all elements in the scores
		array, then returns it.
*/
double Average(double* scores, int size)
{
	//add scores together
	double ave = 0;
	for(int i=0;i<size;i++)
		ave += *(scores + i);

	//get average of the scores
	ave /= size;
	
	return ave;
}

/*
	Displays all of the scores within the scores array.
*/
void DisplayGrades(double* scores, int size)
{
	cout << "Scores:\n";
	for(int i=0;i<size;i++)
	{
		cout << *(scores + i) << "\n";
	}
}