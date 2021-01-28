# Programming Languages (Multiple)

This class introduces students to many different programming concepts. We focused on standard object oriented languages, as well as procedural, homoiconic, and functional languages. 

# Projects

[**C - Turing Machine**](C/)

The first project is meant to teach proper memory management in C. I implemented a Turing Machine that reads a file that contains an initial state and a string of instructions. The loads the initial state and then executes the instructions until it reaches the end of the file.
I implemented a linked list to hold the current state, and an array to hold the instructions.

[**Clojure - NOR Converter**](Clojure/)

The clojure program takes a logical expression of any length and converts it to an expression that only uses the NOR connective. The first step is to convert all OR, AND, and NOT connectives to NOR expressions. Then, the resulting expression is simplified to its simplest form.
With an input of "(and false (or false (and y (not true))))", the program converts the expression to "(nor (nor false) (nor (nor (nor false (nor (nor y) (nor (nor true)))))))", and finally it simplifies this to just "false".

[**Scala - REGEX Parser**](Scala/)

The Scala project aims to teach how recursive descent parsers work. I implemented a parser that reads a REGEX expression, recursively builds a tree to represent the expression, and then uses the tree to evaluate whether a string matches the expression.

[**Prolog - State Space Solver**](Prolog/)

For the Prolog assignment, I wrote a state space solver that finds an open path for a laser. The laser starts at the left side of a grid and then keeps moving until it hits an obstacle. The solver attempts to place mirrors on the grid in a way that allows the laser to pass to the other side of the grid without hitting any obstacles. It does this by traversing the grid until an obstacle is hit, then it backtracks to the previous safe position and places a mirror. Then it continues to do this until it reaches the end or it runs out of possible moves using the current set of mirrors.

[**Python - Project Processor**](Python/)

The Python project was aimed to show how a scripting language can be used to quickly process many files and do something with them. I wrote a Python script that collects the previous projects and extracts different pieces of information from them. The information: file name, number of lines, and the identifiers used, are summarized for each project. Then the source and summaries of all the projects are compressed into a zip archive and emailed to the professor.
