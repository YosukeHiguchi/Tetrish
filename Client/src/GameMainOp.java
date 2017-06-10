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

        game.field.clearMovingBlock();
        receiveData();
        game.field.setMovingBlock();

        t = new Thread(this);
        t.start();
    }

    @Override
    public void run() {
        while (!didEscape) {
            receiveData();
        }
    }

    public void keyAction(int key) {}
    public void keyReleased() {}

    public void drawGame(Graphics g) {
        painter.drawScore(g, 1, game.score, game.lineCnt);
        painter.drawField(g, 1, game.field);
        painter.drawHoldBlock(g, 1, game.hldBlk);
        painter.drawNextBlockList(g, 1, game.nextBlk);
    }

    public void receiveData() {
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

            if (!initial && !isEqual(blkList, game.nextBlk)) {
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
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    private Boolean isEqual(ArrayList<Integer> list1, ArrayList<Integer> list2) {
        for (int i = 0; i < 5; i++) {
            if (list1.get(i) != list2.get(i)) return false;
        }
        return true;
    }
}
