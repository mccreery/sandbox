import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.border.BevelBorder;

@SuppressWarnings("serial")
public class DieRoll extends JPanel {
	private static final Random RANDOM = new Random();

	private int value;

	private final Timer timer;

	private final ActionListener turner = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			int adjacent;
			do {
				adjacent = (value + RANDOM.nextInt(5)) % 6 + 1;
			} while(adjacent == 7 - value);

			updateVal(adjacent);

			if(--turnsRemaining == 0) {
				timer.stop();
			}
		}
	};

	private int turnsRemaining = 0;

	public DieRoll() {
		setPreferredSize(new Dimension(300, 300));
		timer = new Timer(200, turner);
		timer.setInitialDelay(0);

		randomize();
	}

	public void updateVal(int value) {
		this.value = value;
		repaint();
	}

	public void randomize() {
		updateVal(RANDOM.nextInt(6) + 1);
	}

	public void roll() {
		turnsRemaining = 10;
		timer.start();
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		Rectangle bounds = getBounds();
		Insets insets = getInsets();
		bounds.setBounds(
			bounds.x + insets.left,
			bounds.y + insets.top,
			bounds.width - insets.left - insets.right,
			bounds.height - insets.top - insets.bottom);

		int delta = Math.min(bounds.width, bounds.height) / 3;
		final int radius = delta / 3;

		Point dot = new Point(bounds.x + bounds.width / 2 - delta, bounds.y + bounds.height / 2 - delta);

		for(int y = 0; y < 3; y++) {
			for(int x = 0; x < 3; x++) {
				if(hasDot(x, y)) {
					g.fillOval(dot.x - radius, dot.y - radius, radius*2, radius*2);
				}
				dot.translate(delta, 0);
			}
			dot.translate(delta * -3, delta);
		}
	}

	/** @return {@code true} if the current value should display the given dot */
	private boolean hasDot(int x, int y) {
		int i = y * 3 + x;

		switch(i) {
			case 0:
			case 8:
				return value >= 4;
			case 1:
			case 7:
			default:
				return false;
			case 2:
			case 6:
				return value >= 2;
			case 3:
			case 5:
				return value == 6;
			case 4:
				return (value & 1) == 1;
		}
	}

	public static void main(String[] args) {
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setTitle("Die Roll");

		DieRoll die = new DieRoll();
		die.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10), BorderFactory.createBevelBorder(BevelBorder.RAISED)));
		frame.add(die);
	
		JButton button = new JButton("Roll");
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				die.roll();
			}
		});
		frame.add(button, BorderLayout.SOUTH);
	
		frame.pack();
		frame.setVisible(true);
	}
}
