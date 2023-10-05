package Game2023;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class ServerThread extends Thread {

    Socket connsocket;

    Server server;

    DataOutputStream outToClientInstans;

    public ServerThread(Socket connSocket) {
        this.connsocket = connSocket;
    }

    public void run() {
        try {
            server = new Server();
            BufferedReader inFromClient = new BufferedReader(new InputStreamReader(connsocket.getInputStream()));
            DataOutputStream outToClient = new DataOutputStream(connsocket.getOutputStream());
            outToClientInstans = outToClient;

            while(true) {
                String sentence = inFromClient.readLine();
                server.writeMethod(sentence);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void writeBackToGUI(String s) throws IOException {
        outToClientInstans.writeBytes(s);
    }


}
