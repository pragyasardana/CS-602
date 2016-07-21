import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class Start extends JFrame implements ActionListener {
	protected JTextField n;
	protected JLabel l;
	protected JButton s;
	protected JPanel north;
	protected Color c;
	JColorChooser jcc;

	public Start() {
		setLayout(new BorderLayout());
		add("North", north = new JPanel());
		north.setLayout(new BorderLayout());
		north.add("West", l = new JLabel("Enter Your Display Name"));
		north.add("Center", n = new JTextField());
		add("Center", jcc = new JColorChooser());
		add("South", s = new JButton("Enter Group Chat"));
		s.addActionListener(this); /* c=JColorChooser.this; */
		setPreferredSize(new Dimension(500, 500));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		pack();
		setVisible(true);
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new Start();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub

		c = jcc.getColor();
		String name = n.getText();
		if (c != null && !name.isEmpty()) {
			dispose();
			new ChatClient(c, name);
		} else {
			JOptionPane.showMessageDialog(null, "Please Enter Your Display name and select your color", "ERROR",
					JOptionPane.ERROR_MESSAGE);

		}

	}

}
