import java.net.ServerSocket;
import java.net.Socket;

public class ChatServer {
	public ChatServer(int port) {
		try {
			ServerSocket server = new ServerSocket(port);
			while (true) {
				Socket client = server.accept();
				ChatHandler c = new ChatHandler(client);
				c.start();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String args[]) {
		if (args.length != 1)
			new ChatServer(3000);
		else
			new ChatServer(Integer.parseInt(args[0]));
	}
}
