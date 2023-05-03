% Safety conditions
collision(X, Y, Bx, By, Width, Height) :-
    X > Bx-1,
    X < Bx+Width,
	Y > By-1,
	Y < By+Height.

safe_obst(_, _, []).
safe_obst(X, Y, [[Bx, Width, Height]|Rest]) :-
    \+collision(X, Y, Bx+1, 11-Height, Width, Height), 
    safe_obst(X, Y,Rest).

safe_person(X, Y, [Bx, Width, Height]) :- \+collision(X, Y, Bx+1, 1, Width, Height).
safe_bounds(X, Y) :- collision(X, Y, 1, 1, 12, 10).

safe(X, Y, Obstacles) :-
    safe_bounds(X, Y),
    safe_person(X, Y, [4, 2, 6]),
    safe_obst(X, Y, Obstacles).

% Traversing the grid
move([X, Y, "Right", Mirrors, Num], [NewX, Y, "Right", Mirrors, Num]):- NewX is X+1.
move([X, Y, "Up", Mirrors, Num], [X, NewY, "Up", Mirrors, Num]):- NewY is Y+1.
move([X, Y, "Down", Mirrors, Num], [X, NewY, "Down", Mirrors, Num]):- NewY is Y-1.

% Placing mirrors
move([X, Y, "Right", Mirrors, Num], [X, NewY, "Up", [[X, Y, /]|Mirrors], NewNum]):- NewY is Y+1, NewNum is Num+1.
move([X, Y, "Right", Mirrors, Num], [X, NewY, "Down", [[X, Y, \]|Mirrors], NewNum]):- NewY is Y-1, NewNum is Num+1.
move([X, Y, "Up", Mirrors, Num], [NewX, Y, "Right", [[X, Y, /]|Mirrors], NewNum]):- NewX is X+1, NewNum is Num+1.
move([X, Y, "Down", Mirrors, Num], [NewX, Y, "Right", [[X, Y, \]|Mirrors], NewNum]):- NewX is X+1, NewNum is Num+1.

% Solver
solve(12, Y, "Right", Y, _, Mirrors, Num, Mirrors):- Num < 9.
solve(X, Y, Dir, Lazer, Obstacles, Mirrors, Num, Answer) :-
    move([X, Y, Dir, Mirrors, Num], [NewX, NewY, NewDir, NewMirrors, NewNum]), 
    safe(NewX, NewY, Obstacles),
    solve(NewX, NewY, NewDir, Lazer, Obstacles, NewMirrors, NewNum, Answer).

% Main function
place_mirrors(Lazer, Obstacles, Answer) :- solve(1, Lazer, "Right", Lazer, Obstacles, [], 0, Answer).