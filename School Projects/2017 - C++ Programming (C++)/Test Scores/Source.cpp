/*
	Vitaliy Shydlonok
*/
#include "TestScores.h"

using namespace std;

/*
	Program prints a list of grades, and finds the average.
	Handles NegativeScore and TooLargeScore exceptions if
	a grade in the list is less than 0 or greater than 100.
*/
int main()
{
	int grades[] = {100,75,50,25,0};
	int size = 5;
	int average = -1;

	// Instantiate a TestScores object
	TestScores<int> testScores(grades,size);

	// Handle NegativeScore and TooLargeScore exceptions from TestScores::GetAverage
	try 
	{ 
		average = testScores.GetAverage(); 
	}
	catch (NegativeScore ex) 
	{ 
		cout << ex.GetMessage(); 
	}
	catch (TooLargeScore ex) 
	{ 
		cout << ex.GetMessage(); 
	}

	// Print scores and average if average was calculated
	if (average != -1)
		cout << "The average of scores " << testScores << " is: " << average << endl;

	system("Pause");
	return 0;
}