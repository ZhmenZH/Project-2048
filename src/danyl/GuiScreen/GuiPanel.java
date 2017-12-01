package danyl.GuiScreen;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class GuiPanel {

    private ArrayList <MyButton> buttons;

    public GuiPanel()
    {
        buttons = new ArrayList<MyButton>();
    }

    public void update()
    {
        for(MyButton but: buttons)
        {
            but.update();
        }
    }

    public void render(Graphics2D graphics2D)
    {
        for(MyButton but: buttons)
        {
            but.render(graphics2D);
        }
    }

    public void addButton(MyButton button)
    {
        buttons.add(button);
    }

    public void removeButton(MyButton button)
    {
        buttons.remove(button);
    }

    public void mousePressed(MouseEvent me)
    {
        for(MyButton but: buttons)
        {
            but.mousePressed(me);
        }
    }

    public void mouseRealeased(MouseEvent me)
    {
        for(MyButton but: buttons)
        {
            but.mouseReleased(me);
        }
    }

    public void mouseMoved(MouseEvent me)
    {
        for(MyButton but: buttons)
        {
            but.mouseMoved(me);
        }
    }

    public void mouseDragged(MouseEvent me)
    {
        for(MyButton but: buttons)
        {
            but.mouseDragged(me);
        }
    }
}
