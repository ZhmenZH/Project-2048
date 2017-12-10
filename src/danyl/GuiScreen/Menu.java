package danyl.GuiScreen;

import danyl.Game.DrawUtils;
import danyl.Game.Game;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Menu extends GuiPanel {

    private Font titleFont = Game.font.deriveFont(110f);
    private Font creatorFont = Game.font.deriveFont(20f);
   // private String menu = "Menu";
    private String creator = "By Danyl Zhmyrov";

    private String title = "2048";
    private int buttonWidth = 220;
    private int buttonHeight = 60;
    private int spacing = 100;

    public Menu()
    {
        super();

        GuiButton playButton = new GuiButton(Game.WIDTH/2 - buttonWidth/2,200,buttonWidth,buttonHeight);
        playButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                GuiScreen.getInstance().setActualPanel("Play");
            }
        });
        playButton.setMessage("Play");
        addButton(playButton);

        GuiButton scoreButton = new GuiButton(Game.WIDTH/2 - buttonWidth/2,playButton.getY() + spacing,buttonWidth,buttonHeight);
        scoreButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                GuiScreen.getInstance().setActualPanel("Scores");
            }
        });
        scoreButton.setMessage("Scores");
        addButton(scoreButton);

        GuiButton rulesButton = new GuiButton(Game.WIDTH/2 - buttonWidth/2, scoreButton.getY() + spacing,buttonWidth,buttonHeight);
        rulesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                GuiScreen.getInstance().setActualPanel("Rules");
            }
        });
        rulesButton.setMessage("Rules");
        addButton(rulesButton);

        GuiButton quitButton = new GuiButton(Game.WIDTH/2 - buttonWidth/2,rulesButton.getY() + spacing,buttonWidth,buttonHeight);
        quitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        quitButton.setMessage("Quit");
        addButton(quitButton);
    }

    @Override
    public void render(Graphics2D graphics2D) {
        super.render(graphics2D);
        graphics2D.setFont(titleFont);
        graphics2D.setColor(Color.BLACK);
        graphics2D.drawString(title,Game.WIDTH/2 - DrawUtils.getMessageWidth(title,titleFont,graphics2D)/2,140);
        graphics2D.setFont(creatorFont);
        graphics2D.drawString(creator,330,Game.HEIGHT - 10);
    }
}