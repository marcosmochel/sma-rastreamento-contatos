
import java.util.Random;
import java.util.logging.Logger;

import jason.asSyntax.Literal;
import jason.asSyntax.Structure;
import jason.environment.Environment;
import jason.environment.grid.Location;
import jason.stdlib.kill_agent;

public class City extends Environment {

	/* PARÂMETROS */
	public static final int GSize = 20; // grid size
	public static final int NumeroAgentes = 40;
	public static final int NumeroAgentesContaminados = 3;
	public static final int NumeroAgentesApp = 30;
	
	public static final float CHANCE_CONTAMINACAO = .5f;
	public static final float CHANCE_SINTOMATICO = .7f;
	public static final float CHANCE_MORTE = .025f;
	public static final float CHANCE_SINTOMAS_SEM_COVID = .1f;
	
	public static final int DIAS_MANIFESTACAO_SINTOMAS_INICIO = 3;
	public static final int DIAS_MANIFESTACAO_SINTOMAS_FIM = 5;
	
	public static final int DIAS_CONTAMINADO = 14;
	
   
	/* AÇÕES */
	public static final Literal MOVE_RANDOM = Literal.parseLiteral("move_random");
    public static final Literal ENTRA_ISOLAMENTO = Literal.parseLiteral("entra_isolamento");
    public static final Literal SAI_ISOLAMENTO = Literal.parseLiteral("sai_isolamento");
    public static final Literal EM_RECUPERACAO = Literal.parseLiteral("em_recuperacao");
    public static final Literal CURADO = Literal.parseLiteral("curado");
    public static final Literal INFORMA_SINTOMAS_APP = Literal.parseLiteral("informa_sintomas_app");
    public static final Literal VERIFICA_LISTA_APP = Literal.parseLiteral("verifica_lista_app");

    /* PERCEPÇÕES */
    public static final Literal CONTAMINADO = Literal.parseLiteral("contaminado");
    public static final Literal SINTOMAS = Literal.parseLiteral("sintomas");
    public static final Literal MORTO = Literal.parseLiteral("morto");
    public static final Literal TEM_APP = Literal.parseLiteral("tem_aplicativo");
    
    static Logger logger = Logger.getLogger(City.class.getName());
    
    private CityModel model;
    private CityView  view;
    
    private Random random = new Random(System.currentTimeMillis());

    public void init(String[] args) {
        model = new CityModel(GSize, NumeroAgentes, NumeroAgentesApp);
        view  = new CityView(model);
        
    	//Contaminação inicial
        for(int i = 0; i < NumeroAgentesContaminados; i++) {
        	model.addAgenteContaminado(i);
            addPercept(CityModel.AGENT_NAME_PREFIX+(i+1), CONTAMINADO);
        }
    	
        for(int i = 0; i < NumeroAgentesApp; i++) {
            addPercept(CityModel.AGENT_NAME_PREFIX+(i+1), TEM_APP);
        }
    }

    @Override
    public boolean executeAction(String ag, Structure action) {
    	int agId = model.getAgIdBasedOnName(ag);
        try {
            if (action.equals(MOVE_RANDOM)) {
                movimentar(agId);
            } else if(action.equals(ENTRA_ISOLAMENTO)) {
            	entraIsolamento(agId);
            } else if(action.equals(SAI_ISOLAMENTO)) {
            	saiIsolamento(agId);
            } else if(action.equals(EM_RECUPERACAO)) {
            	emRecuperacao(agId);
            } else if(action.equals(CURADO)) {
            	curado(agId);
	        } else if(action.equals(INFORMA_SINTOMAS_APP)) {
	        	logger.warning("[INFORMA_SINTOMAS_APP]A implementar");
	        } else if(action.equals(VERIFICA_LISTA_APP)) {
            	logger.warning("[VERIFICA_LISTA_APP]A implementar");
            } else {
            	logger.severe("Ação não implementada: " + action);
            }
            
            updateAgentStatus(agId);
            
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            Thread.sleep(30);
        } catch (Exception e) {
        	e.printStackTrace();
        }
        
        informAgsEnvironmentChanged();
        return true;
    }
    
	void entraIsolamento(int id) {
    	logger.info("Agente " + (id+1) + " entrando em isolamento.");
    	model.addAgenteIsolado(id);
    	
    	Location la = model.getAgPos(id);
    	model.add(CityView.ISOLATION, la.x, la.y);
    }
	
    private void saiIsolamento(int id) {
    	logger.info("Agente " + (id+1) + " saindo de isolamento.");
    	
    	model.removeAgenteIsolado(id);
    	
    	Location la = model.getAgPos(id);
    	model.remove(CityView.ISOLATION, la.x, la.y);
	}
    
    private void curado(int id) {
    	logger.info("Agente " + (id+1) + " curado.");
    	
    	model.removeAgenteContaminado(id);
    	model.removeAgenteSintomatico(id);
    	model.addAgenteImunizado(id);
    	
    	String agName = CityModel.AGENT_NAME_PREFIX + (id+1);
    	removePercept(agName, SINTOMAS);
    	removePercept(agName, CONTAMINADO);
	}
    
    private void emRecuperacao(int id) {
    	int diasContaminado = model.getAgenteDiasContaminado(id);
    	logger.info("Agente " + (id+1) + "contaminado há "+ diasContaminado  +" dias.");
    	if(CHANCE_MORTE > random.nextFloat()) {
    		String agentName = CityModel.AGENT_NAME_PREFIX + (id+1);
    		addPercept(agentName, MORTO);
    		logger.info("Agente " + (id+1) + " morreu.");
    	}
	}
    
    void movimentar(int id) throws Exception {
    	Location la = model.getAgPos(id);
    	
    	// movimento aleatorio para cima, baixo, esquerda e direita
    	int n = random.nextInt(4);
        switch(n) {
            case 0:
            	if(model.isFree(la.x + 1, la.y))
            		la.x++;
            	else
            		contatoAtPos(id, la.x + 1, la.y);
        	break;
            case 1:
            	if(model.isFree(id, la.x - 1, la.y))
            		la.x--;
            	else
            		contatoAtPos(id, la.x - 1, la.y);
        	break;
            case 2:
            	if(model.isFree(la.x, la.y + 1))
            		la.y++;
            	else
            		contatoAtPos(id, la.x, la.y + 1);
        	break;
            case 3:
            	if(model.isFree(la.x, la.y - 1))
            		la.y--;
            	else
            		contatoAtPos(id, la.x, la.y - 1);
        	break;
        }

        updatePosition(id, la);
        
    }

    public void contatoAtPos(int idAgOrigemContato, int x, int y) {
		int idAg = model.getAgAtPos(x, y);
		if(idAg > -1) { //Se há agente na posição na qual houve contato
			//ToDo: Considerar contaminação inversa também (agente que fez contato que seria contaminado)
        	if(model.isAgenteContaminado(idAgOrigemContato) && //Se agente que fez contato está contaminado
        	   !model.isAgenteContaminado(idAg) && //E agente contatado não está contaminado
        	   !model.isAgenteIsolado(idAg) && //E agente contatado não está isolado
    		   !model.isAgenteImunizado(idAg) && //E agente contatado não está imune
        	   CHANCE_CONTAMINACAO > random.nextFloat()){ //E se o agente contatado deu azar
        		logger.info("Agente contaminado: " + (idAg+1));
        		model.addAgenteContaminado(idAg);
        		addPercept(CityModel.AGENT_NAME_PREFIX+(idAg+1), CONTAMINADO);//ToDo: Só saber quando fizer o teste
        	}
        }
		
		//ToDo: Aplicativo!
//		String agente = CityModel.AGENT_NAME_PREFIX + id;
//		Literal lc = Literal.parseLiteral("contact(" + idAgOrigem + "," + idAg + ")");
//        addPercept(agName, lc);
//        lc = Literal.parseLiteral("contact(" + agNameContato + "," + agName + ")");
//        addPercept(agNameContato, lc);
	}

    void updatePosition(int agentId, Location l){
        model.setAgPos(agentId, l);
        
        // String agName = CityModel.AGENT_NAME_PREFIX + (agentId+1);
    }
    
    /*
     * Atualiza o status do agente a cada ação que este executa, de acordo com a situação atual deste.
     * e.g. Agente passa a ficar sintomático, pois está contaminado.
     */
    void updateAgentStatus(int agentId) {
    	String agName = CityModel.AGENT_NAME_PREFIX + (agentId+1);
    	
    	//Verifica se deve desenvolver sintomas
    	if(model.isAgenteContaminado(agentId) && 
    	   !model.isAgenteSintomatico(agentId)  &&
    	   model.getAgenteDiasContaminado(agentId) >= DIAS_MANIFESTACAO_SINTOMAS_INICIO &&
    	   model.getAgenteDiasContaminado(agentId) <= DIAS_MANIFESTACAO_SINTOMAS_FIM &&
    	   !model.isAgenteImunizado(agentId) &&
    	   random.nextFloat() < CHANCE_SINTOMATICO) {
    		
			model.addAgenteSintomatico(agentId);
			addPercept(agName, SINTOMAS);
			
			logger.info("Agente " + (agentId+1) + " desenvolveu sintomas.");
    	}
    	    	
    	model.cycle(agentId);
    }
}