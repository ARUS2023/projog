test1(X, Y) :- X = Y.

%QUERY test1(X,X)
%ANSWER X=UNINSTANTIATED VARIABLE

%QUERY test1(X,Y)
%ANSWER
% X=UNINSTANTIATED VARIABLE
% Y=UNINSTANTIATED VARIABLE
%ANSWER

%QUERY test1(X,1)
%ANSWER X=1

%QUERY test1(a,X)
%ANSWER X=a

p(1).
p(2).
p(3).

test2(X, Y) :- p(X), p(Y).

%QUERY test2(X,Y)
%ANSWER
% X=1
% Y=1
%ANSWER
%ANSWER
% X=1
% Y=2
%ANSWER
%ANSWER
% X=1
% Y=3
%ANSWER
%ANSWER
% X=2
% Y=1
%ANSWER
%ANSWER
% X=2
% Y=2
%ANSWER
%ANSWER
% X=2
% Y=3
%ANSWER
%ANSWER
% X=3
% Y=1
%ANSWER
%ANSWER
% X=3
% Y=2
%ANSWER
%ANSWER
% X=3
% Y=3
%ANSWER

%QUERY test2(X,X)
%ANSWER X=1
%ANSWER X=2
%ANSWER X=3

%QUERY test2(1,Y)
%ANSWER Y=1
%ANSWER Y=2
%ANSWER Y=3

%TRUE test2(1,3)

test3(X, X) :- p(X), p(X).

%QUERY test3(X,Y)
%ANSWER
% X=1
% Y=1
%ANSWER
%ANSWER
% X=2
% Y=2
%ANSWER
%ANSWER
% X=3
% Y=3
%ANSWER

%QUERY test3(1,Y)
%ANSWER
% Y=1
%ANSWER

%TRUE test3(3,3)
%FALSE test3(1,3)

test4(X, Y) :- test1(X, Y), Y=1.
%TRUE test4(1,1)
%FALSE test4(2,2)
%QUERY test4(1,Y)
%ANSWER Y=1
%QUERY test4(Y,1)
%ANSWER Y=1
%QUERY test4(X,Y)
%ANSWER
% X=1
% Y=1
%ANSWER

test5(X, Y) :- test1(X, Y), X=1.
%TRUE test5(1,1)
%FALSE test5(2,2)
%QUERY test5(1,Y)
%ANSWER Y=1
%QUERY test5(Y,1)
%ANSWER Y=1
%QUERY test5(X,Y)
%ANSWER
% X=1
% Y=1
%ANSWER

