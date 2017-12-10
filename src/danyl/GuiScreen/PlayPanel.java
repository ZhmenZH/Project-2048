package danyl.GuiScreen;

import danyl.Game.*;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;

public class PlayPanel extends GuiPanel {

    private GameBoard board;
    private BufferedImage info;
    private Score scores;
    private Font scoreFont;
    private String toFormatTime;
    private String bestTimeFormat;

    private GuiButton tryagain;
    private GuiButton menu;
    private GuiButton keepGoing;
    private GuiButton gameboardMenu;
    private GuiButton newGame;
    private GuiButton music;
    private GuiButton musicOn;
    private GuiButton musicOff;

    private int spacing = 20;
    private int largeButtonWidth = 340;
    private int buttonHeight = 50;
    private boolean added;
    private int alpha;

    private Font gameOverFont;
    private Font winnerFont;

    private Sound audioLost;
    private Sound audio;

    private GameBoard df;

//ERROR is HERE!!!
    public PlayPanel()
    {
        audio = new Sound(new File("musicMenu1.wav"));
        audio.play(true);
        audio.setVolume(0.65f);

        audioLost = new Sound(new File("lost.wav"));
        scoreFont = Game.font.deriveFont(20f);

        gameOverFont = Game.font.deriveFont(90f);
        winnerFont = Game.font.deriveFont(70f);

        board = new GameBoard(Game.WIDTH/2 - GameBoard.BOARD_WIDTH/2,Game.HEIGHT - GameBoard.BOARD_HEIGHT - 20);
        scores = board.getScores();
        info = new BufferedImage(Game.WIDTH,200,BufferedImage.TYPE_INT_RGB);

        gameboardMenu = new GuiButton(10,10,largeButtonWidth - 240,buttonHeight - 10);
        newGame = new GuiButton(360,10,largeButtonWidth - 210,buttonHeight - 10);
        music = new GuiButton(170,10,largeButtonWidth - 230,buttonHeight - 10);
        musicOn = new GuiButton(225,52,largeButtonWidth - 300,buttonHeight - 10);
        musicOff = new GuiButton(180,52,largeButtonWidth - 300,buttonHeight - 10);

        menu = new GuiButton(Game.WIDTH/2 - largeButtonWidth/2,450,largeButtonWidth,buttonHeight);
        tryagain = new GuiButton(menu.getX(),menu.getY() - spacing - buttonHeight,largeButtonWidth,buttonHeight);
        keepGoing = new GuiButton(tryagain.getX(),tryagain.getY() - spacing - buttonHeight,largeButtonWidth,buttonHeight);

        music.setMessage("Music");
        musicOn.setMessage("On");
        musicOff.setMessage("Off");
        newGame.setMessage("New Game");
        gameboardMenu.setMessage("Menu");
        tryagain.setMessage("Try Again");
        keepGoing.setMessage("Keep Going");
        menu.setMessage("Back to Main Menu");

        musicOn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
               audio.play(true);
            }
        });

        musicOff.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                    audio.stop();
            }
        });

        newGame.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                board.getScores().reset();
                board.reset();
            }
        });

        gameboardMenu.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                GuiScreen.getInstance().setActualPanel("Menu");
            }
        });

        tryagain.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("1");
                board.getScores().reset();
                System.out.println("2");
                board.reset();
                System.out.println("3");
                alpha = 0;
                addButton(gameboardMenu);
                addButton(newGame);
                addButton(music);
                addButton(musicOn);
                addButton(musicOff);

                System.out.println("4");
                removeButton(tryagain);
                removeButton(menu);
                removeButton(keepGoing);
                System.out.println("5");
                added = false;
            }
        });

        keepGoing.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                GuiScreen.getInstance().setActualPanel("Play");
            }
        });

        menu.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                GuiScreen.getInstance().setActualPanel("Menu");
            }
        });

        addButton(gameboardMenu);
        addButton(newGame);
        addButton(music);
        addButton(musicOff);
        addButton(musicOn);
    }

    private void drawGUI(Graphics2D graphics)
    {
        //format the times
        toFormatTime = DrawUtils.toFormatTime(scores.getTime());
        //bestTimeFormat = DrawUtils.toFormatTime(scores.getBestTime());

        //Draw it
        Graphics2D graphics2D = (Graphics2D)info.getGraphics();
        graphics2D.setColor(Color.white);
        graphics2D.fillRect(0,0,info.getWidth(),info.getHeight());
        graphics2D.setColor(Color.darkGray);
        graphics2D.setFont(scoreFont);
        graphics2D.drawString("Score: " + scores.getActualScore(),20,130);
        graphics2D.setColor(Color.RED);
        graphics2D.drawString("HighScore: " + scores.getBestScore(),
                Game.WIDTH - 195,130);
        graphics2D.setColor(Color.darkGray);
        graphics2D.drawString("Time: " + toFormatTime,20,80);
        graphics2D.setColor(Color.RED);
//        graphics2D.drawString("BestTime: " + bestTimeFormat,
//                Game.WIDTH - DrawUtils.getMessageWidth("BestTime: " + bestTimeFormat,scoreFont,graphics2D)-20,90);

        graphics2D.dispose();
        graphics.drawImage(info,0,0,null);
    }

    public void drawGameOver(Graphics2D graphics)
    {
        graphics.setColor(new Color(5,5,5,alpha));
        graphics.fillRect(0,0,Game.WIDTH,Game.HEIGHT);
        graphics.setColor(Color.RED);
        graphics.setFont(gameOverFont);
        graphics.drawString("Game Over!",Game.WIDTH/2 - DrawUtils.getMessageWidth("Game Over!", gameOverFont,graphics)/2,150);
    }

    public void showVictoryMessage(Graphics2D graphics)
    {
        graphics.setColor(new Color(222,222,222,alpha));
        graphics.fillRect(0,0,Game.WIDTH,Game.HEIGHT);
        graphics.setColor(Color.BLUE);
        graphics.setFont(winnerFont);
        graphics.drawString("Congratulations!",Game.WIDTH/2 - DrawUtils.getMessageWidth("Congratulations!", winnerFont,graphics)/2,150);
        graphics.drawString("You won!",Game.WIDTH/2 - DrawUtils.getMessageWidth("You won!", winnerFont,graphics)/2,250);
    }

    @Override
    public void update()
    {
        board.update();

        if(board.isLost())
        {
            alpha++;
            if(alpha > 170)
            {
                alpha = 170;
            }
        }

        else if(board.isWon())
        {
            alpha++;
            if(alpha > 170)
            {
                alpha = 170;
            }
        }
    }

    @Override
    public void render(Graphics2D graphics2D)
    {
        drawGUI(graphics2D);
        board.render(graphics2D);

        if(board.isLost())
        {
            removeButton(music);
            removeButton(musicOn);
            removeButton(musicOff);
            removeButton(gameboardMenu);
            removeButton(newGame);
            if(!added)
            {
                added = true;
                addButton(menu);
                addButton(tryagain);
                audioLost.play(true);
            }
            drawGameOver(graphics2D);
        }

        if(board.isWon())
        {
            if(!added) {
                added = true;
                addButton(menu);
                addButton(tryagain);
                addButton(keepGoing);
            }
            showVictoryMessage(graphics2D);
        }
        super.render(graphics2D);
    }
}