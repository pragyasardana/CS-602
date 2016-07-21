import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Enumeration;
import java.util.Vector;

public class WBHandler extends Thread {
	protected Socket s;
	protected ObjectInputStream i;
	protected ObjectOutputStream o;
	protected LineObj m;
	public static Vector<WBHandler> handlers = new Vector<WBHandler>();
	public WBHandler(Socket s) {
		// TODO Auto-generated constructor stub
		this.s = s;
		try {
			i = new ObjectInputStream(s.getInputStream());
			o = new ObjectOutputStream(s.getOutputStream());
		} catch (IOException e) {
		}
	}

	public void run() {
		try {
			handlers.addElement(this);
			while (true) {
				m = (LineObj) i.readObject();
				broadcast(m);
			}
		} catch (IOException ex) {
		} catch (ClassNotFoundException e) {
		} 
	}
	protected static void broadcast(LineObj obj) {
		LineObj TempObject = new LineObj();
		for(Line l:obj.retLines()){
			TempObject.addLine(new Line(l.getStartX(), l.getStartY(), l.getEndX(), l.getEndY(), l.getColor()));
		}
		synchronized (handlers) {
			
			for(WBHandler c:handlers) {
				try {
					synchronized (c.o) {
						c.o.writeObject(TempObject);
					}
					c.o.flush();
				} catch (IOException ex) {
					c = null;
				}
			}
		}
	}
	protected void finalize() throws Throwable {
		i.close();
		o.close();
		s.close();
		// TODO Auto-generated method stub
		super.finalize();
	}
}
