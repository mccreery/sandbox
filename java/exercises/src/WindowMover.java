import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class WindowMover extends JFrame {
	private final JLabel coordinates = new JLabel("None");

	private Point dragOffset = null;

	private final MouseAdapter adapter = new MouseAdapter() {
		@Override
		public void mouseMoved(MouseEvent e) {
			coordinates.setText(e.getX() + ", " + e.getY());
		}

		@Override
		public void mousePressed(MouseEvent e) {
			if(e.getSource() instanceof Component) {
				Component target = (Component)e.getSource();
				dragOffset = e.getLocationOnScreen();
				dragOffset.translate(-target.getX(), -target.getY());
			}
		}

		@Override public void mouseReleased(MouseEvent e) {dragOffset = null;}

		@Override
		public void mouseDragged(MouseEvent e) {
			if(dragOffset != null && e.getSource() instanceof Component) {
				Component target = (Component)e.getSource();
				Point position = e.getLocationOnScreen();
				position.translate(-dragOffset.x, -dragOffset.y);

				target.setLocation(position);
			}
		}
	};

	private static final int WIDTH = 500, HEIGHT = 500;

	private WindowMover() {
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		Random random = new Random();

		JPanel upper = new JPanel();
		upper.setPreferredSize(new Dimension(WIDTH, HEIGHT));
		upper.setLayout(null);

		for(int i = 0; i < 10; i++) {
			JPanel panel = new JPanel();

			int x = random.nextInt(WIDTH - 20);
			int y = random.nextInt(HEIGHT - 20);
			int width = Math.min(20 + random.nextInt(90), WIDTH - x);
			int height = Math.min(20 + random.nextInt(90), HEIGHT - y);

			panel.setBounds(x, y, width, height);
			panel.setBackground(new Color(random.nextInt(0xffffff)));

			panel.addMouseListener(adapter);
			panel.addMouseMotionListener(adapter);

			upper.add(panel);
		}

		add(upper, BorderLayout.CENTER);
		add(coordinates, BorderLayout.SOUTH);
		pack();
	}

	public static final void main(String[] args) {
		new WindowMover().setVisible(true);
	}
}
