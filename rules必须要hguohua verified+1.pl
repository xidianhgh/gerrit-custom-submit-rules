is_user(user(1000002)).


submit_rule(submit(Fix)) :-
    Fix = label('Verified', need(_)).


submit_rule(submit(Fix)) :-
    gerrit:default_submit(X),
	
	gerrit:commit_label(label('Verified', V), U),
	gerrit:commit_author(Us,Fu,Em),
	V >= 1,
	U = user(1000002),
    Fix = label('Verified', ok(U)).


starts_with(L, []).
starts_with([H|T1], [H|T2]) :- starts_with(T1, T2).