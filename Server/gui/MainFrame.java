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
        GamePanel panel = new GamePanel(game);

        setTitle(title);
        setSize(GAME_W, GAME_H);
        setResizable(false);
        setLocationRelativeTo(null); //center window
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //close

        CP.add(panel);
    }
}
