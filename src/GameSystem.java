package src;

import java.util.*;
import java.awt.*;

import static constant.Const.*;

public class GameSystem {
    int score = 0;
    int lineCnt = 0;
    int hldBlk = -1;
    Boolean didHold = false;
    Field field;
    ArrayList<Integer> nextBlk;
    Boolean isGameOver = false;

    public GameSystem() {
        field = new Field(this);
        nextBlk = new ArrayList<Integer>();

        update();
    }

    public void update() {
        if (!field.isBlockMovable(field.movBlk, 2)) {
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
        while (nextBlk.size() < 6) {
            nextBlk.add((int)(Math.random() * 7) + 1);
        }
        field.spawnBlock(nextBlk.get(0));
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
            hldBlk = field.movBlk.getId();
            updateBlock();
        } else {
            int hoge = hldBlk;
            hldBlk = field.movBlk.getId();
            field.spawnBlock(hoge);
        }
        field.setMovingBlock();
    }
}
