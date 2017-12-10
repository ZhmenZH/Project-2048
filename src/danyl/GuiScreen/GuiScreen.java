package danyl.GuiScreen;

import java.awt.*;
import java.awt.event.MouseEvent;

import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.util.HashMap;

public class GuiScreen {

    private static GuiScreen screen;
    private HashMap<String, GuiPanel> panels;
    private String currentPanel = "";

    private GuiScreen() {
        panels = new HashMap<String, GuiPanel>();
    }

    public static GuiScreen getInstance() {
        if (screen == null) {
            screen = new GuiScreen();
        }
        return screen;
    }

    public void update() {
        if (panels.get(currentPanel) != null) {
            panels.get(currentPanel).update();
        }
    }

    public void render(Graphics2D g) {
        if (panels.get(currentPanel) != null) {
            panels.get(currentPanel).render(g);
        }
    }

    public void add(String panelName, GuiPanel panel) {
        panels.put(panelName, panel);
    }

    public void setActualPanel(String panelName) {
        currentPanel = panelName;
    }

    public void mousePressed(MouseEvent e) {
        if (panels.get(currentPanel) != null) {
            panels.get(currentPanel).mousePressed(e);
        }
    }

    public void mouseReleased(MouseEvent e) {
        if (panels.get(currentPanel) != null) {
            panels.get(currentPanel).mouseReleased(e);
        }
    }

    public void mouseDragged(MouseEvent e) {
        if (panels.get(currentPanel) != null) {
            panels.get(currentPanel).mouseDragged(e);
        }
    }

    public void mouseMoved(MouseEvent e) {
        if (panels.get(currentPanel) != null) {
            panels.get(currentPanel).mouseMoved(e);
        }
    }
}

//
//import java.awt.*;
//import java.awt.event.MouseEvent;
//import java.util.HashMap;
//
//public class GuiScreen {
//
//    public static GuiScreen screen;
//    private HashMap<String, GuiPanel> panels;
//    private String actualPannel = "";
//
//    private GuiScreen()
//    {
//        panels = new HashMap<String, GuiPanel>();
//    }
//
//    public static GuiScreen getInstance()
//    {
//        if(screen == null)
//        {
//            screen = new GuiScreen();
//        }
//        return screen;
//    }
//
//    public void update()
//    {
//        if(panels.get(actualPannel) != null)
//        {
//            panels.get(actualPannel).update();
//        }
//    }
//
//    public void render(Graphics2D graphics2D)
//    {
//        if(panels.get(actualPannel) != null)
//        {
//            panels.get(actualPannel).render(graphics2D);
//        }
//    }
//
//    public void add(String nameOfThePanel, GuiPanel panel)
//    {
//        panels.put(nameOfThePanel,panel);
//    }
//
//    public void setActualPanel(String nameOfThePanel)
//    {
//        actualPannel = nameOfThePanel;
//    }
//
//    public void mousePressed(MouseEvent me)
//    {
//        if(panels.get(actualPannel) != null)
//        {
//            panels.get(actualPannel).mousePressed(me);
//        }
//    }
//
//    public void mouseRealeased(MouseEvent me)
//    {
//        if(panels.get(actualPannel) != null)
//        {
//            panels.get(actualPannel).mouseRealeased(me);
//        }
//    }
//
//    public void mouseMoved(MouseEvent me)
//    {
//        if(panels.get(actualPannel) != null)
//        {
//            panels.get(actualPannel).mouseMoved(me);
//        }
//    }
//
//    public void mouseDragged(MouseEvent me)
//    {
//        if(panels.get(actualPannel) != null)
//        {
//            panels.get(actualPannel).mouseDragged(me);
//        }
//    }
//}