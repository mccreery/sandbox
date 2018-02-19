import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Point;
import java.awt.image.CropImageFilter;
import java.awt.image.FilteredImageSource;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;

@SuppressWarnings("serial")
public class SlidePuzzle extends JFrame {
	private PuzzleCell[][] cells = new PuzzleCell[3][3];
	private Point emptyCell = new Point(0, 0);

	private SlidePuzzle(Image image) {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(new GridLayout(cells[0].length, cells.length));
		setResizable(false);
		setTitle("Slide Puzzle");

		for(int y = 0; y < cells.length; y++) {
			for(int x = 0; x < cells[0].length; x++) {
				JPanel cell;
				if(x == 0 && y == 0) {
					cell = new JPanel();
				} else {
					cell = new PuzzleCell(image, cells, x, y);
				}
				add(cell);

				if(cell instanceof PuzzleCell) {
					cells[y][x] = (PuzzleCell)cell;
				}
			}
		}
		pack();
	}

	private static class PuzzleCell extends JPanel {
		private JPanel[][] cells;
		private int x, y;

		private Image image;

		public PuzzleCell(Image image, JPanel[][] cells, int x, int y) {
			this.cells = cells;
			this.x = x;
			this.y = y;

			this.image = getCellImage(image, x, y, cells[0].length, cells.length);
			setPreferredSize(new Dimension(this.image.getWidth(null), this.image.getHeight(null)));
			setMinimumSize(getPreferredSize());
			setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
		}

		private Image getCellImage(Image source, int gridX, int gridY, int gridWidth, int gridHeight) {
			int cellWidth = source.getWidth(null) / gridWidth;
			int cellHeight = source.getHeight(null) / gridHeight;

			int x = gridX * cellWidth;
			int y = gridY * cellHeight;

			CropImageFilter filter = new CropImageFilter(x, y, cellWidth, cellHeight);
			FilteredImageSource fis = new FilteredImageSource(source.getSource(), filter);
			Image cropped = getToolkit().createImage(fis);

			MediaTracker tracker = new MediaTracker(this);
			tracker.addImage(cropped, 0);

			try {
				tracker.waitForID(0);
			} catch(InterruptedException e) {
				e.printStackTrace();
			}
			return cropped;
		}

		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			g.drawImage(image, 0, 0, null);
		}
	}

	public static final void main(String[] args) {
		if(args.length == 0) {
			System.err.println("Must provide an image.");
			return;
		}

		Image image = null;
		try {
			image = ImageIO.read(new File(args[0]));
		} catch(IOException e) {
			System.err.println("Image not found: " + args[0]);
			return;
		}

		new SlidePuzzle(image).setVisible(true);
	}
}
