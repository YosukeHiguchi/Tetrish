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

public class GamePanel extends MyPanel implements Runnable {
    private MainFrame mainFrame;
    private GamePainter painter;
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

        t = new Thread(this);
        t.start();
    }

    @Override
    public void run() {
        while (true) {
            repaint();

            try{
                Thread.sleep(10); //0.01sec
            }catch (InterruptedException e){
                e.printStackTrace();
            }
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        g.drawImage(bgImage, 0, 0, this);
    }

    @Override
    public void keyPressed(KeyEvent e) {}

    @Override
    public void keyReleased(KeyEvent e) {}

    @Override
    public void keyTyped(KeyEvent e) {}
}
