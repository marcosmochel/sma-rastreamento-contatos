import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import jason.environment.grid.GridWorldModel;
import jason.environment.grid.Location;

public class CityModel extends GridWorldModel {
	public static final String AGENT_NAME_PREFIX = "agent";

	private final Random random = new Random(System.currentTimeMillis());
    
    private int numeroAgentes;
    private int numeroAgentesApp;
    private int gridSize;
    
    private Map<Integer, Integer> agentesDiasContaminados;
    private Map<Integer, Integer> agentesDiasSintomaticos;
    private Map<Integer, Integer> agentesDiasIsolados;
    private Map<Integer, Integer> agentesDiasImunizados;
    
    private Map<Integer, Integer> contatosApp;

	public CityModel(int gSize, int numeroAgentes, int numeroAgentesApp) {
        super(gSize, gSize, numeroAgentes);
        
    	this.gridSize = gSize;
    	this.numeroAgentes = numeroAgentes;
    	this.numeroAgentesApp = numeroAgentesApp;
    	
    	this.agentesDiasContaminados = new HashMap<Integer, Integer>();
    	this.agentesDiasSintomaticos = new HashMap<Integer, Integer>();
    	this.agentesDiasIsolados = new HashMap<Integer, Integer>();
    	this.agentesDiasImunizados = new HashMap<Integer, Integer>();
    	
        // Inicializa agentes
        try {
        	for (int i = 0; i < numeroAgentes; i++) {
        		//Localização inicial aleatória
            	int randomPosX = random.nextInt(gSize);
            	int randomPosY = random.nextInt(gSize);
            	setAgPos(i, randomPosX, randomPosY);
            	
                //ToDo: Inicializa alguns agentes com App
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    int getAgIdBasedOnName(String agName) {
        return (Integer.parseInt(agName.substring(5))) - 1;
    }
    
    String getAgentByPos(int x, int y) {    	
    	for (int i = 0; i < numeroAgentes; i++) {
    		Location la = getAgPos(i);
    		if(la.x == x && la.y == y)
    			return CityModel.AGENT_NAME_PREFIX+(i+1);
		}
    	return "";
    }
    
    public boolean isAgenteContaminado(int agId) {
    	return agentesDiasContaminados.containsKey(agId);
    }

    public int getAgenteDiasContaminado(int agId) {
    	return agentesDiasContaminados.get(agId);
    }

	public void addAgenteContaminado(int idAg) {
		agentesDiasContaminados.put(idAg, 0);
	}
	
	public void removeAgenteContaminado(int idAg) {
		agentesDiasContaminados.remove(idAg);
	}
	
    public boolean isAgenteImunizado(int agId) {
    	return agentesDiasImunizados.containsKey(agId);
    }

    public int getAgenteDiasImunizados(int agId) {
    	return agentesDiasImunizados.get(agId);
    }
    
	public void addAgenteImunizado(int idAg) {
		agentesDiasImunizados.put(idAg, 0);
	}
	
    public boolean isAgenteSintomatico(int agId) {
    	return agentesDiasSintomaticos.containsKey(agId);
    }
    
    public int getAgenteDiasSintomaticos(int agId) {
    	return agentesDiasSintomaticos.get(agId);
    }

	public void addAgenteSintomatico(int idAg) {
		agentesDiasSintomaticos.put(idAg, 0);
	}
	
	public void removeAgenteSintomatico(int idAg) {
		agentesDiasSintomaticos.remove(idAg);
	}
	
	public boolean isAgenteIsolado(int agId) {
    	return agentesDiasIsolados.containsKey(agId);
    }
	
    public int getAgenteDiasIsolado(int agId) {
    	return agentesDiasIsolados.get(agId);
    }

	public void addAgenteIsolado(int idAg) {
		agentesDiasIsolados.put(idAg, 0);
	}
	
	public void removeAgenteIsolado(int idAg) {
		agentesDiasIsolados.remove(idAg);
	}

	public void cycle(int agentId) {
		if(agentesDiasContaminados.containsKey(agentId))
			agentesDiasContaminados.put(agentId, agentesDiasContaminados.get(agentId) + 1);
		if(agentesDiasImunizados.containsKey(agentId))
			agentesDiasImunizados.put(agentId, agentesDiasImunizados.get(agentId) + 1);
		if(agentesDiasSintomaticos.containsKey(agentId))
			agentesDiasSintomaticos.put(agentId, agentesDiasSintomaticos.get(agentId) + 1);
		if(agentesDiasIsolados.containsKey(agentId))
			agentesDiasIsolados.put(agentId, agentesDiasIsolados.get(agentId) + 1);
	}

	
	/* Getters 'n Setters */
	
	
}