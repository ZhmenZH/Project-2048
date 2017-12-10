package danyl.GuiScreen;

import danyl.Game.DrawUtils;
import danyl.Game.Game;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RulesPanel extends GuiPanel {

    private int backButtonWidth = 220;

    private String rules1 = "In each round, a tile with a face value of «2» or «4» appears.";
    private String rules2 = "Use the keyboard arrows to move the tiles in different directions.";
    private String rules3 = "If, on dropping, two tiles of the same denomination «strike»";
    private String rules4 = "one on top of another, then they are combine together into one,";
    private String rules5 = "the value of which is equal to the sum of the combined tiles.";
    private String rules6 = "After each move, a new tile with a nominal value of «2» or «4»";
    private String rules7 = "appears on the free section of the field.";
    private String rules8 = "If the position of the tiles or their value doesn't change";
    private String rules9 = "when the arrows are pressed, the move is not made.";
    private String rules10 = "For each combine, the score increase by the denomination";
    private String rules11 = "of the resulting tile. If after the next move you can't perform";
    private String rules12 = "the action, you LOSE!";
    private String rules13 = "When you score a cell with value of «2048», you WIN the game!";

    private String title = "Rules";
    private Font titleFont = Game.font.deriveFont(48f);
    private Font rulesFont = Game.font.deriveFont(17f);

    public RulesPanel()
    {
        super();
        //rulesPanel = Rules.getInstance();

        GuiButton backButton = new GuiButton(Game.WIDTH / 2 - backButtonWidth / 2, 530, backButtonWidth, 60);
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                GuiScreen.getInstance().setActualPanel("Menu");
            }
        });
        backButton.setMessage("Back");

        addButton(backButton);
    }

    private void showRules(Graphics2D graphics2D)
    {
        graphics2D.setColor(Color.darkGray);
        graphics2D.setFont(rulesFont);
        graphics2D.drawString(rules1,10,120);
        graphics2D.drawString(rules2,10,150);
        graphics2D.drawString(rules3,10,180);
        graphics2D.drawString(rules4,10,210);
        graphics2D.drawString(rules5,10,240);
        graphics2D.drawString(rules6,10,270);
        graphics2D.drawString(rules7,10,300);
        graphics2D.drawString(rules8,10,330);
        graphics2D.drawString(rules9,10,360);
        graphics2D.drawString(rules10,10,390);
        graphics2D.drawString(rules11,10,420);
        graphics2D.drawString(rules12,10,450);
        graphics2D.drawString(rules13,10,480);
    }

    @Override
    public void update(){
    }

    @Override
    public void render(Graphics2D g){
        super.render(g);
        g.setColor(Color.black);
        g.drawString(title, Game.WIDTH / 2 - DrawUtils.getMessageWidth(title, titleFont, g) / 2, DrawUtils.getMessageHeight(title, titleFont, g) + 40);
        showRules(g);
    }
}