package src;

import static constant.Const.*;

public class Field {
    private GameSystem game;
    private int grid[][];
    private Block movBlk = null;
    private Block guideBlock = null;

    public Field(GameSystem game) {
        this.game = game;
        grid = new int[FIELD_H + 3][FIELD_W];
    }

    public Block getMovBlk() {
        return movBlk;
    }

    public Block getGuideBlock() {
        return guideBlock;
    }

    public int getGrid(int x, int y) {
        return grid[x][y];
    }

    public void setGrid(int x, int y, int val) {
        grid[x][y] = val;
    }

    public void spawnBlock(int blockId) {
        movBlk = new Block(blockId);
    }

    public void moveBlock(int dir) {
        clearMovingBlock();
        switch(dir) {
            case RIGHT:
                if (isBlockMovable(movBlk, 0)) movBlk.moveRight();
                break;
            case LEFT:
                if (isBlockMovable(movBlk, 1)) movBlk.moveLeft();
                break;
            case DOWN:
                if (isBlockMovable(movBlk, 2)) {
                    game.setScore(game.getScore() + 1);
                    movBlk.moveDown();
                }
                else {
                    setMovingBlock();
                    game.update();
                }
                break;
            case KEY_X:
            case UP:
                //movBlk.turnRight();
                adjustTurningBlock(true);
                break;
            case KEY_Z:
                //movBlk.turnLeft();
                adjustTurningBlock(false);
                break;
            case SPACE:
                while (isBlockMovable(movBlk, 2)) {
                    game.setScore(game.getScore() + 1);
                    game.setGauge(game.getGauge() + 1);
                    movBlk.moveDown();
                }

                if (game.getGauge() > MAX_GAUGE) game.setGauge(MAX_GAUGE);

                setMovingBlock();
                game.update();
                break;
        }

        setMovingBlock();
    }

    /**
    * remove the moving block from grid
    */
    public void clearMovingBlock() {
        for (int i = 0; i < movBlk.getSize(); i++) {
            if (movBlk.getY(i) < 0)
                grid[movBlk.getY(i) + FIELD_H + 3][movBlk.getX(i)] = 0;
            else
                grid[movBlk.getY(i)][movBlk.getX(i)] = 0;

        }
    }
    /**
    * set the moving block on grid
    */
    public Boolean setMovingBlock() {
        if (grid[0][4] != 0 || grid[0][5] != 0) return false;
        for (int i = 0; i < movBlk.getSize(); i++) {
            if (movBlk.getY(i) < 0)
                grid[movBlk.getY(i) + FIELD_H + 3][movBlk.getX(i)] = movBlk.getId();
            else
                grid[movBlk.getY(i)][movBlk.getX(i)] = movBlk.getId();
        }

        calcGuideBlock();

        return true;
    }
    /**
    * Delete lines
    * @return the number of lines deleted
    */
    public int deleteLine() {
        int delCnt = 0;

        for (int i = 0; i < FIELD_H; i++) {
            int cnt = 0;
            for (; cnt < FIELD_W; cnt++) {
                if (grid[i][cnt] == 0) break;
            }
            if (cnt == FIELD_W) {
                delCnt++;
                for (int j = 0; j < FIELD_W; j++) grid[i][j] = 0;
                for (int j = i; j > 0; j--) {
                    for (int k = 0; k < FIELD_W; k++) {
                        grid[j][k] = grid[j - 1][k];
                        grid[j - 1][k] = 0;
                    }
                }
                //blocks over the field
                for (int j = FIELD_H + 2; j > FIELD_H; j--) {
                    for (int k = 0; k < FIELD_W; k++) {
                        if (j == FIELD_H + 2) {
                            grid[0][k] = grid[j][k];
                            grid[j][k] = 0;
                        } else {
                            grid[j][k] = grid[j - 1][k];
                            grid[j - 1][k] = 0;
                        }
                    }
                }
            }
        }

        return delCnt;
    }

    /**
    * checks if the moving block can move in certain direction
    * @param vec 0: right 1: left 2: down
    */
    public Boolean isBlockMovable(Block b, int vec) {
        if (b == null) return false;

        int vecx[] = {1, -1, 0};
        int vecy[] = {0, 0, 1};
        //block out of field
        for (int i = 0; i < BLOCK_SPAWN_X[movBlk.getId() - 1].length; i++) {
            if (b.getX(i) + vecx[vec] >= FIELD_W ||
                b.getX(i) + vecx[vec] < 0        ||
                b.getY(i) + vecy[vec] >= FIELD_H )
            return false;
        }
        //block towards fixed block
        for (int i = 0; i < BLOCK_SPAWN_X[movBlk.getId() - 1].length; i++) {
            if (b.getY(i) < 0) continue;

            int adjBlk = grid[b.getY(i) + vecy[vec]][b.getX(i) + vecx[vec]];
            if (adjBlk != 0) {
                if (adjBlk == b.getId()) {
                    if (!b.isMyBlock(b.getX(i) + vecx[vec], b.getY(i) + vecy[vec])) return false;
                }
                else return false;
            }
        }
        return true;
    }

    private void calcGuideBlock() {
        int x[] = new int[movBlk.getSize()];
        int y[] = new int[movBlk.getSize()];
        for (int i = 0; i < movBlk.getSize(); i++) {
            x[i] = movBlk.getX(i);
            y[i] = movBlk.getY(i);
        }

        guideBlock = new Block(movBlk.getId(), x, y);

        while (isBlockMovable(guideBlock, 2)) guideBlock.moveDown();
    }

    public void adjustTurningBlock(Boolean isRightTurn) {
        Block b = movBlk;

        if (isRightTurn) b.turnRight();
        else b.turnLeft();

        // judge below (at the edge)
        while (b.maxY() > FIELD_H - 1) b.moveUp();
        // judge side (at the edge)
        while (b.maxX() > FIELD_W - 1) b.moveLeft();
        while (b.minX() < 0) b.moveRight();
        // // judge below (on the block)
        for (int n = 0; n < 2; n++) {
            for (int i = 0; i < 4; i++) {
                int maxY = b.maxY();
                if (b.getY(i) == maxY && grid[maxY][b.getX(i)] != 0) b.moveUp();
                if (b.getId() == 1 && b.getY(i) == maxY - 1 && grid[maxY - 1][b.getX(i)] != 0) b.moveUp();
            }
        }


        // judge side (next to block)
        //
        // for (int n = 0; n < 2; n++) {
        //     for (int i = 0; i < 4; i++) {
        //         int maxX = b.maxX();
        //         if (b.getX(i) == maxX && grid[b.getY(i)][maxX] != 0) b.moveLeft();
        //         //if (b.getId() == 1 && b.getX(i) == maxX - 1 && grid[b.getY(i)][maxX - 1] != 0) b.moveLeft();
        //     }
        // }

        movBlk = b;
    }
}
