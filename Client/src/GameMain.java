package src;

import java.awt.Graphics;

public abstract class GameMain implements Runnable {
    public int time = -1;
    public Boolean onGame = false;

    public abstract void keyAction(int key);
    public abstract void keyReleased();
    public abstract void drawGame(Graphics g);
}
