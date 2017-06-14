package constant;

import java.awt.Color;
import java.awt.event.KeyEvent;

public class Const {
    private Const() {}

    public static final String OS_NAME = System.getProperty("os.name").toLowerCase();

    public static final int MENU_W = 539;
    public static final int MENU_H = 500;

    public static final int GAME_W = 960;
    public static final int GAME_H = 720;
    public static final int GAME_H_WIN = 750;


    public static final int FIELD_W = 10;
    public static final int FIELD_H = 20;

    public static final int RIGHT = KeyEvent.VK_RIGHT;
    public static final int LEFT  = KeyEvent.VK_LEFT;
    public static final int UP    = KeyEvent.VK_UP;
    public static final int DOWN  = KeyEvent.VK_DOWN;
    public static final int KEY_X = KeyEvent.VK_X;
    public static final int KEY_Z = KeyEvent.VK_Z;
    public static final int KEY_C = KeyEvent.VK_C;
    public static final int SPACE = KeyEvent.VK_SPACE;
    public static final int SHIFT = KeyEvent.VK_SHIFT;
    public static final int ENTER = KeyEvent.VK_ENTER;
    public static final int ESC   = KeyEvent.VK_ESCAPE;

    public static final int UPDATE = 1;

    public static final int SPEED_LV[] = {100, 95, 90, 85, 80, 75, 70, 65, 60, 55,
                                           50, 45, 40, 35, 30, 25, 20, 15, 12, 10,
                                            9,  8,  7,  6,  5,  4,  3,  2,  1,  0};

    public static final int BLOCK_SPAWN_X[][] = {
        {3, 4, 5, 6},
        {4, 3, 3, 5},
        {4, 3, 5, 5},
        {4, 5, 4, 5},
        {4, 5, 4, 3},
        {4, 3, 4, 5},
        {4, 4, 3, 5},
        {5, 4, 6, 4, 5, 6, 4, 5, 6}
    };
    public static final int BLOCK_SPAWN_Y[][] = {
        { 0,  0,  0,  0},
        { 0,  0, -1,  0},
        { 0,  0, -1,  0},
        { 0, -1, -1,  0},
        { 0, -1, -1,  0},
        { 0,  0, -1,  0},
        { 0, -1, -1,  0},
        {-1, -1, -1, 0, 0, 0, -2, -2, -2}
    };
    public static final Color BLOCK_COLOR[] = {
        new Color(0, 240, 240), new Color(0, 0, 240),
        new Color(240, 160, 0), new Color(240, 240, 0),
        new Color(0, 240, 0), new Color(160, 0, 240),
        new Color(240, 0, 0), new Color(160, 160, 160)
    };
}
/*
1
口口口口
2
口
口口口
3
　　口
口口口
4
口口
口口
5
　口口
口口
6
　口
口口口
7
口口
　口口

8
口口口
口口口
口口口
*/
