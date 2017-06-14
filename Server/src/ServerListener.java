package src;

public class ServerListener extends Thread {
    private int id;
    private Server server;

    public ServerListener(Server server, int id) {
        this.server = server;
        this.id = id;
    }

    public void run() {
        while (true) {
            System.out.println("receiving: " + id);
            server.receiveAllData(id);

            try{
                Thread.sleep(10);
            }catch (InterruptedException e){
                e.printStackTrace();
            }
        }
    }
}
