package danyl.Game;

import danyl.GuiScreen.GuiScreen;

import javax.sound.sampled.Clip;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Random;

public class GameBoard {

    public static final int ROWS = 4;
    public static final int COLS = 4;

    private final int begin_cells = 2;
    private Cell[][] board;
    private boolean won;
    private boolean lost;
    private BufferedImage gameboard;
    private int x,y;

    private long pastTime;
    private long startTime;
    private boolean hasStarted;
    private int saveCount = 0;

    private static int SPACING = 10;//растояние
    public static int BOARD_WIDTH = (COLS + 1) * SPACING + COLS * Cell.WIDTH;
    public static int BOARD_HEIGHT = (ROWS + 1) * SPACING + ROWS * Cell.HEIGHT;

    private Sound audio;
    private Sound audioMove;
    private Score scores;
    private Leaders leaders;

    private GuiScreen screen;

    public GameBoard(int x,int y)
    {
        this.x = x;
        this.y = y;
        board = new Cell[ROWS][COLS];
        gameboard = new BufferedImage(BOARD_WIDTH,BOARD_HEIGHT,BufferedImage.TYPE_INT_RGB);
        screen = GuiScreen.getInstance();
        createBoardImage();
        initiallyValues();

        audioMove = new Sound(new File("move.wav"));
        audioMove.setVolume(0.65f);

        leaders = Leaders.getInstance();
        leaders.loadScores();
        scores = new Score(this);
        scores.loadGame();
        scores.setBestTime(leaders.getTheFastestTime());
        scores.setBestScore(leaders.getHighScore());
        if(scores.newGame())
        {
            scores.saveGame();
        }
        else {
            for (int i = 0; i < scores.getBoard().length; i++) {
                if(scores.getBoard()[i] == 0) continue;
                spawn(i/ROWS, i % COLS,scores.getBoard()[i]);
           }
            lost = checkLose();
            won = checkWon();
        }
    }

    public void reset()
    {
        board = new Cell[ROWS][COLS];
        initiallyValues();
        scores.saveGame();
        lost = false;
        won = false;
        hasStarted = false;
        startTime = System.nanoTime();
        pastTime = 0;
        saveCount = 0;
    }

    private void spawn(int row, int col,int value)
    {
        board[row][col] = new Cell(value,getCellX(col),getCellY(row));
    }

    private void initiallyValues()
    {
        for(int i = 0; i < begin_cells; i ++)
        {
            randomValue();
        }
    }

    private void randomValue()
    {
        Random random = new Random();
        boolean notValid = true;

        while(notValid)
        {
            int location = random.nextInt(16);
            int row = location / ROWS;
            int col = location % COLS;
            Cell currentlocation = board[row][col];
            if(currentlocation == null)
            {
                int value = random.nextInt(10) < 9 ? 2 : 4;
                Cell cell = new Cell(value,getCellX(col),getCellY(row));
                board[row][col] = cell;
                notValid = false;
            }
        }
    }

    public void createBoardImage()
    {
        Graphics2D graphics2D = (Graphics2D)
        gameboard.getGraphics();
        graphics2D.setColor(Color.darkGray);
        graphics2D.fillRect(0,0,BOARD_WIDTH,BOARD_HEIGHT);
        graphics2D.setColor(Color.gray);

        for(int row = 0; row<ROWS; row++)
        {
            for(int col = 0; col<COLS; col++)
            {
                int x = SPACING + SPACING * col + Cell.WIDTH * col;
                int y = SPACING + SPACING * row + Cell.HEIGHT * row;
                graphics2D.fillRoundRect(x, y, Cell.WIDTH, Cell.HEIGHT, Cell.ARC_WIDTH, Cell.ARC_HEIGHT);
            }
        }
    }

    public void render(Graphics2D graphics)
    {
        BufferedImage finalboard = new BufferedImage(BOARD_WIDTH, BOARD_HEIGHT, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics2D = (Graphics2D)
                finalboard.getGraphics();
        graphics2D.setColor(new Color(0, 0, 0, 0));
        graphics2D.fillRect(0, 0, BOARD_WIDTH, BOARD_HEIGHT);
        graphics2D.drawImage(gameboard,0,0,null);

        for(int row = 0; row<ROWS; row++)
        {
            for(int col = 0; col<COLS; col++)
            {
                Cell currentlocation = board[row][col];
                if(currentlocation == null) continue;
                currentlocation.render(graphics2D);
            }
        }

        graphics.drawImage(finalboard,x,y,null);
        graphics2D.dispose();
    }

    public void update()
    {
        saveCount++;
        if(saveCount >= 120)
        {
            saveCount = 0;
            scores.saveGame();
        }
        if(!won && !lost)
        {
            if(hasStarted)
            {
                pastTime = (System.nanoTime() - startTime) / 1000000;
                scores.setTime(pastTime);
            }
            else {
                startTime = System.nanoTime();
            }
        }

        checkKeys();

        if(scores.getActualScore() > scores.getBestScore())
        {
            scores.setBestScore(scores.getActualScore());
        }

        if(scores.getTime() > scores.getBestTime())
        {
            scores.setBestTime(scores.getTime());
        }

        for(int row = 0; row<ROWS; row++)
        {
            for(int col = 0; col<COLS; col++)
            {
                Cell currentlocation = board[row][col];
                if(currentlocation == null) continue;
                currentlocation.update();

                resetPosition(currentlocation,row,col);

                if(currentlocation.getValue() == 2048)
                {
                    setWon(true);
                }
            }
        }
    }

    private void resetPosition(Cell cell, int row, int col)
    {
        if(cell == null) return;

        int x = getCellX(col);
        int y = getCellY(row);
        int distanceX = cell.getX() - x;
        int distanceY = cell.getY() - y;

        if(Math.abs(distanceX) < Cell.SLIP_SPEED)
        {
            cell.setX(cell.getX() - distanceX);
        }

        if(Math.abs(distanceY) < Cell.SLIP_SPEED)
        {
            cell.setY(cell.getY() - distanceY);
        }

        if(distanceX < 0)
        {
            cell.setX(cell.getX() + Cell.SLIP_SPEED);
        }

        if(distanceY < 0)
        {
            cell.setY(cell.getY() + Cell.SLIP_SPEED);
        }

        if(distanceX > 0)
        {
            cell.setX(cell.getX() - Cell.SLIP_SPEED);
        }

        if(distanceY > 0)
        {
            cell.setY(cell.getY() - Cell.SLIP_SPEED);
        }
    }

    public int getCellX(int col)
    {
        return SPACING + col * Cell.WIDTH + col * SPACING;
    }

    public int getCellY(int row)
    {
        return SPACING + row * Cell.HEIGHT + row * SPACING;
    }

    private boolean checkOutOfFrame(Direction direction,int row, int col)
    {
        if(direction == Direction.LEFT)
        {
            return col < 0;
        }

        else if(direction == Direction.RIGHT)
        {
            return col > COLS - 1;
        }

        else if(direction == Direction.UP)
        {
            return row < 0;
        }

        else if(direction == Direction.DOWN)
        {
            return row > ROWS - 1;
        }
        return false;
    }

    private void moveCells(Direction direction) {
        audioMove.play(true);
        boolean canMove = false;
        int horizontal_direction = 0;
        int vertical_direction = 0;

        if (direction == Direction.LEFT)
        {
            horizontal_direction = -1;

            for (int row = 0; row < ROWS; row++) {
                for (int col = 0; col < COLS; col++) {
                    if (!canMove) {
                        canMove = move(row, col, horizontal_direction, vertical_direction, direction);
                    } else move(row, col, horizontal_direction, vertical_direction, direction);
                }
            }
        }

        else if (direction == Direction.RIGHT)
        {
            horizontal_direction = 1;
            for(int row = 0; row<ROWS; row++)
            {
                for (int col = COLS - 1; col >= 0; col--) {
                    if (!canMove)
                    {
                        canMove = move(row, col, horizontal_direction, vertical_direction, direction);
                    }
                    else move(row, col, horizontal_direction, vertical_direction, direction);
                }
            }
        }
        // 2 2 4 8 ------> 0 4 4 8
        // 2 2 4 8 -------> 0 0 0 16

        else if(direction == Direction.UP)
        {
            vertical_direction = -1;
            for(int row = 0; row<ROWS; row++) {
                for (int col = 0; col < COLS; col++) {
                    if (!canMove)
                    {
                        canMove = move(row, col, horizontal_direction, vertical_direction, direction);
                    }
                    else move(row, col, horizontal_direction, vertical_direction, direction);
                }
            }
        }

        else if(direction == Direction.DOWN) {
            vertical_direction = 1;
            for (int row = ROWS - 1; row >= 0; row--) {
                for (int col = 0; col < COLS; col++) {
                    if (!canMove)
                    {
                        canMove = move(row, col, horizontal_direction, vertical_direction, direction);
                    }
                    else move(row, col, horizontal_direction, vertical_direction, direction);
                }
            }
        }

        else
        {
            System.out.println(direction + "Incorrect direction.");
        }

        for(int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                Cell currentlocation = board[row][col];
                if(currentlocation == null) continue;
                currentlocation.setCanCombine(true);//установить движение
            }
        }

        if(canMove)
        {
            randomValue();
            setLost(checkLose());
        }
    }

    private boolean move(int row, int col,int horizontalDirection, int verticalDirection,Direction direction)
    {
        boolean canMove = false;
        Cell currentlocation = board[row][col];
        if(currentlocation == null) {
            return false;
        }

        boolean move = true;
        int newCol = col;
        int newRow = row;
        while (move) {
            newCol += horizontalDirection;
            newRow += verticalDirection;
            if (checkOutOfFrame(direction, newRow, newCol))
                break;

            if(board[newRow][newCol] == null)
            {
                board[newRow][newCol] = currentlocation;
                canMove = true;
                board[newRow - verticalDirection][newCol - horizontalDirection] = null;
                board[newRow][newCol].setSlipTo(new Point(newRow,newCol));
            }
            else if(board[newRow][newCol].getValue() == currentlocation.getValue() && board[newRow][newCol].isCanCombine())
            {
                board[newRow][newCol].setCanCombine(false);
                board[newRow][newCol].setValue(board[newRow][newCol].getValue() * 2);
                canMove = true;
                board[newRow - verticalDirection][newCol - horizontalDirection] = null;
                board[newRow][newCol].setSlipTo(new Point(newRow,newCol));
                board[newRow][newCol].setCombineAnimation(true);
                scores.setActualScore(scores.getActualScore() + board[newRow][newCol].getValue());
            }
            else {
                move = false;
            }
        }
        return canMove;
    }

    private boolean checkLose()
    {
        for(int row = 0; row < ROWS; row++){
            for(int col = 0; col < COLS; col++)
            {
                if(board[row][col] == null) {
                    return false;
                }
                boolean canCombine = checkSurroundingCells(row,col,board[row][col]);
                if(canCombine)
                {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean checkWon() {
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++)
            {
                if(board[row][col] == null)
                    continue;
                if(board[row][col].getValue() >= 2048)
                    return true;
            }
        }
        return false;
    }

    private boolean checkSurroundingCells(int row, int col, Cell currentlocation)
    {
        if(row > 0)
        {
            Cell check = board[row - 1][col];
            if(check == null) return true;
            if(currentlocation.getValue() == check.getValue()) return true;
        }

        if(row < ROWS - 1)
        {
            Cell check = board[row + 1][col];
            if(check == null) return true;
            if(currentlocation.getValue() == check.getValue()) return true;
        }

        if(col > 0)
        {
            Cell check = board[row][col - 1];
            if(check == null) return true;
            if(currentlocation.getValue() == check.getValue()) return true;
        }

        if(col < COLS - 1)
        {
            Cell check = board[row][col + 1];
            if(check == null) return true;
            if(currentlocation.getValue() == check.getValue()) return true;
        }
        return false;
    }

    private void checkKeys() {
        if (!Keyboard.pressed[KeyEvent.VK_LEFT] && Keyboard.prev[KeyEvent.VK_LEFT]) {
            moveCells(Direction.LEFT);
            if (!hasStarted) hasStarted = !lost;
            //else if
        }
        if (!Keyboard.pressed[KeyEvent.VK_RIGHT] && Keyboard.prev[KeyEvent.VK_RIGHT]) {
            moveCells(Direction.RIGHT);
            if (!hasStarted) hasStarted = !lost;
        }
        if (!Keyboard.pressed[KeyEvent.VK_UP] && Keyboard.prev[KeyEvent.VK_UP]) {
            moveCells(Direction.UP);
            if (!hasStarted) hasStarted = !lost;
        }
        if (!Keyboard.pressed[KeyEvent.VK_DOWN] && Keyboard.prev[KeyEvent.VK_DOWN]) {
            moveCells(Direction.DOWN);
            if (!hasStarted) hasStarted = !lost;
        }
    }

    public boolean isLost()
    {
        return lost;
    }

    public void setLost(boolean lost)
    {
        if(!this.lost && lost)
        {
            leaders.addScore(scores.getActualScore());
            //leaders.addTime(scores.getTime());
            leaders.saveScores();
            audioMove.stop();
        }
        this.lost = lost;
    }

    public Cell[][] getBoard()
    {
        return board;
    }

    public boolean isWon()
    {
        return won;
    }

    public void  setWon(boolean won)
    {
        if(!this.won && won && !lost)
        {
            leaders.addTime(scores.getTime());

            leaders.saveScores();
        }
        this.won = won;
    }

    public Score getScores()
    {
        return scores;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }
}