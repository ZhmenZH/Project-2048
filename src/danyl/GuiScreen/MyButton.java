package danyl.GuiScreen;

import danyl.Game.DrawUtils;
import danyl.Game.Game;

import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class MyButton {

    private State actualState = State.RELEASED;
    private Rectangle clickWindow;
    private ArrayList<ActionListener> actionListeners;
    private String message = "";

    private Color released;
    private Color pressed;
    private Color hover;
    private Font font = Game.font.deriveFont(28f);

    //private Audio audio;

    public MyButton(int x, int y, int width, int height)
    {
        clickWindow = new Rectangle(x,y,width,height);
        actionListeners = new ArrayList<ActionListener>();
        released = new Color(173,177,179);
        pressed = new Color(101,13,106);
        hover = new Color(11,33,77);

// audio = Audio;
        // audio.load(select);
    }

    public void update()
    {

    }

    public void render(Graphics2D graphics2D)
    {
        if(actualState == State.RELEASED)
        {
            graphics2D.setColor(released);
            graphics2D.fill(clickWindow);
        }
        else if(actualState == State.PRESSED)
        {
            graphics2D.setColor(pressed);
            graphics2D.fill(clickWindow);
        }
        else{
            graphics2D.setColor(hover);
            graphics2D.fill(clickWindow);
        }

        graphics2D.setColor(Color.gray);
        graphics2D.setFont(font);
        graphics2D.drawString(message,clickWindow.x + clickWindow.width/2 - DrawUtils.getMessageWidth(message,font,graphics2D)/2,
                clickWindow.y + clickWindow.height/2 - DrawUtils.getMessageHeight(message,font,graphics2D)/2);

    }

    public void addActionListener(ActionListener actionListener)
    {
        actionListeners.add(actionListener);
    }

    public void mousePressed(MouseEvent me)
    {
        if(clickWindow.contains(me.getPoint()))
        {
            actualState = State.PRESSED;
        }
    }

    public void mouseReleased(MouseEvent me)
    {
        if(clickWindow.contains(me.getPoint()))
        {
            for(ActionListener al : actionListeners)
            {
                al.actionPerformed(null);
            }
            // audio.play();
        }

        actualState = State.RELEASED;
    }

    public void mouseDragged(MouseEvent me)
    {
        if(clickWindow.contains(me.getPoint()))
        {
            actualState = State.PRESSED;
        }
        else
        {
            actualState = State.RELEASED;
        }
    }

    public void mouseMoved(MouseEvent me)
    {
        if(clickWindow.contains(me.getPoint()))
        {
            actualState = State.HOVER;
        }
        else
        {
            actualState = State.RELEASED;
        }
    }

    public int getX()
    {
        return clickWindow.x;
    }

    public int getY()
    {
        return clickWindow.y;
    }

    public int getWidth()
    {
       return clickWindow.width;
    }

    public int getHeight()
    {
        return clickWindow.height;
    }

    public void setMessage(String message)
    {
        this.message = message;
    }
}
