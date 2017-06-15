package gui;

import java.awt.*;
import javax.swing.*;

import static constant.Const.*;
import gui.panel.*;

public class MainFrame extends JFrame {
    private Container CP;

    public MainFrame(String title) {
        CP = getContentPane();

        setFrame(title, MENU_W, MENU_H, new MenuPanel(this));
    }

    private void setFrame(String title, int width, int height, MyPanel panel) {
        setTitle(title);
        setSize(width, height);
        setResizable(false);
        setLocationRelativeTo(null); //center window
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //close

        panel.setLayout(null);

        addKeyListener(panel);
        addMouseListener(panel);
        addMouseMotionListener(panel);

        CP.add(panel);
    }

    public void switchPanel(JPanel panel, String str, String addr) {
        removeKeyListener((MyPanel)panel);
        removeMouseListener((MyPanel)panel);
        removeMouseMotionListener((MyPanel)panel);
        CP.remove(panel);

        int h = (OS_NAME.startsWith("windows"))? GAME_H_WIN: GAME_H;

        if (str.equals("1 PLAYER")) {
            System.out.println("1 PLAYER GAME");
            setFrame("Game", GAME_W / 2, h, new GamePanel(this));
        }
        else if (str.equals("2 PLAYER")) {
            System.out.println("2 PLAYER GAME");
            setFrame("Game", GAME_W, h, new GamePanel(this, addr));
        }
        else if (str.equals("MENU")) {
            System.out.println("MENU SELECTION");
            setFrame("Menu", MENU_W, MENU_H, new MenuPanel(this));
        }
        else if (str.equals("EXIT")) {
            System.out.println("See ya!");
            System.exit(0);
        }
    }
}
