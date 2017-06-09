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

public class MenuPanel extends MyPanel{
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
                selectedMenu = (selectedMenu == 1)? menuMap.size(): selectedMenu - 1;
                repaint();
                break;
            case DOWN:
                selectedMenu = (selectedMenu == menuMap.size())? 1: selectedMenu + 1;
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
}
