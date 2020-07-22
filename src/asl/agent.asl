// Agent sample_agent in project rastreamento_contatos

/* Initial beliefs and rules */


/* Initial goals */

!move(a).

/* Plans */
+!move(a) : true
	<-move_random(Me)
	!move(a).
+!move(a).


