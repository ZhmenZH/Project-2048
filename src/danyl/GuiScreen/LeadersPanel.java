package danyl.GuiScreen;

import danyl.Game.DrawUtils;
import danyl.Game.Game;
import danyl.Game.Leaders;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class LeadersPanel extends GuiPanel{

    private Leaders lBoard;
    private int buttonWidth = 110;
    private int backButtonWidth = 220;
    private int buttonSpacing = 30;
    private int buttonY = 120;
    private int buttonHeight = 50;
    private int leaderboardsX = 200;
    private int leaderboardsY = buttonY + buttonHeight + 90;

    private String title = "Leaderboards";
    private Font titleFont = Game.font.deriveFont(48f);
    private Font scoreFont = Game.font.deriveFont(30f);
    private State currentState = State.SCORE;

    public LeadersPanel(){
        super();
        lBoard = Leaders.getInstance();
        lBoard.loadScores();

        GuiButton scoreButton = new GuiButton(Game.WIDTH / 2 - buttonWidth / 2 - 80, buttonY, buttonWidth, buttonHeight);
        scoreButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                currentState = State.SCORE;
            }
        });
        scoreButton.setMessage("Scores");
        addButton(scoreButton);

        GuiButton timeButton = new GuiButton(Game.WIDTH / 2 + buttonWidth / 2 + buttonSpacing - 80, buttonY, buttonWidth, buttonHeight);
        timeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                currentState = State.TIME;
            }
        });
        timeButton.setMessage("Times");
        addButton(timeButton);

        GuiButton backButton = new GuiButton(Game.WIDTH / 2 - backButtonWidth / 2, 500, backButtonWidth, 60);
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                GuiScreen.getInstance().setActualPanel("Menu");
            }
        });
        backButton.setMessage("Back");
        addButton(backButton);
    }

    private void drawLeaderboards(Graphics2D g){
        ArrayList<String> strings = new ArrayList<String>();
        if(currentState == State.SCORE){
            strings = convertToStrings(lBoard.getBestScores());
        }
        else {
            for(Long l : lBoard.getBestTimes()){
                strings.add(DrawUtils.toFormatTime(l));
            }
        }
        g.setColor(Color.black);
        g.setFont(scoreFont);

        for(int i = 0; i < strings.size(); i++){
            String s = (i + 1) + ". " + strings.get(i);
            g.drawString(s, leaderboardsX, leaderboardsY + i * 40);
        }
    }

    private ArrayList<String> convertToStrings(ArrayList<? extends Number> list){
        ArrayList<String> ret = new ArrayList<String>();
        for(Number n : list){
            ret.add(n.toString());
        }
        return ret;
    }

    @Override
    public void update(){
    }

    @Override
    public void render(Graphics2D g){
        super.render(g);
        g.setColor(Color.black);
        g.drawString(title, Game.WIDTH / 2 - DrawUtils.getMessageWidth(title, titleFont, g) / 2, DrawUtils.getMessageHeight(title, titleFont, g) + 40);
        drawLeaderboards(g);
    }

    private enum State{
        SCORE,
        TIME
    }
}