package gui;

import java.util.*;
import java.awt.*;
import javax.swing.*;

import static constant.Const.*;
import gui.panel.*;
import src.*;

public class MainFrame extends JFrame {
    private Container CP;

    public MainFrame(String title, ArrayList<GameSystem> game) {
        CP = getContentPane();

        System.out.println("hi");

        setFrame(title, GAME_W, GAME_H, new GamePanel(this, 2, game));
    }

    private void setFrame(String title, int width, int height, MyPanel panel) {
        setTitle(title);
        setSize(width, height);
        setResizable(false);
        setLocationRelativeTo(null); //center window
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //close

        addKeyListener(panel);
        CP.add(panel);
    }
}
