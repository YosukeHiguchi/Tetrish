package src;

import java.io.*;
import java.util.*;
import java.net.*;
import java.awt.*;

import static constant.Const.*;
import gui.paint.GamePainter;

public class GameMainOp extends GameMain {
    private GameSystem game;
    private GameMain2 gameMain2;
    private GamePainter painter;
    private Thread t;
    private Socket socket;
    private Boolean initial = true;
    private Boolean didEscape = false;

    public GameMainOp(GameMain2 gameMain2, Socket socket) {
        this.gameMain2 = gameMain2;
        this.socket = socket;

        game = new GameSystem();
        painter = new GamePainter();

        receiveAllData();

        t = new Thread(this);
        t.start();
    }

    public void setDidEscape(Boolean didEscape) {
        this.didEscape = didEscape;
    }

    @Override
    public void run() {
        while (!didEscape) {
            receiveAllData();
        }
    }

    public void keyAction(int key) {}
    public void keyReleased() {}

    public void drawGame(Graphics g) {
        painter.draw(g, game, 1, 2);
    }

    public void receiveAllData() {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            if (in.readLine().equals("score")) {
                game.setScore(Integer.parseInt(in.readLine()));
            }
            if (in.readLine().equals("lineCnt")) {
                game.setLineCnt(Integer.parseInt(in.readLine()));
            }
            if (in.readLine().equals("grid")) {
                for (int i = 0; i < FIELD_H; i++) {
                    String str = in.readLine();
                    Scanner sc = new Scanner(str);
                    for (int j = 0; j < FIELD_W; j++) {
                        game.getField().setGrid(i, j, Integer.parseInt(sc.next()));
                    }
                }
            }
            if (in.readLine().equals("hldBlk")) {
                game.setHldBlk(Integer.parseInt(in.readLine()));
            }
            if (in.readLine().equals("nextBlk")) {
                String str = in.readLine();
                Scanner sc = new Scanner(str.substring(1, str.length() - 1)).useDelimiter(", ");
                ArrayList<Integer> blkList = new ArrayList<Integer>();
                while (sc.hasNextInt()) blkList.add(sc.nextInt());
                game.setNextBlk(blkList);
            }
            if (in.readLine().equals("gauge")) {
                game.setGauge(Integer.parseInt(in.readLine()));
            }
            if (in.readLine().equals("tetrish")) {
                String boo = in.readLine();
                if (boo.equals("true")) {
                    gameMain2.getGameSystem().setHldBlk(8);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
