package src;

public class ServerThread extends Thread {
    private int id;
    private Server server;

    public ServerThread(Server server, int id) {
        this.server = server;
        this.id = id;
    }

    public void run() {
        while (true) {
            server.receiveData(id, server.socket.get(id));

            try{
                Thread.sleep(10);
            }catch (InterruptedException e){
                e.printStackTrace();
            }
        }
    }
}
