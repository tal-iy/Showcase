/*
	Vitaliy Shydlonok
*/

#include "Scores.h"

/*
	Program prompts the user for number of scores to keep track of, 
		then prompts for the scores, then calculates the average,
		and then displays the entered scores along with the average.
*/
int main()
{
	//prompt for how many scores to enter
	int num = 0;
	cout << "How many test grades would you like to input?";
	cin >> num;

	//validate input
	while (num < 1)
	{
		cout << "You must enter at least one grade!\n";
		cout << "How many test grades would you like to input?";
		cin >> num;
	}
	
	//get scores from the user
	double* scores = new double[num];
	GetGrades(scores, num);
	
	//calculate the average
	double ave = 0;
	ave = Average(scores, num);
	
	//display scores
	DisplayGrades(scores, num);

	//display the average
	cout << "Average Score " << ave;
	
	//clean up
	delete[] scores;

	cin >> num;
	return 0;
}