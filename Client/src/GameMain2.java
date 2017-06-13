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

    private Socket socket;

    public GameMain2(GamePanel gamePanel) {
        this.gamePanel = gamePanel;

        painter = new GamePainter();
        game = new GameSystem();

        Scanner sc = new Scanner(System.in);
        System.out.print("Input address -> ");
        String str = sc.nextLine();

        try {
            InetAddress addr = InetAddress.getByName(str);
            socket = new Socket(addr, 50001);
            System.out.println("Connected: " + addr);
        } catch(IOException e) {
            e.printStackTrace();
        }

        sendAllData();
        //sendData(0, game.field.movBlk, game.nextBlk);
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

                    sendAllData();
                    //sendData(contKey, game.field.movBlk, game.nextBlk);
                }

                int lv = game.lineCnt / 5 + 1;
                if (lv > SPEED_LV.length) lv = SPEED_LV.length;

                if (time > SPEED_LV[lv - 1]) {
                    game.update();
                    time = 0;

                    sendAllData();
                    //sendData(UPDATE, game.field.movBlk, game.nextBlk);
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
        sendAllData();
        //sendData(key, game.field.movBlk, game.nextBlk);
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

    public void sendAllData() {
        try {
            PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);

            out.println(game.score);
            out.println(game.lineCnt);
            for (int i = 0; i < FIELD_H; i++) {
                for (int j = 0; j < FIELD_W; j++) {
                    out.print(game.field.grid[i][j] + " ");
                }
                out.println();
            }
            out.println(game.hldBlk);
            out.println(game.nextBlk);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
