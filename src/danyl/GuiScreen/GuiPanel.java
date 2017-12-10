package danyl.GuiScreen;

import javax.swing.*;
import java.awt.event.MouseEvent;
import java.awt.Graphics2D;
import java.util.ArrayList;

public class GuiPanel extends JPanel{

    private ArrayList<GuiButton> buttons;

    public GuiPanel(){
        buttons = new ArrayList<GuiButton>();
    }

    public void update(){
        for(GuiButton b : buttons){
            b.update();
        }
    }

    public void render(Graphics2D g){
        for(GuiButton b : buttons){
            b.render(g);
        }
    }

    public void addButton(GuiButton button){
        buttons.add(button);
    }

    public void removeButton(GuiButton button){
        buttons.remove(button);
    }

    public void mousePressed(MouseEvent e){
        for(GuiButton b : buttons){
            b.mousePressed(e);
        }
    }

   public void mouseReleased(MouseEvent e){
        for(GuiButton b : buttons){
            b.mouseReleased(e);
        }
    }

    public void mouseDragged(MouseEvent e){
        for(GuiButton b : buttons){
            b.mouseDragged(e);
        }
    }

    public void mouseMoved(MouseEvent e){
        for(GuiButton b : buttons){
            b.mouseMoved(e);
        }
    }
}