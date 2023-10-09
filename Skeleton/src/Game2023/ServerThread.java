package Game2023;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class ServerThread extends Thread {

    Socket connsocket;

    DataOutputStream outToClientInstans;

    public ServerThread(Socket connSocket) {
        this.connsocket = connSocket;
    }


    public void run() {
        try {
            BufferedReader inFromClient = new BufferedReader(new InputStreamReader(connsocket.getInputStream()));
            DataOutputStream outToClient = new DataOutputStream(connsocket.getOutputStream());
            this.outToClientInstans = outToClient;

            while(true) {
                String sentence = inFromClient.readLine();
                System.out.println("sentence i ST: " + sentence);
                Server.writeMethod(sentence);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void writeBackToGUI(String s) throws IOException {
        outToClientInstans.writeBytes(s + "\n");
    }
}
