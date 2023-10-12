package Game2023;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.text.*;

public class GUI extends Application {

    public static final int size = 20;
    public static final int scene_height = size * 20 + 100;
    public static final int scene_width = size * 20 + 200;

    public static Image image_floor;
    public static Image image_wall;
    public static Image hero_right, hero_left, hero_up, hero_down;

    public static Player me;
    public static List<Player> players = new ArrayList<Player>();

    private static Label[][] fields;
    private TextArea scoreList;

    private byte[] msgArr = new byte[2];

    Socket clientSocket;
    static BufferedReader inFromServer;
    DataOutputStream outToServer;


    private static String[] board = {    // 20x20
            "wwwwwwwwwwwwwwwwwwww",
            "w        ww        w",
            "w w  w  www w  w  ww",
            "w w  w   ww w  w  ww",
            "w  w               w",
            "w w w w w w w  w  ww",
            "w w     www w  w  ww",
            "w w     w w w  w  ww",
            "w   w w  w  w  w   w",
            "w     w  w  w  w   w",
            "w ww ww        w  ww",
            "w  w w    w    w  ww",
            "w        ww w  w  ww",
            "w         w w  w  ww",
            "w        w     w  ww",
            "w  w              ww",
            "w  w www  w w  ww ww",
            "w w      ww w     ww",
            "w   w   ww  w      w",
            "wwwwwwwwwwwwwwwwwwww"
    };


    // -------------------------------------------
    // | Maze: (0,0)              | Score: (1,0) |
    // |-----------------------------------------|
    // | boardGrid (0,1)          | scorelist    |
    // |                          | (1,1)        |
    // -------------------------------------------

    @Override
    public void start(Stage primaryStage) {
        try {
            GridPane grid = new GridPane();
            grid.setHgap(10);
            grid.setVgap(10);
            grid.setPadding(new Insets(0, 10, 0, 10));

            Text mazeLabel = new Text("Maze:");
            mazeLabel.setFont(Font.font("Arial", FontWeight.BOLD, 20));

            Text scoreLabel = new Text("Score:");
            scoreLabel.setFont(Font.font("Arial", FontWeight.BOLD, 20));

            scoreList = new TextArea();

            GridPane boardGrid = new GridPane();

            image_wall = new Image(getClass().getResourceAsStream("Image/wall4.png"), size, size, false, false);
            image_floor = new Image(getClass().getResourceAsStream("Image/floor1.png"), size, size, false, false);

            hero_right = new Image(getClass().getResourceAsStream("Image/heroRight.png"), size, size, false, false);
            hero_left = new Image(getClass().getResourceAsStream("Image/heroLeft.png"), size, size, false, false);
            hero_up = new Image(getClass().getResourceAsStream("Image/heroUp.png"), size, size, false, false);
            hero_down = new Image(getClass().getResourceAsStream("Image/heroDown.png"), size, size, false, false);

            fields = new Label[20][20];
            for (int j = 0; j < 20; j++) {
                for (int i = 0; i < 20; i++) {
                    switch (board[j].charAt(i)) {
                        case 'w':
                            fields[i][j] = new Label("", new ImageView(image_wall));
                            break;
                        case ' ':
                            fields[i][j] = new Label("", new ImageView(image_floor));
                            break;
                        default:
                            throw new Exception("Illegal field value: " + board[j].charAt(i));
                    }
                    boardGrid.add(fields[i][j], i, j);
                }
            }
            scoreList.setEditable(false);


            grid.add(mazeLabel, 0, 0);
            grid.add(scoreLabel, 1, 0);
            grid.add(boardGrid, 0, 1);
            grid.add(scoreList, 1, 1);

            Scene scene = new Scene(grid, scene_width, scene_height);
            primaryStage.setScene(scene);
            primaryStage.show();

            clientSocket = new Socket("10.10.139.182", 6789);
            ClientThread ct = new ClientThread(clientSocket);
            outToServer = new DataOutputStream(clientSocket.getOutputStream());
            inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            ct.start();

            scene.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
                switch (event.getCode()) {
                    case UP:
                        try {
                            outToServer.writeBytes("UP " + me.name + '\n');
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                        break;
                    case DOWN:
                        try {
                            outToServer.writeBytes("DOWN " + me.name + '\n');
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                        break;
                    case LEFT:
                        try {
                            outToServer.writeBytes("LEFT " + me.name + '\n');
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                        break;
                    case RIGHT:
                        try {
                            outToServer.writeBytes("RIGHT " + me.name + '\n');
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                        break;
                    default:
                        break;
                }
            });

            // Setting up standard players

            me = new Player("Mikkel", 14, 15, "up");
            players.add(me);
            fields[14][15].setGraphic(new ImageView(hero_up));

            Player harry = new Player("Lucas", 9, 4, "up");
            players.add(harry);
            fields[9][4].setGraphic(new ImageView(hero_up));

            scoreList.setText(getScoreList());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void playerMoved(int delta_x, int delta_y, String direction, Player player) {
        System.out.println("playerMoved called with direction: " + direction);

        player.direction = direction;
        int x = player.getXpos(), y = player.getYpos();

        if (board[y + delta_y].charAt(x + delta_x) == 'w') {
            player.addPoints(-1);
        } else {
            Player p = getPlayerAt(x + delta_x, y + delta_y);
            if (p != null) {
                player.addPoints(10);
                p.addPoints(-10);
            } else {
                player.addPoints(1);

                fields[x][y].setGraphic(new ImageView(image_floor));
                x += delta_x;
                y += delta_y;

                if (direction.equals("RIGHT")) {
                    System.out.println("Setting graphic to hero_right at x=" + x + ", y=" + y);
                    fields[x][y].setGraphic(new ImageView(hero_right));
                }
                ;
                if (direction.equals("LEFT")) {
                    System.out.println("Setting graphic to hero_left at x=" + x + ", y=" + y);
                    fields[x][y].setGraphic(new ImageView(hero_left));
                }
                ;
                if (direction.equals("UP")) {
                    System.out.println("Setting graphic to hero_up at x=" + x + ", y=" + y);
                    fields[x][y].setGraphic(new ImageView(hero_up));
                }
                ;
                if (direction.equals("DOWN")) {
                    System.out.println("Setting graphic to hero_down at x=" + x + ", y=" + y);
                    fields[x][y].setGraphic(new ImageView(hero_down));
                }
                ;

                player.setXpos(x);
                player.setYpos(y);
            }
        }
    }

    public String getScoreList() {
        StringBuffer b = new StringBuffer(100);
        for (Player p : players) {
            b.append(p + "\r\n");
        }
        return b.toString();
    }

    public static Player getPlayerAt(int x, int y) {
        for (Player p : players) {
            if (p.getXpos() == x && p.getYpos() == y) {
                return p;
            }
        }
        return null;
    }

    static class ClientThread extends Thread {
        Socket socket;

        ClientThread(Socket clientSocket) {
            this.socket = clientSocket;
        }

        public void run() {
            while (true) {
                try {
                    /*sentence = inFromServer.readLine();
                    outToServer.writeBytes(sentence + '\n');*/
                    String sentence = inFromServer.readLine();
                    String direction = sentence.split(" ")[0];

                    String name = sentence.split(" ")[1];

                    Player playerToBeMoved = null;
                    for (Player p : players) {
                        if (p.name.equals(name)) {
                            playerToBeMoved = p;
                        }
                    }

                    if (playerToBeMoved != null) {
                        Player playerToBeMovedFinal = playerToBeMoved;
                        Platform.runLater(() -> {
                            System.out.println(name);
                            System.out.println(direction);

                            if (direction.equals("UP")) {
                                playerMoved(0, -1, "UP", playerToBeMovedFinal);
                            }
                            if (direction.equals("DOWN")) {
                                playerMoved(0, +1, "DOWN", playerToBeMovedFinal);
                            }
                            if (direction.equals("LEFT")) {
                                playerMoved(-1, 0, "LEFT", playerToBeMovedFinal);
                            }
                            if (direction.equals("RIGHT")) {
                                playerMoved(+1, 0, "RIGHT", playerToBeMovedFinal);
                            }
                        });
/*
                                switch (direction) {
                                    case "UP":
                                        Player finalPlayerToBeMoved1 = playerToBeMoved;
                                        playerMoved(0, -1, direction, finalPlayerToBeMoved1);
                                        break;

                                    case "DOWN":
                                        Player finalPlayerToBeMoved = playerToBeMoved;
                                        playerMoved(0, +1, direction, finalPlayerToBeMoved);
                                        break;

                                    case "LEFT":
                                        Player finalPlayerToBeMoved2 = playerToBeMoved;
                                        playerMoved(-1, 0, direction, finalPlayerToBeMoved2);
                                        break;

                                    case "RIGHT":
                                        System.out.println("anden swithc case: " + direction);
                                        Player finalPlayerToBeMoved3 = playerToBeMoved;
                                        playerMoved(+1, 0, direction, finalPlayerToBeMoved3);
                                        break;

                                    default:
                                        break;
                                });
                    } */
                    }


                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }


        }
    }
}