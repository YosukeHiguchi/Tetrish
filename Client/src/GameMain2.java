package src;

import java.io.*;
import java.util.*;
import java.net.*;
import java.awt.*;

import static constant.Const.*;
import gui.panel.GamePanel;
import gui.paint.GamePainter;

public class GameMain2 extends GameMain {
    private GamePanel gamePanel;
    private GamePainter painter;
    private GameMainOp gameMainOp;
    private GameSystem game;
    private Thread t;
    private Boolean isKeyDown = false;
    private int contKey = -1;
    private int speed = INIT_SPEED;

    private Socket socket;

    public GameMain2(GamePanel gamePanel) {
        this.gamePanel = gamePanel;

        painter = new GamePainter();
        game = new GameSystem();

        try {
            InetAddress addr = InetAddress.getByName("localhost");
            socket = new Socket(addr, 50001);
            System.out.println("Connected: " + addr);
        } catch(IOException e) {
            e.printStackTrace();
        }

        sendData(0, game.field.movBlk, game.nextBlk);
        gameMainOp = new GameMainOp(socket);

        time = 0;
        onGame = true;

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

                    sendData(contKey, game.field.movBlk, game.nextBlk);
                }
                if (time > (speed - 5 * (game.lineCnt / 5))) {
                    game.update();
                    time = 0;

                    sendData(UPDATE, game.field.movBlk, game.nextBlk);
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

        //GameOver
        if (game.isGameOver) {
            gameMainOp.didEscape = true;
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
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
        sendData(key, game.field.movBlk, game.nextBlk);
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

        gameMainOp.drawGame(g);
    }

    public void sendData(int action, Block b, ArrayList<Integer> nextBlk) {
        try {
            PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);

            out.println(b.getId());
            out.println(nextBlk);
            out.println(action);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
