sum_list([], 0).
sum_list([H | Rest], Sum) :- sum_list(Rest,Tmp), Sum is H + Tmp.

xxx('hguohua').
is_user(x) :- xxx(x).

add_category_min_score(In, Category, Min,  P) :-
    findall(X, gerrit:commit_label(label(Category,X),R),Z),
    sum_list(Z, Sum),
    Sum >= Min, !,
    gerrit:commit_label(label(Category, V), U),
    V >= 1,
    !,
    P = [label(Category,ok(U)) | In].

add_category_min_score(In, Category,Min,P) :-
    P = [label(Category,need(Min)) | In].

add_category_min_score(In, Category,Min,P) :-
    gerrit:commit_label(label(Category, V), U),
    V >= 1,
	U = user(1000002),
	P = [label(Category,ok(U)) | In].



user_verify(In, Category,Min,P) :-
	U = user(1000002),
    gerrit:commit_label(label(Category, V), U),
	gerrit:commit_author(Us,Fu,Em),
	is_user(Fu),
	P = [label(Category,ok(U)) | In].
	
test(U) :-
	U = user(1000002).
	


submit_rule(S) :-
    gerrit:default_submit(X),
    X =.. [submit | Ls],
    gerrit:remove_label(Ls,label('Verified',_),NoCR),
	user_verify(NoCR,'Verified', 1, Labels),
    S =.. [submit | Labels].