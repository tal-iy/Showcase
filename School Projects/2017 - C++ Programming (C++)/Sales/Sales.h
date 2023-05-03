#pragma once

#include <iostream>
#include <string>

using namespace std;

const int COLS = 5;
const int ROWS = 5;

double GetTotal(double[][COLS]);

double GetAverage(double[][COLS]);

double GetRowTotal(double[][COLS], int);

double GetColumnTotal(double[][COLS], int);

double GetHighest(double[][COLS], int&, int&);

double GetLowest(double[][COLS], int&, int&);