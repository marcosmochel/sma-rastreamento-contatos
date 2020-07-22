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

public class City extends Environment {

	public static final int GSize = 10; // grid size

    public static final Literal mr = Literal.parseLiteral("move_random(Me)");

    static Logger logger = Logger.getLogger(City.class.getName());
    
    private CityModel model;
    private CityView  view;

    @Override
    public void init(String[] args) {
        model = new CityModel();
        view  = new CityView(model);
        model.setView(view);
        //updatePercepts();
    }

    @Override
    public boolean executeAction(String ag, Structure action) {
        try {
        	int agId = getAgIdBasedOnName(ag);
            if (action.getFunctor().equals("move_random")) {
                model.movimentar(agId);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        updatePercepts();

        try {
            Thread.sleep(500);
        } catch (Exception e) {}
        informAgsEnvironmentChanged();
        return true;
    }
    
    private int getAgIdBasedOnName(String agName) {
        return (Integer.parseInt(agName.substring(5))) - 1;
    }



    void updatePercepts() {
        clearPercepts();
        
        Literal m = Literal.parseLiteral("move(a)");
        
        addPercept(m);
    }

    class CityModel extends GridWorldModel {
        Random random = new Random(System.currentTimeMillis());

        private CityModel() {
            super(GSize, GSize, 5);

            // initial location of agents
            try {
            	for (int i = 0; i < 5; i++) {
            		setAgPos(i, 0, 0);
                }
                

                //Location r2Loc = new Location(GSize/2, GSize/2);
                //setAgPos(1, r2Loc);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        void movimentar(int id) throws Exception {
        	Location la = getAgPos(id);
        	
        	logger.info("Posição Agente "+ (id+1) +": " + la.x +" - "+ la.y);
        	
        	Random random = new Random();
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
            
            if (la.x == getWidth()) {
                la.x--;
            } else if (la.x == -1) {
            	la.x++;
            }
            if (la.y == getHeight()) {
                la.y--;
            } else if (la.y == -1) {
            	la.y++;
            }
            for (int i=0; i<5; i++) {
            	if (i == id) {
            		setAgPos(id, la);
            	} else {
            		//setAgPos(i, getAgPos(i));
            	}
            }
        }

    }

    class CityView extends GridWorldView {

        public CityView(CityModel model) {
            super(model, "Cidade", 600);
            defaultFont = new Font("Arial", Font.BOLD, 18); // change default font
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
