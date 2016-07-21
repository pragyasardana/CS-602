import java.net.ServerSocket;
import java.net.Socket;

public class WBServer {
	public WBServer(int port) {
		try {
			ServerSocket server = new ServerSocket(port);
			while (true) {
				Socket client = server.accept();
				WBHandler c = new WBHandler(client);
				c.start();
			}
		} catch (Exception e) {
		}
	}

	public static void main(String args[]) {
		if (args.length != 1)
			new WBServer(3001);
		else
			new WBServer(Integer.parseInt(args[0]));
	}
}
