import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import jason.environment.grid.GridWorldView;

public @SuppressWarnings("serial")
class CityView extends GridWorldView {
	
	static final int HOUSE  = 16; // house code in grid model
	
	static final Color verde = new Color(30, 130, 30);
	static final Color amarelo = new Color(240, 240, 0);
	
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
        	case CityView.HOUSE:
        		drawObstacle(g, x, y);
            break;
        }
    }

    @Override
    public void drawAgent(Graphics g, int x, int y, Color c, int id) {
    	if(model.isAgenteSintomatico(id))
    		c = Color.ORANGE;
    	else if(model.isAgenteContaminado(id)) 
        	c = amarelo;
        else
        	c = verde;
        super.drawAgent(g, x, y, c, -1);
        
        String label = "A"+(id+1);
        g.setColor(Color.white);
        super.drawString(g, x, y, defaultFont, label);

        //repaint();
    }

}