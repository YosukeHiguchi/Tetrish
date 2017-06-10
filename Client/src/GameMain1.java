package src;

import java.awt.*;

import static constant.Const.*;
import gui.panel.GamePanel;
import gui.paint.GamePainter;

public class GameMain1 extends GameMain {
    private GamePanel gamePanel;
    private GamePainter painter;
    private GameSystem game;
    private Thread t;
    private Boolean isKeyDown = false;
    private int contKey = -1;
    private int speed = INIT_SPEED;

    public GameMain1(GamePanel gamePanel) {
        this.gamePanel = gamePanel;

        painter = new GamePainter();
        game = new GameSystem();

        t = new Thread(this);
        t.start();
    }

    @Override
    public void run() {
        while (!game.isGameOver) {
            if (onGame) {
                if (isKeyDown && time > 5) {
                    time = 0;
                    game.command(contKey);
                }
                if (time > (speed - 5 * (game.lineCnt / 5))) {
                    game.update();
                    time = 0;
                }

                time++;
            }

            try{
                Thread.sleep(10); //0.01sec
            }catch (InterruptedException e){
                e.printStackTrace();
            }

            gamePanel.repaint();
        }
        onGame = false;
        while (!onGame) {
            gamePanel.repaint();

            try{
                Thread.sleep(10); //0.01sec
            }catch (InterruptedException e){
                e.printStackTrace();
            }
        }

    }

    public void keyAction(int key) {
        if (key == SPACE && !onGame && !game.isGameOver) {
            onGame = true;
            return;
        }

        //Pause
        if (key == ESC && onGame) {
            onGame = false;
            time = -1;
            return;
        }
        //GameOver
        if (game.isGameOver) {
            if (key == ESC) gamePanel.backToMenu();
            return;
        }

        if (isKeyDown) return;
        else if (key == DOWN) {
            isKeyDown = true;
            contKey = DOWN;
            return;
        }

        game.command(key);
    }

    public void keyReleased() {
        isKeyDown = false;
    }

    public void drawGame(Graphics g) {
        if (game.field.guideBlock != null)
            painter.drawGuideBlock(g, game.field.guideBlock);

        painter.drawScore(g, 0, game.score, game.lineCnt);
        painter.drawField(g, 0, game.field);
        painter.drawHoldBlock(g, 0, game.hldBlk);
        painter.drawNextBlockList(g, 0, game.nextBlk);
    }
}
