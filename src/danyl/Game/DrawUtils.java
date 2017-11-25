package danyl.Game;

import java.awt.*;
import java.awt.font.TextLayout;
import java.awt.geom.Rectangle2D;

public class DrawUtils {

    private DrawUtils(){}

    public static int getMessageWidth(String message, Font font, Graphics2D graphics2D)
    {
        graphics2D.setFont(font);
        Rectangle2D bounds = graphics2D.getFontMetrics().getStringBounds(message,graphics2D);
        return (int) bounds.getWidth();
    }

    public static int getMessageHeight(String message, Font font, Graphics2D graphics2D)
    {
        graphics2D.setFont(font);
        if(message.length() == 0)
            return 0;
        TextLayout textLayout = new TextLayout(message,font, graphics2D.getFontRenderContext());
        return(int)textLayout.getBounds().getHeight();
    }
}