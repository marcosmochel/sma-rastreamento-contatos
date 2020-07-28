// Internal action code for project rastreamento_contatos

//package rastreamento_contatos;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.util.Random;
import java.util.logging.Logger;

import jason.asSyntax.*;
import jason.environment.Environment;
import jason.environment.grid.GridWorldModel;
import jason.environment.grid.GridWorldView;
import jason.environment.grid.Location;

/**
 * @author Marco
 *
 */
public class City extends Environment {

	public static final int GSize = 20; // grid size
	public static final int AgentNumber = 8;

    public static final Literal MoveRandom = Literal.parseLiteral("move_random");

    static Logger logger = Logger.getLogger(City.class.getName());
    
    private CityModel model;
    private CityView  view;

    public void init(String[] args) {
        model = new CityModel();
        view  = new CityView(model);
        model.setView(view);
    }

    @Override
    public boolean executeAction(String ag, Structure action) {
    	int agId = getAgIdBasedOnName(ag);
        try {
            if (action.equals(MoveRandom)) {
                model.movimentar(agId);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            Thread.sleep(100);
        } catch (Exception e) {	
        }
        
        informAgsEnvironmentChanged();
        return true;
    }
    
    private int getAgIdBasedOnName(String agName) {
        return Integer.parseInt(agName.substring(5)) - 1;
    }


    void updatePercepts(int id, int x, int y){
    	String agName = "agent"+(id+1);
        clearPercepts(agName);
        Literal pos = Literal.parseLiteral("position("+x+", "+y+")");
        addPercept(agName, pos);
    }
    
    /**
     * Modelo da cidade, que armazenará os dados inerentes ao ambiente
     *
     */
    class CityModel extends GridWorldModel {
        Random random = new Random(System.currentTimeMillis());
        
        //Matriz de contatos
        Boolean[][] contatos = new Boolean[AgentNumber][AgentNumber];

        private CityModel() {
            super(GSize, GSize, AgentNumber);

            // initial location of agents
            try {
            	for (int i = 0 ; i < AgentNumber; i++) {
                	int randomPosX = random.nextInt(GSize);
                	int randomPosY = random.nextInt(GSize);
            		setAgPos(i, randomPosX, randomPosY);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        void movimentar(int id) throws Exception {
        	Location la = getAgPos(id);
        	
        	// movimento aleatorio para cima, baixo, esquerda e direita
        	int n = random.nextInt(4);
            switch(n) {
	            case 0:
	            	la.x++;
	            	break;
	            case 1:
	            	la.x--;
	            	break;
	            case 2:
	            	la.y++;
	            	break;
	            case 3:
	            	la.y--;
	            	break;
            }
            
            // corrige o movimento caso o agente tente sair do ambiente
            if (la.x == GSize) {
                la.x--;
            } else if (la.x == -1) {
            	la.x++;
            }
            if (la.y == GSize) {
                la.y--;
            } else if (la.y == -1) {
            	la.y++;
            }
 
            // seta a nova posição
            setAgPos(id, la);
            // atualiza a percepção de localizacao
            updatePercepts(id, la.x, la.y);
        }

    }

    /*
     * Classe View, que manipula o comportamente de renderização do ambiente
     */
    @SuppressWarnings("serial")
	class CityView extends GridWorldView {

        public CityView(CityModel model) {
            super(model, "Cidade", 800);
            defaultFont = new Font("Arial", Font.BOLD, 10); // change default font
            setVisible(true);
            repaint();
        }

        /** draw application objects */

        @Override
        public void drawAgent(Graphics g, int x, int y, Color c, int id) {
            String label = "A"+(id+1);
            c = Color.blue;
            
            super.drawAgent(g, x, y, c, -1);
            g.setColor(Color.white);
            super.drawString(g, x, y, defaultFont, label);
            repaint();
        }

    }
}
