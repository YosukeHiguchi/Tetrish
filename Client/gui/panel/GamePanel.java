package gui.panel;

import java.io.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.imageio.ImageIO;

import static constant.Const.*;
import src.*;
import gui.MainFrame;
import gui.paint.GamePainter;

public class GamePanel extends MyPanel{
    private MainFrame mainFrame;
    private GamePainter painter;
    private GameMain gameMain;
    private Thread t;
    private int player;
    private Image bgImage;

    public GamePanel(MainFrame mainFrame, int player) {
        this.mainFrame = mainFrame;
        this.player = player;

        try {
            bgImage = ImageIO.read(new File("gui/img/background.png"));
        } catch(IOException e) {
            e.printStackTrace();
        }

        painter = new GamePainter();

        gameMain = (player == 1)? new GameMain1(this): new GameMain2(this);
    }

    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D)g;

        //backgroud image
        g.drawImage(bgImage, 0, 0, this);

        if (!gameMain.onGame) {
            if (gameMain.time == -1)
                painter.drawPauseScreen(g2d, "PRESS SPACE TO START");
            else {
                gameMain.drawGame(g);
                painter.drawPauseScreen(g2d, "GAMEOVER!! PRESS ESC");
            }
        } else {
            gameMain.drawGame(g);
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        gameMain.keyAction(e.getKeyCode());
    }

    @Override
    public void keyReleased(KeyEvent e) {
        gameMain.keyReleased();
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    public void backToMenu() {
        mainFrame.switchPanel(this, "MENU");
    }
}
