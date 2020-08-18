import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import jason.environment.grid.GridWorldView;

public @SuppressWarnings("serial")
class CityView extends GridWorldView {
	
	static final int ISOLATION  = 16; // house code in grid model
	
	static final Color VERDE = new Color(30, 130, 30);
	static final Color AMARELO = new Color(240, 240, 0);
	
	CityModel model;

    public CityView(CityModel model) {
        super(model, "Cidade", 600);
        this.model = model;
        defaultFont = new Font("Arial", Font.PLAIN, 6); // change default font
        setVisible(true);
        repaint();
    }

    /** draw application objects */
    @Override
    public void draw(Graphics g, int x, int y, int element) {
        switch (element) {
        	case CityView.ISOLATION:
        		drawObstacle(g, x, y);
            break;
        }
    }
    

    @Override
    public void drawAgent(Graphics g, int x, int y, Color agColor, int id) {
    	
    	if(model.isAgenteSintomatico(id)) {
    		agColor = Color.ORANGE;
    	} else if(model.isAgenteContaminado(id)) {
    		agColor = AMARELO;
    	} else if (model.isAgenteImunizado(id)){    		agColor = Color.BLUE;
    	} else {
    		agColor = VERDE;
    	}
    	
        super.drawAgent(g, x, y, agColor, -1);
        
        String label = "A"+(id+1);
        g.setColor(Color.white);
        super.drawString(g, x, y, defaultFont, label);

        //repaint();
    }

}