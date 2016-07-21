import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

public class ChatClient extends JFrame implements Runnable, ActionListener {
	protected JTextPane output, userList;
	protected JTextField input;
	protected Socket s;
	protected ObjectInputStream i;
	protected ObjectOutputStream o;
	protected ChatMessageEx m;
	protected Thread listener;
	protected boolean first = true, kill = false;
	protected String name = "";
	protected JButton send;
	protected JPanel flash, superman, greenArrow, batman, cyborg, aquamman;
	protected Color colour;
	protected JButton disconnect;
	protected JButton viewHistory;
	protected WhiteBoard w;
	protected JScrollPane pragya;
	protected JComboBox<String> toName;
	protected JTextPane list;
	protected JLabel title;

	public ChatClient(Color c, String name2) {
		super(name2);
		name = name2;
		colour = c;
		setLayout(new BorderLayout());
		//control = new CommandClient(name);
		add("North", superman = new JPanel());
		superman.setLayout(new FlowLayout());
		superman.add(viewHistory = new JButton("View History"));
		superman.add(disconnect = new JButton("Disconnect"));
		add("Center", greenArrow = new JPanel());
		greenArrow.setLayout(new BorderLayout());
		greenArrow.add("Center", pragya = new JScrollPane(output = new JTextPane()));
		output.setEditable(false);
		output.setBackground(getBackground());
		greenArrow.add("South", flash = new JPanel());
		flash.setLayout(new BorderLayout());
		flash.add("Center", input = new JTextField());
		flash.add("East", send = new JButton("Send"));
		String[] names = { "All" };
		flash.add("West", toName = new JComboBox<String>(names));
		add("East", w = new WhiteBoard(colour, 400, getHeight()));
		input.addActionListener(this);
		send.addActionListener(this);
		viewHistory.addActionListener(this);
		disconnect.addActionListener(this);
		add("West", cyborg = new JPanel());
		cyborg.setLayout(new BorderLayout());
		cyborg.add("Center", list = new JTextPane());
		list.setEditable(false);
		list.setBackground(getBackground());
		cyborg.add("North", aquamman = new JPanel());
		aquamman.add(title = new JLabel("User List"));
		m = new ChatMessageEx();
		try {
			s = new Socket("afsaccess2.njit.edu", 3000);
			o = new ObjectOutputStream(s.getOutputStream());
			i = new ObjectInputStream(s.getInputStream());
		} catch (Exception e) {
		}

		setPreferredSize(new Dimension(1000, 500));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		pack();
		setVisible(true);
		listener = new Thread(this);
		listener.start();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == input || e.getSource() == send) {
			try {
				m.setName(this.name);
				m.setMessage(input.getText());
				m.setColour(this.colour);
				m.setToName((String) toName.getSelectedItem());
				o.writeObject(m);
			} catch (IOException ex) {
				ex.printStackTrace();
				kill = true;
			}
			input.setText("");
			return;
		}
		if (e.getSource() == disconnect) {
			kill = true;
			try {
				w.close();
				i.close();
				o.close();
				s.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			listener = null;
			setVisible(false);
			dispose();
		}
		if (e.getSource() == viewHistory) {
			Control temp = new Control();
			temp.setCommand("get_hist");
			try {
				o.writeObject(temp);
				o.flush();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		return;
	}

	@Override
	public void run() {
		if (kill) {
			return;
		}
		if (first) {
			try {
				m.setName(this.name);
				m.setMessage("HAS ENTERED");
				m.setColour(this.colour);
				m.setToName("All");
				o.writeObject(m);
			} catch (IOException e) {
				e.printStackTrace();
				kill = true;
			}
			first = false;
		}
		try {
			while (true) {
				Object obj = i.readObject();
				if (obj instanceof ChatMessageEx) {
					m = (ChatMessageEx) obj;
					String line = m.getName() + ": " + m.getMessage() + "\n";
					StyledDocument a = output.getStyledDocument();
					SimpleAttributeSet attributeSet = new SimpleAttributeSet();
					StyleConstants.setForeground(attributeSet, m.getColor());
					try {
						if (!m.getToName().equals("All")) {
							String tempLine = "*****Private Message Start*****\n";
							a.insertString(a.getLength(), tempLine, attributeSet);
						}
						a.insertString(a.getLength(), line, attributeSet);
						if (!m.getToName().equals("All")) {
							String tempLine = "*****Private Message End*****\n";
							a.insertString(a.getLength(), tempLine, attributeSet);
						}
					} catch (BadLocationException e) {
						e.printStackTrace();
					}
					pragya.getVerticalScrollBar().setValue(pragya.getVerticalScrollBar().getMaximum());
					String listName = "";
					toName.removeAllItems();
					toName.addItem("All");
					for (String s : m.getUserList()) {
						listName += s + "\n";
						toName.addItem(s);
					}
					list.setText(listName);
				} else if (obj instanceof Control) {
					Control temp=(Control)obj;
					if (temp.getCommand().equals("disp_hist")) {
						String data = temp.getData();
						new HistoryClient(data);
					} 
				}
			}
		} catch (IOException ex) {
			ex.printStackTrace();
		} catch (ClassNotFoundException el) {
		} catch (Exception e) {

		} finally {
			input.setVisible(false);
			validate();
			try {
				o.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}

	public void finalize() throws Throwable {
		w.close();
		o.close();
		i.close();
		s.close();
		super.finalize();
	}
}
