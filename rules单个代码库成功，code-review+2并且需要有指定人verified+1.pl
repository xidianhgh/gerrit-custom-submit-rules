


submit_rule(submit(V, CR)) :-
    user_verified(submit(V, CR)),
    gerrit:max_with_block(-2, 2, 'Code-Review', CR),
    gerrit:max_with_block(-1, 1, 'Verified', V).


submit_rule(submit(NX, NY)) :-
    NX = label('Code-Review', need(_)),
    NY = label('Verified', need(_)).

user_verified(submit(Fix, W)) :-
    gerrit:default_submit(X),
	
	gerrit:commit_label(label('Code-Review', VV), UU),
	gerrit:commit_label(label('Verified', V), U),
	gerrit:commit_author(Us,Fu,Em),
	V >= 1,
	VV >= 0,
	U = user(1000002),
    Fix = label('Verified', ok(U)).
