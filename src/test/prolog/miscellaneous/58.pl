a(a).
a(b).
a(c).
a(d).
a(e).
a(f).
a(g).

b(1).
b(2).
b(3).
b(4).
b(5).

x(a, 1, Y, Z) :- b(Y), Y>3, b(Z), Z>3, !.
x(a, 2, Y, Z) :- b(Y), Y<3, b(Z), Z<3, !.
x(a, 3, Y, Z) :- b(Y), Y>3, b(Z), Z<3, !.
x(a, 4, Y, Z) :- b(Y), Y<3, b(Z), Z>3, !.
x(b, 1, Y, Z) :- b(Y), Y>3, b(Z), !, Z>3.
x(b, 2, Y, Z) :- b(Y), Y<3, b(Z), !, Z<3.
x(b, 3, Y, Z) :- b(Y), Y>3, b(Z), !, Z<3.
x(b, 4, Y, Z) :- b(Y), Y<3, b(Z), !, Z>3.
x(c, 1, Y, Z) :- b(Y), Y>3, !, b(Z), Z>3.
x(c, 2, Y, Z) :- b(Y), Y<3, !, b(Z), Z<3.
x(c, 3, Y, Z) :- b(Y), Y>3, !, b(Z), Z<3.
x(c, 4, Y, Z) :- b(Y), Y<3, !, b(Z), Z>3.
x(d, 1, Y, Z) :- b(Y), !, Y>3, b(Z), Z>3.
x(d, 2, Y, Z) :- b(Y), !, Y<3, b(Z), Z<3.
x(d, 3, Y, Z) :- b(Y), !, Y>3, b(Z), Z<3.
x(d, 4, Y, Z) :- b(Y), !, Y<3, b(Z), Z>3.
x(e, 1, Y, Z) :- !, b(Y), Y>3, b(Z), Z>3.
x(e, 2, Y, Z) :- !, b(Y), Y<3, b(Z), Z<3.
x(e, 3, Y, Z) :- !, b(Y), Y>3, b(Z), Z<3.
x(e, 4, Y, Z) :- !, b(Y), Y<3, b(Z), Z>3.
x(f, 1, Y, Z) :- b(Y), Y>3, b(Z), Z>3.
x(f, 2, Y, Z) :- b(Y), Y<3, b(Z), Z<3.
x(f, 3, Y, Z) :- b(Y), Y>3, b(Z), Z<3.
x(f, 4, Y, Z) :- b(Y), Y<3, b(Z), Z>3.

z :- a(A), b(B), x(A,B,Y,Z), write(A / B / Y / Z), nl, fail.

%QUERY z
%OUTPUT
%a / 1 / 4 / 4
%a / 2 / 1 / 1
%a / 3 / 4 / 1
%a / 4 / 1 / 4
%b / 2 / 1 / 1
%b / 3 / 4 / 1
%c / 1 / 4 / 4
%c / 1 / 4 / 5
%c / 2 / 1 / 1
%c / 2 / 1 / 2
%c / 3 / 4 / 1
%c / 3 / 4 / 2
%c / 4 / 1 / 4
%c / 4 / 1 / 5
%d / 2 / 1 / 1
%d / 2 / 1 / 2
%d / 4 / 1 / 4
%d / 4 / 1 / 5
%e / 1 / 4 / 4
%e / 1 / 4 / 5
%e / 1 / 5 / 4
%e / 1 / 5 / 5
%e / 2 / 1 / 1
%e / 2 / 1 / 2
%e / 2 / 2 / 1
%e / 2 / 2 / 2
%e / 3 / 4 / 1
%e / 3 / 4 / 2
%e / 3 / 5 / 1
%e / 3 / 5 / 2
%e / 4 / 1 / 4
%e / 4 / 1 / 5
%e / 4 / 2 / 4
%e / 4 / 2 / 5
%f / 1 / 4 / 4
%f / 1 / 4 / 5
%f / 1 / 5 / 4
%f / 1 / 5 / 5
%f / 2 / 1 / 1
%f / 2 / 1 / 2
%f / 2 / 2 / 1
%f / 2 / 2 / 2
%f / 3 / 4 / 1
%f / 3 / 4 / 2
%f / 3 / 5 / 1
%f / 3 / 5 / 2
%f / 4 / 1 / 4
%f / 4 / 1 / 5
%f / 4 / 2 / 4
%f / 4 / 2 / 5
%
%OUTPUT
%NO

test1(X,_,Y) :- X>1, !, repeat(3), Y is 3, !, X<4.
test2(X,Y):- a(X), b(Y), test1(Y, X, Z).

%QUERY b(Y), test1(Y, a, Z)
%ANSWER
% Y=2
% Z=3
%ANSWER
%ANSWER
% Y=3 
% Z=3
%ANSWER
%NO

%QUERY test2(X,Y)
%ANSWER
% X=a
% Y=2
%ANSWER
%ANSWER
% X=a
% Y=3 
%ANSWER
%ANSWER
% X=b
% Y=2 
%ANSWER
%ANSWER
% X=b
% Y=3 
%ANSWER
%ANSWER
% X=c
% Y=2 
%ANSWER
%ANSWER
% X=c
% Y=3 
%ANSWER
%ANSWER
% X=d
% Y=2 
%ANSWER
%ANSWER
% X=d
% Y=3 
%ANSWER
%ANSWER
% X=e
% Y=2 
%ANSWER
%ANSWER
% X=e
% Y=3 
%ANSWER
%ANSWER
% X=f
% Y=2 
%ANSWER
%ANSWER
% X=f
% Y=3 
%ANSWER
%ANSWER
% X=g
% Y=2 
%ANSWER
%ANSWER
% X=g
% Y=3
%ANSWER
%NO
