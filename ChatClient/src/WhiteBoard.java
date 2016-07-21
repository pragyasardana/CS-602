import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Panel;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

public class WhiteBoard extends Panel implements MouseListener, MouseMotionListener, Runnable {
	private int bufferWidth;
	private int bufferHeight;
	private Image bufferImage;
	private Graphics bufferGraphics;
	Color drawColor;
	int lastX = 0, lastY = 0;
	LineObj l;
	ArrayList<Line> drawObj;
	protected Socket s;
	protected ObjectInputStream i;
	protected ObjectOutputStream o;
	protected Thread listener;

	public WhiteBoard(Color c, int w, int h) {
		super();

		try {
			s=new Socket("afsaccess2.njit.edu", 3001);
			o = new ObjectOutputStream(this.s.getOutputStream());
			i = new ObjectInputStream(s.getInputStream());
		} catch (Exception e) {
		}

		drawObj = new ArrayList<Line>();
		l = new LineObj();
		setBackground(Color.WHITE);
		setPreferredSize(new Dimension(w, h));
		drawColor = c;
		setForeground(drawColor);
		addMouseListener(this);
		addMouseMotionListener(this);
		listener = new Thread(this);
		listener.start();
	}

	public void update(Graphics g) {
		paintBuffer(g);
	}

	public void paintBuffer(Graphics g) {
		// checks the buffersize with the current panelsize
		// or initialises the image with the first paint
		if (bufferWidth != getSize().width || bufferHeight != getSize().height || bufferImage == null
				|| bufferGraphics == null)
			resetBuffer();
		if (bufferGraphics != null) {
			// this clears the offscreen image, not the onscreen one
			bufferGraphics.clearRect(0, 0, bufferWidth, bufferHeight);

			// calls the paintbuffer method with
			// the offscreen graphics as a param
			paint(bufferGraphics);

			// we finaly paint the offscreen image onto the onscreen image
			g.drawImage(bufferImage, 0, 0, this);
		}

	}

	private void resetBuffer() {
		// always keep track of the image size
		bufferWidth = getSize().width;
		bufferHeight = getSize().height;

		// clean up the previous image
		if (bufferGraphics != null) {
			bufferGraphics.dispose();
			bufferGraphics = null;
		}
		if (bufferImage != null) {
			bufferImage.flush();
			bufferImage = null;
		}
		System.gc();

		// create the new image with the size of the panel
		bufferImage = createImage(bufferWidth, bufferHeight);
		bufferGraphics = bufferImage.getGraphics();
	}

	public void mouseExited(MouseEvent me) {
	}

	public void mouseClicked(MouseEvent me) {
	}

	public void mouseReleased(MouseEvent me) {
		try {
			o.writeObject(l);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void mouseEntered(MouseEvent me) {
		record(me.getX(), me.getY());
	}

	public void mousePressed(MouseEvent me) {
		record(me.getX(), me.getY());
		l = null;
		l = new LineObj();
	}

	public void mouseMoved(MouseEvent me) {
	}

	public void mouseDragged(MouseEvent me) {
		int x = me.getX();
		int y = me.getY();
		l.addLine(new Line(lastX, lastY, x, y, drawColor));
		drawObj.add(new Line(lastX, lastY, x, y, drawColor));
		record(x, y);
		repaint();
	}

	public void drawObj(LineObj in) {
		for (Line x : in.retLines()) {
			drawObj.add(new Line(x.getStartX(), x.getStartY(), x.getEndX(), x.getEndY(), x.getColor()));
		}
		repaint();
	}

	public void paint(Graphics g) {
		for (Line line : drawObj) {
			g.setColor(line.getColor());
			g.drawLine(line.getStartX(), line.getStartY(), line.getEndX(), line.getEndY());
		}
	}

	protected void record(int x, int y) {
		lastX = x;
		lastY = y;
	}

	public void run() {
		try {
			while (listener != null) {
				LineObj m = (LineObj) i.readObject();
				drawObj(m);
			}
		} catch (IOException ex) {
			ex.printStackTrace();
		} catch (ClassNotFoundException el) {
		} catch (Exception e) {

		}
	}

	public void close() throws Exception {
		i.close();
		o.close();
		s.close();
	}

	public void finalize() throws Throwable {
		o.close();
		i.close();
		s.close();
		super.finalize();
	}
}
