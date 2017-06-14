package src;

import java.util.*;
import java.awt.*;
import java.security.*;

import static constant.Const.*;

public class GameSystem {
    private int score = 0;
    private int lineCnt = 0;
    private int hldBlk = -1;
    private Field field;
    private ArrayList<Integer> nextBlk;

    private Boolean didHold = false;
    private Boolean isGameOver = false;

    public GameSystem() {
        field = new Field(this);
        nextBlk = new ArrayList<Integer>();

        update();
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getLineCnt() {
        return lineCnt;
    }

    public void setLineCnt(int lineCnt) {
        this.lineCnt = lineCnt;
    }

    public int getHldBlk() {
        return hldBlk;
    }

    public void setHldBlk(int hldBlk) {
        this.hldBlk = hldBlk;
    }

    public Field getField() {
        return field;
    }

    public ArrayList<Integer> getNextBlk() {
        return nextBlk;
    }

    public void setNextBlk(ArrayList<Integer> nextBlk) {
        this.nextBlk = nextBlk;
    }

    public void update() {
        if (!field.isBlockMovable(field.getMovBlk(), 2)) {
            updateScore();
            updateBlock();
            didHold = false;
        } else {
            field.moveBlock(DOWN);
            score--;
        }
    }

    /**
    * update next blocklist and spawns new moving block
    */
    public void updateBlock() {
        SecureRandom secRandom = null;
        while (nextBlk.size() < 6) {
            byte bytes[] = new byte[16];
            try {
                secRandom = SecureRandom.getInstance("SHA1PRNG");
                secRandom.nextBytes(bytes);
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
            nextBlk.add((int)(secRandom.nextDouble() * 7) + 1);
        }

        field.spawnBlock(nextBlk.get(0));
        //Game ends when there is no space to put block
        if (!field.setMovingBlock()) isGameOver = true;
        nextBlk.remove(0);
    }

    public void updateScore() {
        int delLine = field.deleteLine();

        lineCnt += delLine;
        if (delLine == 1) score += 40;
        else if (delLine == 2) score += 100;
        else if (delLine == 3) score += 300;
        else if (delLine == 4) score += 1200;
    }

    public void command(int key) {
        Integer n[] = {RIGHT, LEFT, DOWN, KEY_Z, KEY_X, SPACE, UP};
        if (Arrays.asList(n).contains(key)) {
            field.moveBlock(key);
            return;
        }

        switch (key) {
            case KEY_C:
            case SHIFT:
                if (!didHold) holdBlock();
                didHold = true;
                break;
        }
    }

    public void holdBlock() {
        field.clearMovingBlock();
        if (hldBlk == -1) {
            hldBlk = field.getMovBlk().getId();
            updateBlock();
        } else {
            int hoge = hldBlk;
            hldBlk = field.getMovBlk().getId();
            field.spawnBlock(hoge);
        }
        field.setMovingBlock();
    }
}
