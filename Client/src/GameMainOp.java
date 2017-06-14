package src;

import java.io.*;
import java.util.*;
import java.net.*;
import java.awt.*;

import static constant.Const.*;
import gui.paint.GamePainter;

public class GameMainOp extends GameMain {
    private GameSystem game;
    private GamePainter painter;
    private Thread t;
    private Socket socket;
    private Boolean initial = true;

    public Boolean didEscape = false;

    public GameMainOp(Socket socket) {
        this.socket = socket;

        game = new GameSystem();
        painter = new GamePainter();

        receiveAllData();

        t = new Thread(this);
        t.start();
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
        painter.draw(g, game, 1);
    }

    public void receiveAllData() {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            if (in.readLine().equals("score")) {
                game.score = Integer.parseInt(in.readLine());
            }
            if (in.readLine().equals("lineCnt")) {
                game.lineCnt = Integer.parseInt(in.readLine());
            }
            if (in.readLine().equals("grid")) {
                for (int i = 0; i < FIELD_H; i++) {
                    String str = in.readLine();
                    Scanner sc = new Scanner(str);
                    for (int j = 0; j < FIELD_W; j++) {
                        game.field.grid[i][j] = Integer.parseInt(sc.next());
                    }
                }
            }
            if (in.readLine().equals("hldBlk")) {
                game.hldBlk = Integer.parseInt(in.readLine());
            }
            if (in.readLine().equals("nextBlk")) {
                String str = in.readLine();
                Scanner sc = new Scanner(str.substring(1, str.length() - 1)).useDelimiter(", ");
                ArrayList<Integer> blkList = new ArrayList<Integer>();
                while (sc.hasNextInt()) blkList.add(sc.nextInt());
                game.nextBlk = blkList;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void receiveCommand() {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            String str = in.readLine();
            int blockId = Integer.parseInt(str);
            if (initial) {
                game.field.movBlk = new Block(blockId);
            }

            str = in.readLine();
            Scanner sc = new Scanner(str.substring(1, str.length() - 1)).useDelimiter(", ");
            ArrayList<Integer> blkList = new ArrayList<Integer>();
            while (sc.hasNextInt()) {
                blkList.add(sc.nextInt());
            }

            if (!initial && !isListEqual(blkList, game.nextBlk)) {
                int head = game.nextBlk.get(0);
                game.nextBlk = blkList;
                game.nextBlk.add(0, head);
            }
            else {
                initial = false;
                game.nextBlk = blkList;
            }

            str = in.readLine();
            int cmd = Integer.parseInt(str);

            if (cmd == 1) {
                game.update();
            }
            else game.command(cmd);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    private Boolean isListEqual(ArrayList<Integer> list1, ArrayList<Integer> list2) {
        for (int i = 0; i < 5; i++) {
            if (list1.get(i) != list2.get(i)) return false;
        }
        return true;
    }
}
