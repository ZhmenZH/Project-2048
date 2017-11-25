package danyl.Game;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
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

    private boolean hasStarted;


    private static int SPACING = 10;//растояние
    public static int BOARD_WIDTH = (COLS + 1) * SPACING + COLS * Cell.WIDTH;
    public static int BOARD_HEIGHT = (ROWS + 1) * SPACING + ROWS * Cell.HEIGHT;


    public GameBoard(int x,int y)
    {
        this.x = x;
        this.y = y;
        board = new Cell[ROWS][COLS];
        gameboard = new BufferedImage(BOARD_WIDTH,BOARD_HEIGHT,BufferedImage.TYPE_INT_RGB);
        finalboard = new BufferedImage(BOARD_WIDTH,BOARD_HEIGHT,BufferedImage.TYPE_INT_RGB);

        createBoardImage();
        initiallyNumbers();
    }

    public void createBoardImage()
    {
        Graphics2D graphics2D = (Graphics2D) gameboard.getGraphics();
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
        Graphics2D graphics2D = (Graphics2D)finalboard.getGraphics();
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
        checkKeys();

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
            newRow += verticalDirection;
            newCol += horizontalDirection;
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
//                board[newRow][newCol].setCombineAnimation(true);
                // add to score
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
            horizontal_direction = 1;
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

    private void checkLose()
    {
        for(int row = 0; row < ROWS; row++){
            for(int col = 0; col < COLS; col++)
            {
                if(board[row][col] == null) return;
                if(checkSurroundingCells(row,col,board[row][col]))
                {
                    return;
                }
            }
        }

        lost = true;
        //set score
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