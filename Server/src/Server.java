package src;

import java.io.*;
import java.util.*;
import java.net.*;

import static constant.Const.*;
import gui.MainFrame;

public class Server {
    private ArrayList<GameSystem> game = new ArrayList<GameSystem>(MAXP);

    public ArrayList<Socket> socket = new ArrayList<Socket>(MAXP);

    public Server() {
        // MainFrame mf = new MainFrame("Game Viewing");
        // mf.setVisible(true);

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
            Thread t = new ServerListener(this, i);
            t.start();
        }
    }

    public void receiveData(int id, Socket s) {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));

            // read moving block
            String str = in.readLine();
            game.get(id).field.movBlk = new Block(Integer.parseInt(str));

            // read next block list
            str = in.readLine();
            Scanner sc = new Scanner(str.substring(1, str.length() - 1)).useDelimiter(", ");
            ArrayList<Integer> blkList = new ArrayList<Integer>();
            while (sc.hasNextInt()) blkList.add(sc.nextInt());
            game.get(id).nextBlk = blkList;

            // read command
            str = in.readLine();
            int cmd = Integer.parseInt(str);

            sendData(cmd, id, game.get(id));
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    public void sendData(int command, int id, GameSystem game) {
        for (int i = 0; i < MAXP; i++) {
            if (i == id) continue;

            try {
                PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.get(i).getOutputStream())), true);

                out.println(game.field.movBlk.getId());
                out.println(game.nextBlk);
                out.println(command);
            } catch(IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void receiveAllData(int id) {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.get(id).getInputStream()));

            if (in.readLine().equals("score")) {
                game.get(id).score = Integer.parseInt(in.readLine());
            }
            if (in.readLine().equals("lineCnt")) {
                game.get(id).lineCnt = Integer.parseInt(in.readLine());
            }
            if (in.readLine().equals("grid")) {
                for (int i = 0; i < FIELD_H; i++) {
                    String str = in.readLine();
                    Scanner sc = new Scanner(str);
                    for (int j = 0; j < FIELD_W; j++) {
                        game.get(id).field.grid[i][j] = sc.nextInt();
                    }
                }
            }
            if (in.readLine().equals("hldBlk")) {
                game.get(id).hldBlk = Integer.parseInt(in.readLine());
            }
            if (in.readLine().equals("nextList")) {
                String str = in.readLine();
                Scanner sc = new Scanner(str.substring(1, str.length() - 1)).useDelimiter(", ");
                ArrayList<Integer> blkList = new ArrayList<Integer>();
                while (sc.hasNextInt()) blkList.add(sc.nextInt());
                game.get(id).nextBlk = blkList;
            }

            sendAllData(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendAllData(int id) {
        for (int n = 0; n < MAXP; n++) {
            if (n == id) continue;

            try {
                PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.get(n).getOutputStream())), true);

                out.println("score");
                out.println(game.get(id).score);
                out.println("lineCnt");
                out.println(game.get(id).lineCnt);
                out.println("grid");
                for (int i = 0; i < FIELD_H; i++) {
                    for (int j = 0; j < FIELD_W; j++) {
                        out.print(game.get(id).field.grid[i][j] + " ");
                    }
                    out.println();
                }
                out.println("hldBlk");
                out.println(game.get(id).hldBlk);
                out.println("nextBlk");
                out.println(game.get(id).nextBlk);

            } catch(IOException e) {
                e.printStackTrace();
            }
        }
    }
}
