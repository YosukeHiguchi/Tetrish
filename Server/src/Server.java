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
        for (int i = 0; i < MAXP; i++) {
            receiveData(i, socket.get(i));
        }
        for (int i = 0; i < MAXP; i++) {
            sendData(0, i, game.get(i));
        }

        for (int i = 0; i < MAXP; i++) {
            Thread t = new ServerThread(this, i);
            t.start();
        }

    }

    public void receiveData(int id, Socket s) {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));

            String str = in.readLine();
            game.get(id).field.movBlk = new Block(Integer.parseInt(str));

            str = in.readLine();
            Scanner sc = new Scanner(str.substring(1, str.length() - 1)).useDelimiter(", ");
            ArrayList<Integer> blkList = new ArrayList<Integer>();
            while (sc.hasNextInt()) {
                blkList.add(sc.nextInt());
            }
            game.get(id).nextBlk = blkList;

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
}
