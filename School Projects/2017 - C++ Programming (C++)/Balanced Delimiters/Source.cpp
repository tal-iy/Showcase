#include <iostream>
#include <string>
#include <stack>

using namespace std;

int main()
{
	string code = "Testing  123 ( {e} and { ) 456 } )";

	stack<char> delimiters;
	bool invalid = false;

	// Go through every character in the string or until an invalid delimiter is found
	for (int i = 0; i < code.length() && !invalid; i++)
	{
		char c = code.at(i);

		// Push any starting delimiters onto the stack
		if (c == '{' || c == '[' || c == '(')
			delimiters.push(c);
		else if (c == '}' || c == ']' || c == ')')
		{
			char ex;

			// Make sure that the stack is not empty
			if (delimiters.empty())
			{
				cout << "Invalid delimiter: found " << c << " but expected nothing" << endl;
				invalid = true;
			}
			else
			{
				// Figure out what delimiter is expected
				if (delimiters.top() == '{')
					ex = '}';
				else if (delimiters.top() == '[')
					ex = ']';
				else if (delimiters.top() == '(')
					ex = ')';

				// Check if found delimiter is the same as expected
				if (c == ex)
					delimiters.pop();
				else
				{
					cout << "Invalid delimiter: found " << c << " but expected " << ex << endl;
					invalid = true;
				}
			}
		}
	}

	// Notify if no invalid delimiters were found
	if (!invalid)
		cout << "The code \"" << code << "\" is properly delimitered" << endl;

	system("pause");
	return 0;
}