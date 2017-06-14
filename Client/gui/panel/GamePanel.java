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
    private Image bgImage;

    public int mode;

    public GamePanel(MainFrame mainFrame, int mode) {
        this.mainFrame = mainFrame;
        this.mode = mode;

        try {
            bgImage = ImageIO.read(new File("gui/img/background.png"));
        } catch(IOException e) {
            e.printStackTrace();
        }

        painter = new GamePainter();

        gameMain = (mode == 1)? new GameMain1(this): new GameMain2(this);
    }

    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D)g;

        // backgroud image
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

    public void backToMenu() {
        mainFrame.switchPanel(this, "MENU");
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

    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {}

    @Override
    public void mousePressed(MouseEvent e) {}

    @Override
    public void mouseReleased(MouseEvent e) {}

    @Override
    public void mouseClicked(MouseEvent e) {}

    @Override
    public void mouseDragged(MouseEvent e) {}

    @Override
    public void mouseMoved(MouseEvent e) {

    }
}
