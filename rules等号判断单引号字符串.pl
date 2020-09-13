is_user(user(1000002)).


submit_rule(submit(Fix)) :-
    Fix = label('Verified', need(_)).


submit_rule(submit(Fix)) :-
    gerrit:default_submit(X),
	gerrit:commit_label(LB, U),
	gerrit:commit_author(Us,Fu,Em),
	Fu = 'hguohua',
    Fix = label('Verified', ok(U)).


starts_with(L, []).
starts_with([H|T1], [H|T2]) :- starts_with(T1, T2).