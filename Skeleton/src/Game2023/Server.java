package Game2023;

import java.io.DataOutputStream;
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

    public static void updatePlayerPosition(ServerThread playerThread, String direction) {
        // Find den pågældende spiller baseret på playerThread eller anden identifikation.
        /*
        Player playerToUpdate = få fat i den spiller som skal opdatere

        int x-koordinat = playerToUpdate.getXkoor
        int y-koor = playerToUpdate.getY

        if (direction.equals("UP")) {
            y-koor--;
        } else if (direction.equals("DOWN")) {
            y-koor++;
        } else if (direction.equals("LEFT")) {
            x-koordinat--;
        } else if (direction.equals("RIGHT")) {
           x-koordinat++;
        }


!!skal opdatere selve gui!!
        Platform.runLater(() -> {
            fields[playerToUpdate.getXpos()][playerToUpdate.getYpos()].setGraphic(new ImageView(image_floor));
            fields[newX][newY].setGraphic(new ImageView(getPlayerImage(direction)));
            playerToUpdate.setXpos(y-koor);
            playerToUpdate.setYpos(x-koordinat);
        });

         */

    }
}
