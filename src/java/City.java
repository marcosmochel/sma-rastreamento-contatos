
import java.util.Random;
import java.util.logging.Logger;

import jason.asSyntax.Literal;
import jason.asSyntax.Structure;
import jason.environment.Environment;
import jason.environment.grid.Location;

public class City extends Environment {

	/* PARÂMETROS */
	public static final int GSize = 20; // grid size
	public static final int NumeroAgentes = 30;
	public static final int NumeroAgentesApp = 15;
	public static final int NumeroCicloParaSintomas = 100;
	
	public static final float CHANCE_CONTAMINACAO = .5f;
	public static final float CHANCE_SINTOMATICO = .7f;
	public static final float CHANCE_MORTE = .025f;
	public static final float CHANCE_SINTOMAS_SEM_COVID = .1f;
	
	public static final int DIAS_MANIFESTACAO_SINTOMAS = 5;
	
    
	/* AÇÕES */
	public static final Literal MOVE_RANDOM = Literal.parseLiteral("move_random");
	public static final Literal AGENTE_RECUPERADO = Literal.parseLiteral("agente_recuperado");
    public static final Literal ENTRA_ISOLAMENTO = Literal.parseLiteral("entra_isolamento");
    public static final Literal SAI_ISOLAMENTO = Literal.parseLiteral("sai_isolamento");
    public static final Literal EM_RECUPERACAO = Literal.parseLiteral("em_recuperacao");
    public static final Literal INFORMA_SINTOMAS_APP = Literal.parseLiteral("informa_sintomas_app");
    public static final Literal VERIFICA_LISTA_APP = Literal.parseLiteral("verifica_lista_app");

    /* CRENÇAS */
    public static final Literal CONTAMINADO = Literal.parseLiteral("contaminado");
    public static final Literal SINTOMAS = Literal.parseLiteral("sintomas");
    
    static Logger logger = Logger.getLogger(City.class.getName());
    
    private CityModel model;
    private CityView  view;
    
    private Random random = new Random(System.currentTimeMillis());

    public void init(String[] args) {
        model = new CityModel(GSize, NumeroAgentes, NumeroAgentesApp);
        view  = new CityView(model);
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
            	logger.warning("[SAI_ISOLAMENTO]A implementar");
            } else if(action.equals(EM_RECUPERACAO)) {
            	logger.warning("[EM_RECUPERACAO]A implementar");
            } else if(action.equals(INFORMA_SINTOMAS_APP)) {
            	logger.warning("[INFORMA_SINTOMAS_APP]A implementar");
            } else if(action.equals(VERIFICA_LISTA_APP)) {
            	logger.warning("[VERIFICA_LISTA_APP]A implementar");
            }
            
            updateAgentStatus(agId);
            
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            Thread.sleep(10);
        } catch (Exception e) {
        	e.printStackTrace();
        }
        
        informAgsEnvironmentChanged();
        return true;
    }
    
    void entraIsolamento(int id) throws Exception {
    	logger.info("Agente " + (id+1) + " entrando em isolamento.");
    	Location la = model.getAgPos(id);
    	model.add(CityView.HOUSE, la.x, la.y);
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
			//ToDo: Considerar contaminação inversa também (agente que fez contato que foi contaminado)
        	if(model.isAgenteContaminado(idAgOrigemContato) && //Se agente que fez contato está contaminado 
        	   CHANCE_CONTAMINACAO > random.nextFloat()){ //E se o agente contatado deu azar
        		logger.info("Agente contaminado: " + (idAg+1));
        		model.addAgenteContaminado(idAg);
        		//addPercept(agContatadoName, CONTAMINADO);//ToDo: Só vai saber quando fizer o teste
        	}
        }
		
	}

    void updatePosition(int agentId, Location l){
    	// seta a nova posicao
        model.setAgPos(agentId, l);
        
        // ToDo: atualiza a percepcao de localizacao
        // String agName = CityModel.AGENT_NAME_PREFIX + (agentId+1);
    }
    
    /*
     * Atualiza o status do agente a cada ação que este executa, de acordo com a situação atual deste.
     * e.g. Agente passa a ficar sintomático, pois está contaminado.
     */
    void updateAgentStatus(int agentId) {
    	if(model.isAgenteContaminado(agentId)) {
    		//Se estiver entre o m-ésimo e n-ésimo dia de contaminação, há uma change de manifestar sintomas
    		if(!model.isAgenteSintomatico(agentId) &&
    		   model.getAgenteDiasContaminado(agentId) == DIAS_MANIFESTACAO_SINTOMAS &&
    		   random.nextFloat() < CHANCE_SINTOMATICO) {
    			model.addAgenteSintomatico(agentId);
    			addPercept(CityModel.AGENT_NAME_PREFIX + (agentId+1), SINTOMAS);
    			logger.info("Agente " + (agentId+1) + " desenvolveu sintomas.");
    		}
    	}
    	
    	model.cycle(agentId);
    }
}