package danyl.GuiScreen;

import danyl.Game.DrawUtils;
import danyl.Game.Game;
import danyl.Game.Sound;

import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.io.File;
import java.util.ArrayList;

public class GuiButton {

    private State currentState = State.RELEASED;
    private Rectangle clickBox;
    private ArrayList<ActionListener> actionListeners;
    private String text = "";

    private Color main;
    private Color hover;
    private Color pressed;
    private Font font = Game.font.deriveFont(22f);

    private Sound audio;

    public GuiButton(int x, int y, int width, int height){
        audio = new Sound(new File("select.wav"));

        clickBox = new Rectangle(x, y, width, height);
        actionListeners = new ArrayList<ActionListener>();
        main = new Color(173, 177, 179);
        hover = new Color(158, 56, 58);
        pressed = new Color(111, 116, 117);
    }

    public void update(){
    }

    public void render(Graphics2D g){
        if(currentState == State.RELEASED){
            g.setColor(main);
            g.fill(clickBox);
        }
        else if(currentState == State.PRESSED){
            g.setColor(pressed);
            g.fill(clickBox);
        }
        else{
            g.setColor(hover);
            g.fill(clickBox);
        }
        g.setColor(Color.white);
        g.setFont(font);
        g.drawString(text, clickBox.x + clickBox.width / 2  - DrawUtils.getMessageWidth(text, font, g) / 2,
                clickBox.y + clickBox.height / 2  + DrawUtils.getMessageHeight(text, font, g) / 2);
    }

    public void addActionListener(ActionListener listener){
        actionListeners.add(listener);
    }

    public void mousePressed(MouseEvent e) {
        if(clickBox.contains(e.getPoint())){
            currentState = State.PRESSED;
        }
    }

    public void mouseReleased(MouseEvent e) {
        if(clickBox.contains(e.getPoint())){
            for(ActionListener al : actionListeners){
                al.actionPerformed(null);
            }
            audio.play(true);
        }
        currentState = State.RELEASED;
    }

    public void mouseDragged(MouseEvent e) {
        if(clickBox.contains(e.getPoint())){
            currentState = State.PRESSED;
        }
        else{
            currentState = State.RELEASED;
        }
    }

    public void mouseMoved(MouseEvent e) {
        if(clickBox.contains(e.getPoint())){
            currentState = State.HOVER;
        }
        else{
            currentState = State.RELEASED;
        }
    }

    public int getX(){
        return clickBox.x;
    }

    public int getY(){
        return clickBox.y;
    }

    public int getWidth(){
        return clickBox.width;
    }

    public int getHeight(){
        return clickBox.height;
    }

    public void setMessage(String text){
        this.text = text;
    }
}
