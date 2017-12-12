package danyl.Game;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public class Cell {

    public static final int WIDTH = 100;
    public static final int HEIGHT = 100;
    public static final int ARC_WIDTH = 15;
    public static final int ARC_HEIGHT = 15;
    public static final int SLIP_SPEED = 25;

    private int value;
    private Color background;
    private Color text;
    private Font font;
    private int x,y;
    private BufferedImage cellimage;

    private Point slipTo;//скользить

    private boolean beginAnimation = true;
    private double scaleFirst = 0.1;
    private BufferedImage beginImage;

    private boolean combineAnimation = false;
    private double scaleCombine = 1.2;
    private BufferedImage combineImage;

    private boolean canCombine = true;

    public Cell(int value,int x, int y)
    {
        this.value = value;
        this.x = x;
        this.y = y;
        slipTo = new Point(x,y);
        cellimage = new BufferedImage(WIDTH,HEIGHT,BufferedImage.TYPE_INT_ARGB);
        beginImage = new BufferedImage(WIDTH,HEIGHT,BufferedImage.TYPE_INT_ARGB);
        combineImage = new BufferedImage(WIDTH * 2,HEIGHT* 2,BufferedImage.TYPE_INT_ARGB);
        drawImage();
    }

    public void drawImage()
    {
        Graphics2D graphics = (Graphics2D)
        cellimage.getGraphics();

        if(value == 2){
            background =  new Color(0xC0CBB8);
            text = new Color(0x000000);
        }
        else if(value == 4){
            background = new Color(0xC1B89F);
            text = new Color(0x000000);
        }
        else if(value == 8){
            background = new Color(0xFF8E2F);
            text = new Color(0x000000);
        }
        else if(value == 16){
            background = new Color(0xFF712C);
            text = new Color(0x000000);
        }
        else if(value == 32){
            background = new Color(0xFF3E12);
            text = new Color(0x000000);
        }
        else if(value == 64){
            background = new Color(0xE9100A);
            text = new Color(0x000000);
        }
        else if(value == 128){
            background = new Color(0xFFF969);
            text = new Color(0x000000);
        }
        else if(value == 256){
            background = new Color(0xFFDD31);
            text = new Color(0x000000);
        }
        else if(value == 512){
            background = new Color(0xFFD32F);
            text = new Color(0x000000);
        }
        else if(value == 1024){
            background = new Color(0xFFC403);
            text = new Color(0x000000);
        }
        else if(value == 2048){
            background = new Color(0xF5FF00);
            text = new Color(0x000000);
        }
        else if(value == 4096){
            background = new Color(0x03B7FF);
            text = new Color(0x000000);
        }

        else if(value == 8192){
            background = new Color(0x0290FF);
            text = new Color(0x000000);
        }

        else if(value == 16384){
            background = new Color(0x0033FF);
            text = new Color(0x000000);
        }

        else if(value == 32768){
            background = new Color(0x1C00FF);
            text = new Color(0x000000);
        }

        else if(value == 65536){
            background = new Color(0x7800FF);
            text = new Color(0x000000);
        }
        else if(value == 131072){
            background = new Color(0x7800CC);
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
            font = Game.font.deriveFont(40F);
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
        if(beginAnimation) {
            AffineTransform transform = new AffineTransform();
            transform.translate(WIDTH / 2 - scaleFirst * WIDTH / 2, HEIGHT / 2 - scaleFirst * HEIGHT / 2);
            transform.scale(scaleFirst, scaleFirst);
            Graphics2D graphics2D = (Graphics2D)
                    beginImage.getGraphics();
            graphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
            graphics2D.setColor(new Color(0, 0, 0, 0));
            graphics2D.fillRect(0, 0, WIDTH, HEIGHT);
            graphics2D.drawImage(cellimage, transform, null);
            scaleFirst += 0.1;
            graphics2D.dispose();

            if (scaleFirst >= 1) {
                beginAnimation = false;
            }
        }

        else if(combineAnimation) {
            AffineTransform transform = new AffineTransform();
            transform.translate(WIDTH / 2 - scaleCombine * WIDTH / 2, HEIGHT / 2 - scaleCombine * HEIGHT / 2);
            transform.scale(scaleCombine, scaleCombine);
            Graphics2D graphics2D = (Graphics2D)
            combineImage.getGraphics();

            graphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
            graphics2D.setColor(new Color(0, 0, 0, 0));
            graphics2D.fillRect(0, 0, WIDTH, HEIGHT);
            graphics2D.drawImage(cellimage, transform, null);
            scaleCombine -= 0.08;
            graphics2D.dispose();

            if(scaleCombine <= 1)
            {
                combineAnimation = false;
            }
        }
    }

    public void render(Graphics2D graphics2D) {
        if(beginAnimation)
        {
            graphics2D.drawImage(beginImage,x,y,null);
        }
        else if(combineAnimation)
        {
            graphics2D.drawImage(combineImage,(int)(x + WIDTH/2 - scaleCombine * WIDTH/2),
                                              (int)(y+ HEIGHT/2 - scaleCombine * HEIGHT/2),null);
        }
        else{
            graphics2D.drawImage(cellimage, x, y, null);
        }
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

    public Point getSlipto()
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

    public boolean isCombineAnimation()
    {
        return combineAnimation;
    }

    public void setCombineAnimation(boolean combineAnimation)
    {
        this.combineAnimation = combineAnimation;
        if(combineAnimation)
            scaleCombine = 1.2;
    }
}