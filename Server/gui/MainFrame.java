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
        System.out.println("OS: " + OS_NAME);

        CP = getContentPane();
        GamePanel panel = new GamePanel(game);

        setTitle(title);
        if (OS_NAME.startsWith("windows")) {
            setSize(GAME_W, GAME_H_WIN);
        } else {
            setSize(GAME_W, GAME_H);
        }
        setResizable(false);
        setLocationRelativeTo(null); //center window
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //close

        CP.add(panel);
    }
}
