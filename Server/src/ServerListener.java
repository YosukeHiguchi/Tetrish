package src;

import java.net.*;

public class ServerListener extends Thread {
    private int id;
    private Server server;
    private Socket socket;

    public ServerListener(Server server, int id, Socket socket) {
        this.server = server;
        this.id = id;
        this.socket = socket;
    }

    public void run() {
        while (true) {
            if (socket.isClosed()) break;
            server.receiveAllData(id);

            try{
                Thread.sleep(10);
            }catch (InterruptedException e){
                e.printStackTrace();
            }
        }
    }
}
