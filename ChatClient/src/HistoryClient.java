import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;

public class HistoryClient extends JFrame implements ActionListener {
	protected JPanel p, q;
	protected JTextPane output;
	protected JButton ok;
	protected JScrollPane scroll;

	public HistoryClient(String data) {
		super("History");
		setLayout(new BorderLayout());
		add("Center", p = new JPanel());
		p.setLayout(new GridLayout(1, 0));
		p.setBackground(getBackground());
		p.add(scroll = new JScrollPane(output = new JTextPane()));
		output.setBackground(getBackground());
		output.setEditable(false);
		add("South", q = new JPanel());
		q.setLayout(new FlowLayout());
		q.add(ok = new JButton("Done"));
		ok.addActionListener(this);
		output.setText(data);
		scroll.getVerticalScrollBar().setValue(scroll.getVerticalScrollBar().getMaximum());
		setPreferredSize(new Dimension(500, 500));
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		pack();
		setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if (e.getSource() == ok) {
			setVisible(false);
			dispose();
		}

	}

}
