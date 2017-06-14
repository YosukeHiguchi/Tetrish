package gui.panel;

import java.io.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.imageio.ImageIO;

import static constant.Const.*;
import gui.MainFrame;
import gui.paint.MenuPainter;

public class MenuPanel extends MyPanel {
    private MainFrame mainFrame;
    private MenuPainter painter;
    private Image titleImage;
    private HashMap<Integer, String> menuMap;
    private int selectedMenu = 1;

    public MenuPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;

        painter = new MenuPainter();
        menuMap = new HashMap<Integer, String>();

        menuMap.put(1, "1 PLAYER");
        menuMap.put(2, "2 PLAYER");
        menuMap.put(3, "EXIT");

        try {
            titleImage = ImageIO.read(new File("gui/img/tetrish.png"));
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        painter.paintBackground((Graphics2D)g, new Rectangle(0, 0, MENU_W, MENU_H), new Color(12, 239, 249));
        g.drawImage(titleImage, 0, 30, this);

        for (int idx: menuMap.keySet()) {
            painter.paint((Graphics2D)g, menuMap.get(idx), new Rectangle(209, 230 + 50 * idx, 120, 40), (idx == selectedMenu));
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case UP:
                selectedMenu = (selectedMenu == 0 || selectedMenu == 1)? menuMap.size(): selectedMenu - 1;
                repaint();
                break;
            case DOWN:
                selectedMenu = (selectedMenu == 0 || selectedMenu == menuMap.size())? 1: selectedMenu + 1;
                repaint();
                break;
            case ENTER:
            case SPACE:
                mainFrame.switchPanel(this, menuMap.get(selectedMenu));
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {}

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {}

    @Override
    public void mousePressed(MouseEvent e) {}

    @Override
    public void mouseReleased(MouseEvent e) {}

    @Override
    public void mouseClicked(MouseEvent e) {
        if (selectedMenu != 0) mainFrame.switchPanel(this, menuMap.get(selectedMenu));
    }

    @Override
    public void mouseDragged(MouseEvent e) {}

    @Override
    public void mouseMoved(MouseEvent e) {
        Point point = e.getPoint();
        int x = point.x, y = point.y;

        if (x > 209 && x < 330) {
            if (y > 304 && y < 343) selectedMenu = 1;
            else if (y > 354 && y < 394) selectedMenu = 2;
            else if (y > 404 && y < 454) selectedMenu = 3;
            else selectedMenu = 0;
            repaint();
        }
    }
}
