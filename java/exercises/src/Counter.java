import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.border.BevelBorder;

@SuppressWarnings("serial")
public class Counter extends JFrame {
	private int count = 0;

	private final JButton increment = new JButton("Increment");
	private final JButton reset     = new JButton("Reset");
	private final JLabel  output    = new JLabel();

	private void setCounter(int count) {
		this.count = count;
		output.setText(String.valueOf(count));
	}

	private Counter() {
		setTitle("Counter");
		setLayout(new BoxLayout(getContentPane(), BoxLayout.LINE_AXIS));
		getRootPane().setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

		increment.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setCounter(count + 1);
			}
		});
		reset.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setCounter(0);
			}
		});

		output.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED), BorderFactory.createEmptyBorder(0, 10, 0, 10)));
		output.setMinimumSize(new Dimension(100, 0));
		
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		add(increment);
		add(reset, 1);
		add(output, 2);
		setCounter(0);
		pack();
	}

	public static void main(String[] args) {
		new Counter().setVisible(true);
	}
}
