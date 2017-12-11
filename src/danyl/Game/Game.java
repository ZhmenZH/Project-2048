package danyl.Game;

import danyl.GuiScreen.*;

import javax.swing.*;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;

public class Game extends JPanel implements Runnable, KeyListener,MouseMotionListener,MouseListener{

    public static final int WIDTH = 500;
    public static final int HEIGHT = 630;
    public static final Font font = new Font("", Font.ITALIC, 30);
    BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
    private Thread game;
    private boolean running;

    private GuiScreen screen;

    public Game() {
        setFocusable(true);
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        addKeyListener(this);
        addMouseListener(this);
        addMouseMotionListener(this);

        screen = GuiScreen.getInstance();
        screen.add("Menu",new Menu());
        screen.add("Play",new PlayPanel());
        screen.add("Scores", new LeadersPanel());
        screen.add("Rules",new RulesPanel());
        screen.setActualPanel("Menu");
    }

    private void render() {
        Graphics2D graphics = (Graphics2D) image.getGraphics();
        graphics.setColor(Color.yellow);
        graphics.fillRect(0, 0, WIDTH, HEIGHT);
        screen.render(graphics);
        graphics.dispose();

        Graphics2D graphics2D = (Graphics2D) getGraphics();
        graphics2D.drawImage(image, 0, 0, null);
        graphics2D.dispose();
    }

    private void update() {
        screen.update();
        Keyboard.update();
    }

    @Override
    public void run() {
        int fps = 0, updates = 0;
        long fpsTimer = System.currentTimeMillis();
        double nsPerUpdate = 1000000000.0 / 60;

        // last update time in nanoseconds
        double then = System.nanoTime();
        double unprocessed = 0;

        while (running) {

            boolean shouldRender = false;

            double now = System.nanoTime();
            unprocessed += (now - then) / nsPerUpdate;
            then = now;

            // Update queue
            while (unprocessed >= 1) {

                // update
                updates++;
                update();
                unprocessed--;
                shouldRender = true;
            }

            // Render
            if (shouldRender) {
                fps++;
                render();
                shouldRender = false;
            }
            else {
                try {
                    Thread.sleep(1);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            // FPS timer
            if (System.currentTimeMillis() - fpsTimer > 1000) {
                System.out.printf("%d fps %d updates", fps, updates);
                System.out.println("");
                fps = 0;
                updates = 0;
                fpsTimer += 1000;
            }
        }
    }

    public synchronized void start()
    {
        if(running) return;
        running = true;
        game = new Thread(this,"game");
        game.start();
    }

    public synchronized void stop()
    {
        if(!running) return;
        running = false;
        System.exit(0);

    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        Keyboard.KeyPressed(e);
    }

    @Override
    public void keyReleased(KeyEvent e) {
        Keyboard.KeyReleased(e);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
        screen.mousePressed(e);
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        screen.mouseReleased(e);
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        screen.mouseDragged(e);
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        screen.mouseMoved(e);
    }
}