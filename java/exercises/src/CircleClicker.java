import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class CircleClicker extends JFrame {
	private static final Random RANDOM = new Random();

	private final List<ColoredShape> shapes = new ArrayList<ColoredShape>();
	private final JPanel canvas = new JPanel() {
		@Override
		protected void paintComponent(Graphics g) {
			for(ColoredShape coloredShape : shapes) {
				coloredShape.draw(g);
			}
		}
	};

	private CircleClicker() {
		setTitle("Shapes");
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		shapes.add(new ColoredShape(new Ellipse2D.Float(10, 10, 100, 100), Color.RED));
		shapes.add(new ColoredShape(new Ellipse2D.Float(120, 10, 100, 100), Color.BLUE));
		shapes.add(new ColoredShape(new Rectangle(10, 120, 100, 100), Color.GREEN));
		shapes.add(new ColoredShape(new Rectangle(120, 120, 100, 100), Color.ORANGE));

		canvas.setPreferredSize(new Dimension(230, 230));
		canvas.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				for(ColoredShape coloredShape : shapes) {
					if(coloredShape.shape.contains(e.getPoint())) {
						coloredShape.color = new Color(RANDOM.nextInt());
						repaint();
					}
				}
			}
		});
		add(canvas);

		pack();
	}

	static class ColoredShape {
		private final Shape shape;
		public Color color;

		ColoredShape(Shape shape, Color color) {
			this.shape = shape;
			this.color = color;
		}

		void draw(Graphics g) {
			g.setColor(color);
			((Graphics2D)g).fill(shape);
		}
	}

	public static void main(String[] args) {
		new CircleClicker().setVisible(true);
	}
}
