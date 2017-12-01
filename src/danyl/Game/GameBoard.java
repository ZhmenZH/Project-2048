package danyl.Game;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URISyntaxException;
import java.util.Random;

public class GameBoard {

    public static final int ROWS = 4;
    public static final int COLS = 4;

    private final int begin_cells = 2;
    private Cell[][] board;
    private boolean won;
    private boolean lost;
    private BufferedImage gameboard;
    private BufferedImage finalboard;
    private int x,y;

    private int score = 0;
    private int highscore = 0;
    private Font scoreFont;

    private long pastTime;
    private long thefastestTime;
    private long startTime;
    private String formattedTime = "00:00:000";
    private boolean hasStarted;

    //Saving
    private String saveData;
    private String filename = "SaveData";

    private static int SPACING = 10;//растояние
    public static int BOARD_WIDTH = (COLS + 1) * SPACING + COLS * Cell.WIDTH;
    public static int BOARD_HEIGHT = (ROWS + 1) * SPACING + ROWS * Cell.HEIGHT;

    public GameBoard(int x,int y)
    {
        try {
            saveData = GameBoard.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath();
            // saveData = System.getProperty("user.home") + "foldername";
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        scoreFont = Game.font.deriveFont(20f);
        this.x = x;
        this.y = y;
        board = new Cell[ROWS][COLS];
        gameboard = new BufferedImage(BOARD_WIDTH,BOARD_HEIGHT,BufferedImage.TYPE_INT_RGB);
        finalboard = new BufferedImage(BOARD_WIDTH,BOARD_HEIGHT,BufferedImage.TYPE_INT_RGB);
        startTime = System.nanoTime();

        loadHighScore();
        createBoardImage();
        initiallyNumbers();
    }

    public void createSaveData()
    {
        try {
        File file = new File(saveData,filename);

        FileWriter output = new FileWriter(file);
        BufferedWriter writer = new BufferedWriter(output);
        writer.write("" + 0);
        writer.newLine();
        writer.write("" + Integer.MAX_VALUE);
        writer.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadHighScore()
    {
        try {
            File file = new File(saveData,filename);
            if(!file.isFile())
            {
                createSaveData();
            }

            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
            highscore = Integer.parseInt(reader.readLine());
//          thefastestTime = Long.parseLong(reader.readLine());
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setHighScore()
    {
        FileWriter output = null;

        try{
            File file = new File(saveData,filename);
            output = new FileWriter(file);
            BufferedWriter writer = new BufferedWriter(output);
            writer.write("" + highscore);
            writer.newLine();

            if(pastTime <= thefastestTime && won)
            {
               writer.write("" + pastTime);
            }
            else{
                writer.write("" + thefastestTime);
            }

            writer.close();

        } catch (IOException e) {
        e.printStackTrace();

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

    private void initiallyNumbers()
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
            int location = random.nextInt(ROWS * COLS);
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

    public int getCellX(int col)
    {
        return SPACING + col*Cell.WIDTH + col*SPACING;
    }

    public int getCellY(int row)
    {
        return SPACING + row * Cell.HEIGHT + row*SPACING;
    }

    public void render(Graphics2D graphics)
    {
        Graphics2D graphics2D = (Graphics2D)
        finalboard.getGraphics();
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

        graphics.setColor(Color.BLACK);
        graphics.setFont(scoreFont);
        graphics.drawString("Score: " + score,30,40);
        graphics.setColor(Color.RED);
        graphics.setFont(scoreFont);
        graphics.drawString("Highscore: " + highscore,
                Game.WIDTH - DrawUtils.getMessageWidth("Highscore " + highscore,scoreFont,graphics) - 20,40);

        graphics.drawString("Time: " + formattedTime,30,90);
        graphics.setColor(Color.RED);
        graphics.drawString("Highscore: " + thefastestTime,
                Game.WIDTH - DrawUtils.getMessageWidth
                        ("Highscore" + thefastestTime, scoreFont, graphics)-60,
                90);
    }

    public void update()
    {
        if(!won && !lost)
        {
            if(hasStarted)
            {
                pastTime = (System.nanoTime() - startTime) / 1000000;
                formattedTime = toFormatTime(pastTime);
            }
            else {
                startTime = System.nanoTime();
            }
        }

        checkKeys();

        if(score >= highscore)
        {
            highscore = score;
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
                won = true;
            }
          }
        }
    }

    private String toFormatTime(long millis) {
        String formattedTime = "";

        int hours = (int) (millis / 3600000);
        if (hours >= 1) {
            millis -= hours * 3600000;
            formattedTime += hours + ":";
        }

        int minutes = (int) (millis / 60000);
        if (minutes >= 1) {
            millis -= minutes * 60000;
            if (minutes < 10) {
                formattedTime += "0" + minutes + ":";
            }
            else {
                formattedTime += minutes + ":";
            }
        }

        int seconds = (int) (millis / 1000);
        if (seconds >= 1) {
            millis -= seconds * 1000;
            if (seconds < 10) {
                formattedTime += "0" + seconds + ":";
            }
            else {
                formattedTime += seconds + ":";
            }
        }

        if (millis > 99) {
            formattedTime += millis;
        }
        else if (millis > 9) {
            formattedTime += "0" + millis;
        }
        else {
            formattedTime += "00" + millis;
        }

        return formattedTime;
    }


    private void resetPosition(Cell currentlocation, int row, int col)
    {
        if(currentlocation == null) return;

        int x = getCellX(col);
        int y = getCellY(row);
        int distanceX = currentlocation.getX() - x;
        int distanceY = currentlocation.getY() - y;

        if(Math.abs(distanceX) < Cell.SLIP_SPEED)
        {
            currentlocation.setX(currentlocation.getX() - distanceX);
        }

        if(Math.abs(distanceY) < Cell.SLIP_SPEED)
        {
            currentlocation.setY(currentlocation.getY() - distanceY);
        }

        if(distanceX < 0)
        {
            currentlocation.setX(currentlocation.getX() + Cell.SLIP_SPEED);
        }

        if(distanceY < 0)
        {
            currentlocation.setY(currentlocation.getY() + Cell.SLIP_SPEED);
        }

        if(distanceX > 0)
        {
            currentlocation.setX(currentlocation.getX() - Cell.SLIP_SPEED);
        }

        if(distanceY > 0)
        {
            currentlocation.setY(currentlocation.getY() - Cell.SLIP_SPEED);
        }
    }

    private void moveCells(Direction direction) {
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
            checkLose();
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
                board[newRow - verticalDirection][newCol - horizontalDirection] = null;
                board[newRow][newCol].setSlipTo(new Point(newRow,newCol));
                canMove = true;
            }

            else if(board[newRow][newCol].getValue() == currentlocation.getValue() && board[newRow][newCol].isCanCombine())
            {
                board[newRow][newCol].setCanCombine(false);
                board[newRow][newCol].setValue(board[newRow][newCol].getValue() * 2);
                canMove = true;
                board[newRow - verticalDirection][newCol - horizontalDirection] = null;
                board[newRow][newCol].setSlipTo(new Point(newRow,newCol));
                board[newRow][newCol].setCombineAnimation(true);
                score += board[newRow][newCol].getValue();
            }
            else {
                move = false;
            }
        }
        return canMove;
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

    private void checkLose()
    {
        for(int row = 0; row < ROWS; row++){
            for(int col = 0; col < COLS; col++)
            {
                if(board[row][col] == null)
                    return;
                if(checkSurroundingCells(row,col,board[row][col]))
                {
                    return;
                }
            }
        }

        lost = true;
//        if(score >= highscore)
//        {
//            highscore = score;
//        }
        setHighScore();
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
        return  false;
    }

    private void checkKeys() {
        if (Keyboard.typed(KeyEvent.VK_LEFT)) {
            moveCells(Direction.LEFT);
            if(!hasStarted)
                hasStarted = true;
        }
        if (Keyboard.typed(KeyEvent.VK_RIGHT)) {
            moveCells(Direction.RIGHT);
            if(!hasStarted)
                hasStarted = true;
        }
        if (Keyboard.typed(KeyEvent.VK_UP)) {
            moveCells(Direction.UP);
            if(!hasStarted)
                hasStarted = true;
        }
        if (Keyboard.typed(KeyEvent.VK_DOWN)) {
            moveCells(Direction.DOWN);
            if(!hasStarted)
                hasStarted = true;
        }
    }
}