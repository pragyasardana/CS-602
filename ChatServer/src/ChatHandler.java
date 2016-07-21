import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Vector;

public class ChatHandler extends Thread {
	protected Socket s;
	protected ObjectInputStream i;
	protected ObjectOutputStream o;
	protected ChatMessageEx m;
	public static Vector<ChatHandler> handlers = new Vector<ChatHandler>();
	protected BufferedWriter output;
	String name;

	public ChatHandler(Socket s) {
		this.s = s;
		try {
			i = new ObjectInputStream(s.getInputStream());
			o = new ObjectOutputStream(s.getOutputStream());
			output = new BufferedWriter(new FileWriter("History.txt", true));
		} catch (IOException e) {
		}
	}

	public void run() {
		try {
			handlers.addElement(this);
			while (true) {
				Object obj = i.readObject();
				if (obj instanceof ChatMessageEx) {
					m = (ChatMessageEx) obj;
					String line = m.getName() + ": " + m.getMessage() + "\n";
					if (m.getMessage().equals("HAS ENTERED")) {
						name = m.getName();
					}
					if (m.toName.equals("All")) {
						output.write(line);
						output.flush();
					}
					broadcast(m);
				} else if (obj instanceof Control) {
					Control cmd=(Control)obj;
					Control tx = new Control();
					if (cmd.getCommand().equals("get_hist")) {
						BufferedReader br = new BufferedReader(new FileReader("History.txt"));
						String temp;
						String temp2 = "";
						while ((temp = br.readLine()) != null) {
							temp2 += temp + "\n";
						}
						br.close();

						tx.setCommand("disp_hist");
						tx.setData(temp2);
						o.writeObject(tx);
						o.flush();
					}
				}
			}
		} catch (IOException ex) {
		} catch (ClassNotFoundException e) {
		} finally {
			handlers.removeElement(this);
			m.setMessage("HAS LEFT");
			m.setToName("All");
			String line = m.getName() + ": " + m.getMessage() + "\n";
			try {
				output.write(line);
				output.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
			broadcast(m);
			try {
				s.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}

	protected static void broadcast(ChatMessageEx obj) {
		ChatMessageEx TempObject = obj.copy();
		synchronized (handlers) {
			ArrayList<String> names = new ArrayList<String>();
			for (ChatHandler c : handlers) {
				names.add(c.name);
			}
			TempObject.setUserList(names);
			for (ChatHandler c : handlers) {
				try {
					if (TempObject.getToName().equals("All") || TempObject.getToName().equals(c.name)
							|| TempObject.getName().equals(c.name)) {
						synchronized (c.o) {
							c.o.writeObject(TempObject);
							c.o.flush();
						}
					}
				} catch (IOException ex) {
					c = null;
				}
			}
		}
	}

	protected void finalize() throws Throwable {
		i.close();
		o.close();
		output.close();
		super.finalize();
	}
}