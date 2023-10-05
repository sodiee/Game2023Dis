package Game2023;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server {
    static List<ServerThread> threads = new ArrayList<>();
    public static void main(String[] args) throws Exception {
        while (true) {
            ServerSocket welcomeSocket = new ServerSocket(6789);
            Socket connectionSocket = welcomeSocket.accept();
            ServerThread thread = new ServerThread(connectionSocket);
            thread.start();
            threads.add((thread));
        }
    }

    public void writeMethod(String s){
        for(ServerThread thread : threads){
            thread.writeBackToGUI(s);
        }
    }
}
