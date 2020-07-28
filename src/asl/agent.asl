// Agent in project rastreamento_contatos

/* Initial beliefs and rules */

vivo.
sintomas_gripais.
tem_aplicativo. //RANDOM no env?


/* Initial goals */

!move.

/* Plans */
+!move : true
	<-move_random
	!move.



