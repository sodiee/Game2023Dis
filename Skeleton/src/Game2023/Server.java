package Game2023;

import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    public static void main(String[] args) throws Exception{
        ServerSocket welcomeSocket = new ServerSocket(6789);
        while(true) {
            Socket connectionSocket = welcomeSocket.accept();
            ServerThread st = new ServerThread(connectionSocket);

            /** --DET HUN SNAKKEDE OM PÅ TAVLEN--
            st.run();
            st.add();

             */
        }
    }
}
