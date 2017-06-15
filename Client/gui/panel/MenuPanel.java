package gui.panel;

import java.io.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.imageio.ImageIO;

import static constant.Const.*;
import gui.MainFrame;
import gui.paint.MenuPainter;

public class MenuPanel extends MyPanel implements Runnable {
    private MainFrame mainFrame;
    private MenuPainter painter;
    private Image titleImage;
    private HashMap<Integer, String> menuMap;
    private int selectedMenu = 1;
    private Thread anim;
    private Boolean slide = false;
    private int slide_w = 0;
    private JTextField addrText;
    private MenuPanel mp = this;

    public MenuPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;

        painter = new MenuPainter();
        menuMap = new HashMap<Integer, String>();
        addrText = new JTextField(20);
        addrText.setBounds(368, 350, 120, 18);
        addrText.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                String addr = addrText.getText();
                mainFrame.requestFocus();
                mainFrame.switchPanel(mp, menuMap.get(selectedMenu), addr);
            }
        });

        menuMap.put(1, "1 PLAYER");
        menuMap.put(2, "2 PLAYER");
        menuMap.put(3, "EXIT");

        try {
            titleImage = ImageIO.read(new File("gui/img/tetrish.png"));
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        slide = true;
        for (int i = 0; i < 200; i+=2) {
            slide_w = i;
            repaint();

            try {
                anim.sleep(1);
            } catch (InterruptedException ie) {
                ie.printStackTrace();
            }
        }

        this.add(addrText);
        addrText.requestFocus();
        repaint();
    }

    @Override
    public void paintComponent(Graphics g) {
        painter.paintBackground((Graphics2D)g, new Rectangle(0, 0, MENU_W, MENU_H), new Color(12, 239, 249));
        g.drawImage(titleImage, 0, 30, this);

        for (int idx: menuMap.keySet()) {
            painter.paint((Graphics2D)g, menuMap.get(idx), new Rectangle(209, 230 + 50 * idx, 120, 40), (idx == selectedMenu));
        }

        if (slide) {
            painter.drawAddrInputBox((Graphics2D)g, new Rectangle(330, 230 + 50 * selectedMenu, slide_w, 40), "Input address and hit Enter");
            painter.drawAddrInputText((Graphics2D)g, new Rectangle(330, 230 + 50 * selectedMenu, slide_w, 40));
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case UP:
                selectedMenu = (selectedMenu == 0 || selectedMenu == 1)? menuMap.size(): selectedMenu - 1;
                repaint();
                break;
            case DOWN:
                selectedMenu = (selectedMenu == 0 || selectedMenu == menuMap.size())? 1: selectedMenu + 1;
                repaint();
                break;
            case ENTER:
            case SPACE:
                if (selectedMenu == 2) {
                    anim = new Thread(this);
                    anim.start();
                }
                else if (selectedMenu != 0) mainFrame.switchPanel(this, menuMap.get(selectedMenu), "");
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {}

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {}

    @Override
    public void mousePressed(MouseEvent e) {}

    @Override
    public void mouseReleased(MouseEvent e) {}

    @Override
    public void mouseClicked(MouseEvent e) {
        if (selectedMenu == 2) {
            anim = new Thread(this);
            anim.start();
        }
        //if (selectedMenu != 0) mainFrame.switchPanel(this, menuMap.get(selectedMenu));
    }

    @Override
    public void mouseDragged(MouseEvent e) {}

    @Override
    public void mouseMoved(MouseEvent e) {
        slide = false;
        this.remove(addrText);
        mainFrame.requestFocus();

        Point point = e.getPoint();
        int x = point.x, y = point.y;

        selectedMenu = 0;
        if (x > 209 && x < 330) {
            if (y > 304 && y < 343) selectedMenu = 1;
            else if (y > 354 && y < 394) selectedMenu = 2;
            else if (y > 404 && y < 454) selectedMenu = 3;
        }

        repaint();
    }
}
