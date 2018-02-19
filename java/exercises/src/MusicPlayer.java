import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JToggleButton;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

@SuppressWarnings("serial")
public class MusicPlayer extends JFrame {
	private static final Dimension BUTTON_SIZE = new Dimension(32, 32);

	private static final Icon PLAY_ICON  = new ImageIcon(MusicPlayer.class.getClassLoader().getResource("play.png"));
	private static final Icon PAUSE_ICON = new ImageIcon(MusicPlayer.class.getClassLoader().getResource("pause.png"));

	private JLabel albumArt = new JLabel() {
		@Override
		public void paint(Graphics g) {
			try {
				Image image = ImageIO.read(getClass().getResource("album.jpg"));
				int size = Math.min(getWidth(), getHeight());
				
				image = image.getScaledInstance(size, size, Image.SCALE_SMOOTH);
				g.drawImage(image,
					getX() + (getWidth() - size) / 2,
					getY() + (getHeight() - size) / 2,
					null);
			} catch(IOException e) {
				e.printStackTrace();
			}
		}
	};

	private final JToggleButton playPause = new JToggleButton() {
		@Override
		public Icon getIcon() {
			return isSelected() ? PAUSE_ICON : PLAY_ICON;
		}
	};
	private final JSlider scrubber = new JSlider(0, 100);
	private final JLabel time = new JLabel() {
		@Override
		public String getText() {
			int remaining = length - currentTime;
			return String.format("%d:%02d/-%d:%02d",
				currentTime / 60, currentTime % 60, remaining / 60, remaining % 60);
		}
	};

	private void setCurrentTime(int current) {
		currentTime = current;
		playPause.setSelected(false);
		time.invalidate();
	}

	private void setLength(int length) {
		this.length = length;
		scrubber.setMaximum(length);
		scrubber.invalidate();
		playPause.setSelected(false);
		time.invalidate();
	}

	private int currentTime;
	private int length;

	public MusicPlayer() {
		JMenuBar menu = new JMenuBar();
		JMenu file = new JMenu("File");
		file.add(new JMenuItem("Open"));
		menu.add(file);
		setJMenuBar(menu);

		add(albumArt);

		add(getControls(), BorderLayout.SOUTH);
		setLength(180);
		setCurrentTime(0);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		pack();

		setSize(300, 300);
	}

	private JPanel getControls() {
		JPanel controls = new JPanel();
		controls.setLayout(new BoxLayout(controls, BoxLayout.X_AXIS));

		playPause.setPreferredSize(BUTTON_SIZE);
		playPause.setMaximumSize(BUTTON_SIZE);

		scrubber.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				setCurrentTime(scrubber.getValue());
			}
		});

		controls.add(playPause);
		controls.add(scrubber);
		controls.add(time);
		return controls;
	}

	public static void main(String[] args) {
		new MusicPlayer().setVisible(true);
	}
}
