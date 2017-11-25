package danyl.Game;

import java.awt.*;
import java.awt.image.BufferedImage;

//Cell = Tile
public class Cell {

    public static final int WIDTH = 90;
    public static final int HEIGHT = 90;
    public static final int ARC_WIDTH = 15;
    public static final int ARC_HEIGHT = 15;
    public static final int SLIP_SPEED = 20;

    private int value;
    private Color background;
    private Color text;
    private Font font;
    private int x,y;
    private BufferedImage cellimage;
//???????????????????????????????????
    private Point slipTo;//скользить

    private boolean canCombine;

    public Cell(int value,int x, int y)
    {
        this.value = value;
        this.x = x;
        this.y = y;
        slipTo = new Point(x,y);
        cellimage = new BufferedImage(WIDTH,HEIGHT,BufferedImage.TYPE_INT_ARGB);
        drawImage();
    }

    public void drawImage()
    {
        Graphics2D graphics = (Graphics2D) cellimage.getGraphics();
        if(value == 2){
            background =  new Color(0xFF0C1B);
            text = new Color(0x000000);
        }
        else if(value == 4){
            background = new Color(0x0000FF);
            text = new Color(0x000000);
        }
        else if(value == 8){
            background = new Color(0x16FFA7);
            text = new Color(0x000000);
        }
        else if(value == 16){
            background = new Color(0xFF1CB3);
            text = new Color(0x000000);
        }
        else if(value == 32){
            background = new Color(0x808080);
            text = new Color(0x000000);
        }
        else if(value == 64){
            background = new Color(0xE9C24C);
            text = new Color(0x000000);
        }
        else if(value == 128){
            background = new Color(0x9517FF);
            text = new Color(0x000000);
        }
        else if(value == 256){
            background = new Color(0xFF9614);
            text = new Color(0x000000);
        }
        else if(value == 512){
            background = new Color(0x1BDAFF);
            text = new Color(0x000000);
        }
        else if(value == 1024){
            background = new Color(0x35E909);
            text = new Color(0x000000);
        }
        else if(value == 2048){
            background = new Color(0x3E0610);
            text = new Color(0x000000);
        }
        else{
            background = Color.black;
            text = Color.white;
        }

        graphics.setColor(new Color(0,0,0,0));
        graphics.fillRect(0,0,WIDTH,HEIGHT);

        graphics.setColor(background);
        graphics.fillRoundRect(0,0,WIDTH,HEIGHT,ARC_WIDTH,ARC_HEIGHT);

        graphics.setColor(text);

        if(value <= 64)
        {
            font = Game.font.deriveFont(36F);
        }
        else {
            font = Game.font;
        }
        graphics.setFont(font);

        int drawX = WIDTH/2 - DrawUtils.getMessageWidth("" + value,font,graphics)/2;
        int drawY = HEIGHT/2 + DrawUtils.getMessageHeight("" + value,font,graphics)/2;
        graphics.drawString(""+ value,drawX,drawY);
        graphics.dispose();
    }

    public void update()
    {

    }

    public void render(Graphics2D graphics2D) {
        graphics2D.drawImage(cellimage, x, y, null);
    }

    public int getValue()
    {
        return value;
    }

    public void setValue(int value)
    {

        this.value = value;
        drawImage();
    }

    public boolean isCanCombine()
    {
        return canCombine;
    }

    public void setCanCombine(boolean canCombine)
    {
        this.canCombine = canCombine;
    }

    public Point isSlipto()
    {
        return slipTo;
    }

    public void setSlipTo(Point slipTo)
    {
        this.slipTo = slipTo;
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

//other
//        else if(value == 4096){
//            background = new Color();
//            text = new Color();
//        }
//
//        else if(value == 8192){
//            background = new Color();
//            text = new Color();
//        }
//
//        else if(value == 16384){
//            background = new Color();
//            text = new Color();
//        }
//
//        else if(value == 32768){
//            background = new Color();
//            text = new Color();
//        }
//
//        else if(value == 65536){
//            background = new Color();
//            text = new Color();
//        }
//        else if(value == 131072){
//            background = new Color();
//            text = new Color();
//        }
