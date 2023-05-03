/*
	Vitaliy Shydlonok
*/
#pragma once
#include <iostream>
#include <string>
#include "NegativeScore.h"
#include "TooLargeScore.h"

using namespace std;

/*
	Keeps track of an array of grades with template data type
	and calculates the average of the grades.
*/
template <class T>
class TestScores
{
	private:
		T* grades;
		int size;
		
	public:
		TestScores(T* grades, int size);
		T GetAverage();

		template<class T>
		friend ostream &operator << (ostream &strm, const TestScores<T> &obj);
};

/*
	Initializes members: grades and size
*/
template<class T>
TestScores<T>::TestScores(T* grades, int size)
{
	this->grades = grades;
	this->size = size;
}

/*
	Calculates the average of all scores in array of grades,
	returns that average, and throws a NegativeScore or a
	TooLargeScore exception if any of the grades are greater
	than 100 or less than 0.
*/
template<class T>
T TestScores<T>::GetAverage()
{
	T average = 0;

	// Calculate total sum of grades
	for (int i = 0; i < size; i++)
	{
		// Throw exception if score is less than 0 or greater than 100
		if (grades[i] < 0)
			throw NegativeScore("Score " + to_string(grades[i]) + " is negative, scores must be greater than or equal to 0\n");
		if (grades[i] > 100)
			throw TooLargeScore("Score " + to_string(grades[i]) + " is too large, scores must be less than or equal to 100\n");

		average += grades[i];
	}

	// Calculate final average
	average /= size;

	return average;
}

/*
	Output stream operator, prints array of grades in TestScores into stream
*/
template<class T>
ostream & operator<<(ostream & strm, const TestScores<T> & obj)
{
	for (int i = 0; i < obj.size; i++)
	{
		// Print commas and a final "and" between scores
		if (i > 0 && i < obj.size - 1)
			strm << ", ";
		else if (i == obj.size - 1)
			strm << ", and ";

		// Print each score
		strm << obj.grades[i];
	}
	return strm;
}