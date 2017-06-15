package gui.paint;

import java.awt.*;

public class MenuPainter {
    public void paint(Graphics2D g2d, String text, Rectangle bounds, Boolean isSelected) {
        FontMetrics fm = g2d.getFontMetrics();
        if (isSelected) {
            paintBox(g2d, bounds, Color.BLUE, Color.WHITE);
        } else {
            paintBox(g2d, bounds, Color.DARK_GRAY, Color.LIGHT_GRAY);
        }
        int x = bounds.x + ((bounds.width - fm.stringWidth(text)) / 2);
        int y = bounds.y + ((bounds.height - fm.getHeight()) / 2) + fm.getAscent();

        g2d.setColor(isSelected ? Color.WHITE : Color.LIGHT_GRAY);
        g2d.drawString(text, x, y);
    }

    public void paintBackground(Graphics2D g2d, Rectangle bounds, Color background) {
        g2d.setColor(background);
        g2d.fill(bounds);
    }

    public void paintBox(Graphics2D g2d, Rectangle bounds, Color background, Color foreground) {
        g2d.setColor(background);
        g2d.fill(bounds);
        g2d.setColor(foreground);
        g2d.draw(bounds);
    }

    public void drawAddrInputBox(Graphics2D g2d, Rectangle bounds) {
        g2d.setColor(Color.GRAY);
        g2d.fill(bounds);
        g2d.setColor(Color.BLUE);
        g2d.draw(bounds);
    }

    public void drawAddrInputText(Graphics2D g2d, Rectangle bounds, String text) {
        //String text = "Input address and hit Enter";

        FontMetrics fm = g2d.getFontMetrics();
        int x = bounds.x + ((bounds.width - fm.stringWidth(text)) / 2);
        int y = bounds.y + ((bounds.height - fm.getHeight()) / 16) + fm.getAscent();
        g2d.setColor(Color.WHITE);
        g2d.drawString(text, x, y);
    }
}
