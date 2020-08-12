// Agent in project rastreamento_contatos

/* Initial beliefs and rules */
/* Commented beliefs are for furter use and will be initialized by enviroment */

//isolado.
//imunizado.
//sintomas.
//tem_aplicativo.
//pos(X, Y)


/* Initial goals */
!move.

/* Plans */
//Se está sem sintomas aparentes e não está em isolamento, anda aleatoriamente
+!move : not sintomas &
		 not isolado
	<-move_random;
	  !move.

//Se está com sintomas aparentes e não está em isolamento, entra em isolamento por 14 dias
+!move :     sintomas &
		 not isolado
	<- +isolado(14);
		entra_isolamento.

//Se recupera até o 14º dia	  
+isolado(DIAS) : DIAS > 0
	<--+isolado(DIAS-1);
		em_recuperacao.

+isolado(DIAS) : tem_aplicativo
	<-informa_sintomas_app.

//Recuperado	  
+isolado(DIAS) : DIAS == 0
	<--isolado(DIAS);
	  +imunizado;
	  -sintomas;
	  -contaminado;
	  sai_isolamento;
	  !move.

-contaminado
	<- agente_recuperado.
