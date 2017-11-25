package danyl.Game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;

public class Game extends JPanel implements KeyListener, Runnable {

    public static final int WIDTH = 400;
    public static final int HEIGHT = 630;
    public static final Font font = new Font("", Font.ITALIC, 25);
    BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
    //font

    private Thread game;
    private boolean running;

    private long starttime;
    private long elapsedtime;
    private boolean set;

    private GameBoard board;

    public Game() {
        setFocusable(true);
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        addKeyListener(this);

        board = new GameBoard(WIDTH / 2 - GameBoard.BOARD_WIDTH / 2, HEIGHT - GameBoard.BOARD_HEIGHT - 10);
    }

    @Override
    public void run() {
        int fps = 0;
        int updates = 0;
        long fpsTimer = System.currentTimeMillis();
        double nsPerUpdate = 1000000000.0 / 60;

        //last update time in nanoces

        double then = System.nanoTime();
        double unprocessed = 0;

        while (running) {
            boolean shouldRender = false;

            double now = System.nanoTime();
            unprocessed += (now - then) / nsPerUpdate;
            then = now;
            //update queueu
            while (unprocessed >= 1) {
                updates++;
                update();
                unprocessed--;
                shouldRender = true;
            }

            //render
            if (shouldRender) {
                fps++;
                render();
                shouldRender = false;
            }
            else {
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        //fps timer
        if (System.currentTimeMillis() - fpsTimer > 1000) {
            System.out.printf("%d fps %d updates", fps, updates);
            System.out.println();
            fps = 0;
            updates = 0;
            fpsTimer += 1000;

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

    private void render() {
        Graphics2D graphics = (Graphics2D)image.getGraphics();
        graphics.setColor(Color.white);
        graphics.fillRect(0, 0, WIDTH, HEIGHT);
        board.render(graphics);

        graphics.dispose();

        Graphics2D graphics2D = (Graphics2D) getGraphics();
        graphics2D.drawImage(image, 0, 0, null);
        graphics2D.dispose();
    }

    public void update() {
        board.update();
        Keyboard.update();
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
}