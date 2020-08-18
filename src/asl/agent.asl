// Agent in project rastreamento_contatos

/* Initial beliefs and rules */

/* Initial goals */
!move.

/* Plans */
//Se est� sem sintomas aparentes e n�o est� em isolamento, anda aleatoriamente
+!move : not sintomas &
		 not isolado
	<-move_random;
	  !move.
	  
+!move : sintomas
	<-.print("Sinto sintomas, evitar movimenta��o!").
	  
+sintomas[source(percept)]
	<-.print("Desenvolvi sintomas!");
	  +isolado(14);
	  entra_isolamento.


/* Controle de contamina��o */
+contaminado [source(percept)] //Percep��o
	<- !cura(14); 
	  .print("Contaminado pelo ambiente (outro agente).").

+!cura(X) : X > 0
	<- !cura(X - 1);
	   .print("Contaminado em recupera��o, restando ", X, " dias");
	   em_recuperacao.

+!cura(X) : X == 0 //Fim da contamina��o
	<- -contaminado;
	   +imunizado;
	   curado.


/* Controle de Isolamento */
//Permanece isolado por X ciclos
+isolado(X) : X > 0
	<--isolado(X);
	  .print("Isolamento encerra em ", X, "dias");
	  +isolado(X-1).

//Fim do isolamento
+isolado(X) : X == 0
	<--isolado(0); 
	  sai_isolamento;
	  !move.

//Se desenvolve sintomas e tem o aplicativo, informa
+sintomas: tem_aplicativo
	<-informa_sintomas_app.
	
+morto[source(percept)]
	<-.my_name(NAME);
	  .kill_agent(NAME).
