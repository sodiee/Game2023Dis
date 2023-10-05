package Game2023;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server {
    static List<ServerThread> threads = new ArrayList<>();
    public static void main(String[] args) throws Exception {
        ServerSocket welcomeSocket = new ServerSocket(6789);
        while (true) {
            Socket connectionSocket = welcomeSocket.accept();
            ServerThread thread = new ServerThread(connectionSocket);
            thread.start();
            threads.add((thread));
        }
    }

    public static void writeMethod(String s) throws IOException {
        for(ServerThread thread : threads){
            thread.writeBackToGUI(s);
        }
    }
}
