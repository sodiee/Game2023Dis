import java.net.*;
import java.util.ArrayList;
import java.util.List;

public class Server {
	/**
	 * @param args
	 */
	public static void main(String[] args)throws Exception {
		List<ServerThread> tråde = new ArrayList<>();
		ServerSocket welcomeSocket = new ServerSocket(6789);

		while (true) {
			Socket connectionSocket = welcomeSocket.accept();
			ServerThread thread = new ServerThread(connectionSocket);
			thread.start();
			tråde.add((thread));

			}
		}
	}


}
