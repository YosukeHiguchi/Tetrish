package src;

import static constant.Const.*;

public class Block {
    private int id;
    private int x[];
    private int y[];
    private int phase;

    public Block(int id) {
        phase = 1;
        this.id = id;

        x = new int[BLOCK_SPAWN_X[id - 1].length];
        y = new int[BLOCK_SPAWN_Y[id - 1].length];

        for (int i = 0; i < BLOCK_SPAWN_X[id - 1].length; i++) {
            x[i] = BLOCK_SPAWN_X[id - 1][i];
            y[i] = BLOCK_SPAWN_Y[id - 1][i];
        }
    }

    public Block(int id, int x[], int y[]) {
        this.id = id;

        this.x = new int[x.length];
        this.y = new int[y.length];

        for (int i = 0; i < x.length; i++) {
            this.x[i] = x[i];
            this.y[i] = y[i];
        }
    }

    public int getId() {
        return id;
    }

    public int getX(int n) {
        return x[n];
    }

    public int getY(int n) {
        return y[n];
    }

    public int getPhase() {
        return phase;
    }

    public int maxX() {
        return maxInArray(x);
    }

    public int maxY() {
        return maxInArray(y);
    }

    public int minX() {
        return minInArray(x);
    }

    public int getSize() {
        return BLOCK_SPAWN_X[id - 1].length;
    }

    public int maxInArray(int arr[]) {
        int max = arr[0];

        for (int i = 1; i < getSize(); i++) {
            if (max < arr[i]) max = arr[i];
        }

        return max;
    }
    public int minInArray(int arr[]) {
        int min = arr[0];

        for (int i = 1; i < getSize(); i++) {
            if (min > arr[i]) min = arr[i];
        }

        return min;
    }

    public void moveRight() {
        for (int i = 0; i < getSize(); i++) x[i]++;
    }

    public void moveLeft() {

        for (int i = 0; i < getSize(); i++) x[i]--;
    }

    public void moveDown() {
        for (int i = 0; i < getSize(); i++) y[i]++;
    }

    public void moveUp() {
        for (int i = 0; i < getSize(); i++) y[i]--;
    }

    //turn around the 1st coordinate
    public void turnRight() {
        if (id == 1) turnBarBlock(true);
        else if (id != 4) turnCell(x[0], y[0], Math.PI / 2);

        phase = (phase == 4)? 1: phase + 1;
    }

    public void turnLeft() {
        if (id == 1) turnBarBlock(false);
        else if (id != 4) turnCell(x[0], y[0], -Math.PI / 2);

        phase = (phase == 1)? 4: phase - 1;
    }

    public void turnBarBlock(Boolean isRightTurn) {
        int centerX = -1, centerY = -1;

        if (isRightTurn) {
            centerX = (phase == 1 || phase == 3)? x[2]: x[1];
            centerY = (phase == 1 || phase == 3)? y[2]: y[1];
            turnCell(centerX, centerY, -Math.PI / 2);
        }
        else {
            centerX = (phase == 1 || phase == 3)? x[1]: x[2];
            centerY = (phase == 1 || phase == 3)? y[1]: y[2];
            turnCell(centerX, centerY, Math.PI / 2);
        }
    }

    public void turnCell(int centerX, int centerY, double angle) {
        for (int i = 0; i < getSize(); i++) {
            int nx = (y[i] - centerY) * (int)Math.sin(angle) * -1 + centerX;
            int ny = (x[i] - centerX) * (int)Math.sin(angle) + centerY;
            x[i] = nx;
            y[i] = ny;
        }
    }

    public Boolean isMyBlock(int gridX, int gridY) {
        for (int i = 0; i < getSize(); i++) {
            if (x[i] == gridX && y[i] == gridY) return true;
        }
        return false;
    }
}
