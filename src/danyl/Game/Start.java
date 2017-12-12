package danyl.Game;

import javax.swing.*;

public class Start {

    public static void main(String[] args) {

        Game game = new Game();

        JFrame window = new JFrame("2048");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setVisible(true);
        window.add(game);
        window.pack();
        window.setLocationRelativeTo(null);
        window.setResizable(true);
        game.start();
    }
}