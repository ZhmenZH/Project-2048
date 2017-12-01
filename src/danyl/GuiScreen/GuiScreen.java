package danyl.GuiScreen;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.HashMap;

public class GuiScreen {

    public static GuiScreen screen;
    private HashMap<String, GuiPanel> panels;
    private String actualPannel = "";

    private GuiScreen()
    {
        panels = new HashMap<String, GuiPanel>();
    }

    public static GuiScreen getInstance()
    {
        if(screen == null)
        {
            screen = new GuiScreen();
        }
        return screen;
    }

    public void update()
    {
        if(panels.get(actualPannel) != null)
        {
            panels.get(actualPannel).update();
        }
    }

    public void render(Graphics2D graphics2D)
    {
        if(panels.get(actualPannel) != null)
        {
            panels.get(actualPannel).render(graphics2D);
        }
    }

    public void add(String nameOfThePanel, GuiPanel panel)
    {
        panels.put(nameOfThePanel,panel);
    }

    public void setActualPanel(String nameOfThePanel)
    {
        actualPannel = nameOfThePanel;
    }

    public void mousePressed(MouseEvent me)
    {
        if(panels.get(actualPannel) != null)
        {
            panels.get(actualPannel).mousePressed(me);
        }
    }

    public void mouseRealeased(MouseEvent me)
    {
        if(panels.get(actualPannel) != null)
        {
            panels.get(actualPannel).mouseRealeased(me);
        }
    }

    public void mouseMoved(MouseEvent me)
    {
        if(panels.get(actualPannel) != null)
        {
            panels.get(actualPannel).mouseMoved(me);
        }
    }

    public void mouseDragged(MouseEvent me)
    {
        if(panels.get(actualPannel) != null)
        {
            panels.get(actualPannel).mouseDragged(me);
        }
    }
}
