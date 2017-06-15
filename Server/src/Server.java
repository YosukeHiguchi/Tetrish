package src;

import java.io.*;
import java.util.*;
import java.net.*;

import static constant.Const.*;
import gui.MainFrame;

public class Server {
    private ArrayList<GameSystem> game = new ArrayList<GameSystem>(MAXP);
    private ArrayList<Socket> socket = new ArrayList<Socket>(MAXP);

    public Server() {
        MainFrame mf = new MainFrame("Game Viewing", game);
        mf.setVisible(true);

        try {
            ServerSocket server = new ServerSocket(PORT);
            System.out.println("Server started: " + server);

            while (socket.size() < MAXP) {
                Socket s = server.accept();
                socket.add(s);
                game.add(new GameSystem());

                System.out.println("Accepted: " + s);
            }
        } catch(IOException e) {
            e.printStackTrace();
        }

        // initialize
        for (int i = 0; i < MAXP; i++) receiveAllData(i);//receiveData(i, socket.get(i));
        for (int i = 0; i < MAXP; i++) sendAllData(i);//sendData(0, i, game.get(i));

        // Start receiving and sending data
        for (int i = 0; i < MAXP; i++) {
            Thread t = new ServerListener(this, i, socket.get(i));
            t.start();
        }
    }

    public void receiveAllData(int id) {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.get(id).getInputStream()));

            if (in.readLine().equals("score")) {
                game.get(id).setScore(Integer.parseInt(in.readLine()));
            }
            if (in.readLine().equals("lineCnt")) {
                game.get(id).setLineCnt(Integer.parseInt(in.readLine()));
            }
            if (in.readLine().equals("grid")) {
                for (int i = 0; i < FIELD_H; i++) {
                    String str = in.readLine();
                    Scanner sc = new Scanner(str);
                    for (int j = 0; j < FIELD_W; j++) {
                        game.get(id).getField().setGrid(i, j, sc.nextInt());
                    }
                }
            }
            if (in.readLine().equals("hldBlk")) {
                game.get(id).setHldBlk(Integer.parseInt(in.readLine()));
            }
            if (in.readLine().equals("nextBlk")) {
                String str = in.readLine();
                Scanner sc = new Scanner(str.substring(1, str.length() - 1)).useDelimiter(", ");
                ArrayList<Integer> blkList = new ArrayList<Integer>();
                while (sc.hasNextInt()) blkList.add(sc.nextInt());
                game.get(id).setNextBlk(blkList);
            }

            sendAllData(id);
        } catch (Exception e) {
            //e.printStackTrace();
        }
    }

    public void sendAllData(int id) {
        for (int n = 0; n < MAXP; n++) {
            if (n == id) continue;

            try {
                PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.get(n).getOutputStream())), true);

                out.println("score");
                out.println(game.get(id).getScore());
                out.println("lineCnt");
                out.println(game.get(id).getLineCnt());
                out.println("grid");
                Field f = game.get(id).getField();
                for (int i = 0; i < FIELD_H; i++) {
                    for (int j = 0; j < FIELD_W; j++) {
                        out.print(f.getGrid(i, j) + " ");
                    }
                    out.println();
                }
                out.println("hldBlk");
                out.println(game.get(id).getHldBlk());
                out.println("nextBlk");
                out.println(game.get(id).getNextBlk());

            } catch(IOException e) {
                e.printStackTrace();
            }
        }
    }
}
