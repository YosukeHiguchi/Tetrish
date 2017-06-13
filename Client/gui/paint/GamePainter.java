package gui.paint;

import java.util.*;
import java.awt.*;

import src.Field;
import src.Block;

import static constant.Const.*;

public class GamePainter {
    private int pauseCount = 0;
    private Boolean pauseStrOn = true;
    public void drawPauseScreen(Graphics2D g2d, String message) {
        pauseCount++;
        if (pauseCount > 50) {
            pauseCount = 0;
            pauseStrOn = (pauseStrOn)? false: true;
        }
        if (!pauseStrOn) return;

        g2d.setColor(Color.WHITE);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2d.setFont(new Font("Impact", Font.BOLD, 50));
        g2d.drawString(message, 15, GAME_H / 2);
    }

    public void drawScore(Graphics g, int player, int score, int lineCnt) {
        int pos = (player == 0)? 0: 482;
        Graphics2D g2d = (Graphics2D)g;

        g2d.setColor(Color.BLACK);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2d.setFont(new Font("Impact", Font.BOLD, 30));
        int level = lineCnt / 5 + 1;
        if (level >= 30) level = 30;
        g2d.drawString("Lv:   " + level, 92 + pos, 66);
        g2d.drawString("SCORE:   " + String.format("%06d", score), 198 + pos, 66);
    }

    public void drawField(Graphics g, int player, Field field) {
        for (int y = 0; y < FIELD_H; y++) {
            for (int x = 0; x < FIELD_W; x++) {
                if (field.grid[y][x] != 0) drawCell(g, player, x, y, BLOCK_COLOR[field.grid[y][x] - 1]);
            }
        }
    }

    public void drawHoldBlock(Graphics g, int player, int hldBlk) {
        if (hldBlk == -1) return;

        int pos = (player == 0)? 0: 482;
        Color c = BLOCK_COLOR[hldBlk - 1];

        for (int i = 0; i < 4; i++) {
            if (hldBlk == 1)
            drawMiniCell(g, 20 + pos, 104, 13, BLOCK_SPAWN_X[hldBlk - 1][i] - 2, BLOCK_SPAWN_Y[hldBlk - 1][i], c);
            else if (hldBlk == 4)
            drawMiniCell(g, 9 + pos, 93, 17, BLOCK_SPAWN_X[hldBlk - 1][i] - 2, BLOCK_SPAWN_Y[hldBlk - 1][i] + 1, c);
            else
            drawMiniCell(g, 17 + pos, 93, 17, BLOCK_SPAWN_X[hldBlk - 1][i] - 2, BLOCK_SPAWN_Y[hldBlk - 1][i] + 1, c);
        }
    }

    public void drawGuideBlock(Graphics g, Block b) {
        Color base = BLOCK_COLOR[b.getId() - 1];
        Color c = new Color(base.getRed(), base.getGreen(), base.getBlue(), 30);

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                drawCell(g, 0, b.getX(i), b.getY(i), c);
            }
        }
    }

    public void drawNextBlockList(Graphics g, int player, ArrayList<Integer> nextBlk) {
        int next = nextBlk.get(0);
        int pos = (player == 0)? 0: 482;
        Color c = BLOCK_COLOR[next - 1];

        for (int i = 0; i < 4; i++) {
            if (next == 1)
            drawMiniCell(g, 381 + pos, 104, 13, BLOCK_SPAWN_X[next - 1][i] - 2, BLOCK_SPAWN_Y[next - 1][i], c);
            else if (next == 4)
            drawMiniCell(g, 370 + pos, 93, 17, BLOCK_SPAWN_X[next - 1][i] - 2, BLOCK_SPAWN_Y[next - 1][i] + 1, c);
            else
            drawMiniCell(g, 378 + pos, 93, 17, BLOCK_SPAWN_X[next - 1][i] - 2, BLOCK_SPAWN_Y[next - 1][i] + 1, c);
        }

        for (int i = 1; i < 5; i++) {
            next = nextBlk.get(i);
            g.setColor(BLOCK_COLOR[next - 1]);
            for (int j = 0; j < 4; j++) {
                if (next == 1)
                g.fillRect(365 + 11 * BLOCK_SPAWN_X[next - 1][j] + pos, 105 + 65 * i + 11 * BLOCK_SPAWN_Y[next - 1][j], 11, 11);
                else if (next == 4)
                g.fillRect(355 + 13 * BLOCK_SPAWN_X[next - 1][j] + pos, 110 + 65 * i + 13 * BLOCK_SPAWN_Y[next - 1][j], 13, 13);
                else
                g.fillRect(362 + 13 * BLOCK_SPAWN_X[next - 1][j] + pos, 110 + 65 * i + 13 * BLOCK_SPAWN_Y[next - 1][j], 13, 13);
            }
        }
    }
    /**
    * 指定したフィールド上の座標にブロックセルを描く
    * @param gridX field.gridのx座標
    * @param gridY field.gridのy座標
    */
    public void drawCell(Graphics g, int player, int gridX, int gridY, Color c) {
        Graphics2D g2d = (Graphics2D)g;
        int pos = (player == 0)? 0: 482;

        g.setColor(c);
        g.fillRoundRect(91 + pos + 30 * gridX, 80 + 30 * gridY, 28, 28, 2, 2);

        BasicStroke stroke = new BasicStroke(3.0f);
        g2d.setStroke(stroke);

        g2d.setColor(c.darker());
        g2d.drawLine(118 + pos + 30 * gridX, 107 + 30 * gridY, 118 + pos + 30 * gridX - 26, 107 + 30 * gridY);
        g2d.drawLine(118 + pos + 30 * gridX, 107 + 30 * gridY, 118 + pos + 30 * gridX, 107 + 30 * gridY - 26);

        g2d.setColor(c.brighter());
        g2d.drawLine(91 + pos + 30 * gridX, 80 + 30 * gridY, 91 + pos + 30 * gridX + 26, 80 + 30 * gridY);
        g2d.drawLine(91 + pos + 30 * gridX, 80 + 30 * gridY, 91 + pos + 30 * gridX, 80 + 30 * gridY + 26);
    }
    /**
    * holdBlock、nextBlock用の小さいブロックセルを描く
    * (frameX, frameY)を原点として、1単位の長さlenとして座標を設定した時の(gridX, gridY)にブロックセルを描く
    * @param frameX MainPanelのxピクセル値
    * @param frameY MainPanelのyピクセル値
    * @param len MainPanelのピクセル値
    */
    public void drawMiniCell(Graphics g, int frameX, int frameY, int len, int gridX, int gridY, Color c) {
        Graphics2D g2d = (Graphics2D)g;

        g.setColor(c);
        g.fillRect(frameX + gridX * len, frameY + gridY * len, len, len);

        BasicStroke stroke = new BasicStroke(2.0f);
        g2d.setStroke(stroke);

        g2d.setColor(c.darker());
        g2d.drawLine(frameX + len - 1 + len * gridX, frameY + len - 1 + len * gridY, frameX + len - 1 + len * gridX - len + 2, frameY + len - 1 + len * gridY);
        g2d.drawLine(frameX + len - 1 + len * gridX, frameY + len - 1 + len * gridY, frameX + len - 1 + len * gridX, frameY + len - 1 + len * gridY - len + 2);

        g2d.setColor(c.brighter());
        g2d.drawLine(frameX + 1 + len * gridX, frameY + 1 + len * gridY, frameX + 1 + len * gridX + len - 2, frameY + 1 + len * gridY);
        g2d.drawLine(frameX + 1 + len * gridX, frameY + 1 + len * gridY, frameX + 1 + len * gridX, frameY + 1 + len * gridY + len - 2);
    }
}
