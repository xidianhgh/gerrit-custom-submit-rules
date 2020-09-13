submit_filter(In, Out) :-
    In =.. [submit | Ls],
    remove_verified_category(Ls, R),
    Out =.. [submit | R].

remove_verified_category([], []).
remove_verified_category([label('Verified', _) | T], R) :- remove_verified_category(T, R), !.
remove_verified_category([H|T], [H|R]) :- remove_verified_category(T, R).
	

