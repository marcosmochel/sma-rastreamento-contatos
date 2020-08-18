// Agent in project rastreamento_contatos

/* Initial beliefs and rules */

/* Initial goals */
!move.

/* Plans */
//Se está sem sintomas aparentes e não está em isolamento, anda aleatoriamente
+!move : not sintomas &
		 not isolado
	<-move_random;
	  !move.
	  
+!move : sintomas
	<-.print("Sinto sintomas, evitar movimentação!").
	  
+sintomas[source(percept)]
	<-.print("Desenvolvi sintomas!");
	  +isolado(14);
	  entra_isolamento.


/* Controle de contaminação */
+contaminado [source(percept)] //Percepção
	<- !cura(14); 
	  .print("Contaminado pelo ambiente (outro agente).").

+!cura(X) : X > 0
	<- !cura(X - 1);
	   .print("Contaminado em recuperação, restando ", X, " dias");
	   em_recuperacao.

+!cura(X) : X == 0 //Fim da contaminação
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
