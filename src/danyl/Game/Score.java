package danyl.Game;

import java.io.*;

public class Score {

    //file
    private String filePath;
    private String tempFile;
    private GameBoard gameBoard;

    //Scores
    private int actualScore;
    private int bestScore;
    private long time;
    private long bestTime;
    private long startingTime;
    private int[] board = new int[16];

    private boolean newGame;

    public Score(GameBoard gameBoard)
    {
        try {
            filePath = new File("").getAbsolutePath();
        } catch (Exception e) {
            e.printStackTrace();
        }

        tempFile = "RESULTS.tmp";
        this.gameBoard = gameBoard;
    }

    public void reset()
    {
        File file = new File(filePath,tempFile);
        if(file.isFile())
        {
            file.delete();
        }

        newGame = true;
        startingTime = 0;
        actualScore = 0;
        time = 0;
    }

    public void createFile()
    {
        FileWriter output = null;
        newGame = true;

        try {
            File file = new File(filePath,tempFile);
            output = new FileWriter(file);
            BufferedWriter writer = new BufferedWriter(output);
            writer.write("" + 0);
            writer.newLine();
            writer.write("" + 0);
            writer.newLine();
            writer.write("" + 0);
            writer.newLine();
            writer.write("" + 0);
            writer.newLine();

            for (int row = 0; row< GameBoard.ROWS; row++)
            {
                for(int col = 0; col<GameBoard.COLS; col++)
                {
                    if(row == GameBoard.ROWS - 1 && col == GameBoard.COLS - 1)
                    {
                        writer.write("" + 0);
                    }
                    else{
                        writer.write(0 + ",");
                    }
                }
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveGame()
    {
        FileWriter output = null;
        if(newGame)
        {
            newGame = false;
        }
        try {
            File file = new File(filePath,tempFile);
            output = new FileWriter(file);
            BufferedWriter writer = new BufferedWriter(output);
            writer.write("" + actualScore);
            writer.newLine();
            writer.write("" + bestScore);
            writer.newLine();
            writer.write("" + time);
            writer.newLine();
            writer.write("" + bestTime);
            writer.newLine();

            //для рекорда по значению ячейки
            for (int row = 0; row < GameBoard.ROWS; row++) {
                for (int col = 0; col < GameBoard.COLS; col++) {
                    this.board[row * GameBoard.COLS + col] = gameBoard.getBoard()[row][col] != null ? gameBoard.getBoard()[row][col].getValue() : 0;
                    if (row == GameBoard.ROWS - 1 && col == GameBoard.COLS - 1)
                        writer.write("" + board[row * GameBoard.COLS + col]);
                    else writer.write(board[row * GameBoard.COLS + col] + ",");
                }
            }

            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadGame()
    {
        try {
        File file = new File(filePath,tempFile);
        if(!file.isFile())
        {
            createFile();
        }

        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
        actualScore = Integer.parseInt(reader.readLine());
        bestScore = Integer.parseInt(reader.readLine());
        time = Long.parseLong(reader.readLine());
        startingTime = time;
        bestTime = Long.parseLong(reader.readLine());
// для рекорда по значению ячеек
        String[] board = reader.readLine().split(",");
        for(int i = 0; i < board.length; i++)
        {
            this.board[i] = Integer.parseInt(board[i]);
        }

        reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int getActualScore() {
        return actualScore;
    }

    public void setActualScore(int actualScore) {
        this.actualScore = actualScore;
    }

    public int getBestScore() {
        return bestScore;
    }

    public void setBestScore(int bestScore) {
        this.bestScore = bestScore;
    }

    public long getBestTime() {
        return bestTime;
    }

    public void setBestTime(long bestTime) {
        this.bestTime = bestTime;
    }

    public int[] getBoard() {
        return board;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time + startingTime;
    }

    public boolean newGame() {
        return newGame;
    }
}