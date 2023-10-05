import java.net.*;
import java.io.*;
public class ServerThread extends Thread {
	Socket connSocket;

	public ServerThread(Socket connSocket) {
		this.connSocket = connSocket;
	}

	public void run() {
		try {
			BufferedReader inFromClient = new BufferedReader(new InputStreamReader(connSocket.getInputStream()));
			DataOutputStream outToClient = new DataOutputStream(connSocket.getOutputStream());

			// Do the work and the communication with the client here


			// The following two lines are only an example
			String clientSentence = inFromClient.readLine();
			String[] s = clientSentence.split(" ");
			byte[] bbb = read(s[1]);


			} catch(IOException e){
				e.printStackTrace();
			}
			// do the work here

		}




		public byte[] read (String aInputFileName) throws FileNotFoundException {
			System.out.println("Reading in binary file named : " + aInputFileName);
			File file = new File(aInputFileName);
			System.out.println("File size: " + file.length());
			byte[] result = new byte[(int) file.length()];
			try {
				InputStream input = null;
				try {
					int totalBytesRead = 0;
					input = new BufferedInputStream(new FileInputStream(file));
					while (totalBytesRead < result.length) {
						int bytesRemaining = result.length - totalBytesRead;
						int bytesRead = input.read(result, totalBytesRead, bytesRemaining);
// input.read() returns -1, 0, or more :
						if (bytesRead > 0) {
							totalBytesRead = totalBytesRead + bytesRead;
						}
					}
					System.out.println("Num bytes read: " + totalBytesRead);
				} finally {
					System.out.println("Closing input stream.");
					input.close();
				}
			} catch (FileNotFoundException ex) {
				throw ex;
			} catch (IOException ex) {
				ex.printStackTrace();
			}
			return result;
		}

	}


