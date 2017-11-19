package wps.swing;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;
import javax.swing.JSpinner.NumberEditor;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.EtchedBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import wps.Resolution;
import wps.SearchPerformer;
import wps.Util;

public class WallpaperFinder extends JFrame implements ActionListener, ChangeListener {
	private static final long serialVersionUID = -733763585162998228L;

	public final JRadioButton onlyPNG = new JRadioButton("Only PNG"),
			onlyJPG = new JRadioButton("Only JPEG"),
			either = new JRadioButton("Any image type");

	public final JTextField searchTerms = new JTextField("wallpaper");
	public final JSpinner width = new JSpinner(), height = new JSpinner();
	private final JButton reset;
	private final JComboBox<Resolution> presets = new JComboBox<Resolution>();

	private final JRadioButton[] rows = new JRadioButton[3], cols = new JRadioButton[3];
	private final TogglePanel[][] indicators = new TogglePanel[rows.length][cols.length];
	private Point grid;

	public WallpaperFinder() {
		super("Wallpaper Finder");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.initIcons();

		this.setMinimumSize(new Dimension(360, 360));
		this.setSize(this.getMinimumSize());

		this.setLocationRelativeTo(null);
		this.getRootPane().setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

		final JPanel gridSelector = new JPanel();
		gridSelector.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED), "Monitor Setup"));

		final GridLayout grid = new GridLayout(this.rows.length + 1, this.cols.length + 1);
		grid.setHgap(5);
		grid.setVgap(5);
		gridSelector.setLayout(grid);
		gridSelector.add(new JPanel());

		ButtonGroup rowGroup = new ButtonGroup(), colGroup = new ButtonGroup();

		for(int col = 0; col < cols.length; col++) {
			cols[col] = new JRadioButton();

			cols[col].setHorizontalAlignment(SwingConstants.CENTER);
			cols[col].setVerticalAlignment(SwingConstants.CENTER);
			cols[col].addActionListener(this);

			colGroup.add(cols[col]);
			gridSelector.add(cols[col]);
		}

		for(int row = 0; row < rows.length; row++) {
			rows[row] = new JRadioButton();
			rowGroup.add(rows[row]);
			gridSelector.add(rows[row]);

			rows[row].setHorizontalAlignment(SwingConstants.CENTER);
			rows[row].setVerticalAlignment(SwingConstants.CENTER);
			rows[row].addActionListener(this);

			for(int col = 0; col < cols.length; col++) {
				indicators[row][col] = new TogglePanel(getBackground().brighter(), getBackground().darker());
				gridSelector.add(indicators[row][col]);
			}
		}

		this.add(gridSelector, BorderLayout.CENTER);

		final JPanel topPanel = new JPanel();
		final ButtonGroup imageType = new ButtonGroup();
		for(JRadioButton radio : new JRadioButton[] {this.onlyJPG, this.onlyPNG, this.either}) {
			imageType.add(radio);
			topPanel.add(radio);
		}
		this.either.setSelected(true);
		this.add(topPanel, BorderLayout.NORTH);

		final JPanel bottomPanel = new JPanel();
		bottomPanel.setPreferredSize(new Dimension(1, 84));

		this.searchTerms.setPreferredSize(new Dimension(240, this.searchTerms.getPreferredSize().height));

		this.width.setModel(new SpinnerNumberModel(Util.MONITOR.getWidth(), 0, Integer.MAX_VALUE, 1));
		this.width.setEditor(new NumberEditor(this.width, "#"));
		this.width.addChangeListener(this);

		this.height.setModel(new SpinnerNumberModel(Util.MONITOR.getHeight(), 0, Integer.MAX_VALUE, 1));
		this.height.setEditor(new NumberEditor(this.height, "#"));
		this.height.addChangeListener(this);

		for(Resolution item : Util.RESOLUTIONS) {
			presets.addItem(item);
		}
		presets.addActionListener(this);

		bottomPanel.add(new JLabel("Search terms:"));
		bottomPanel.add(this.searchTerms);
		bottomPanel.add(new JLabel("Resolution:"));
		bottomPanel.add(this.width);
		bottomPanel.add(new JLabel("x"));
		bottomPanel.add(this.height);
		bottomPanel.add(this.presets);

		reset = new JButton("Reset");
		reset.addActionListener(this);
		bottomPanel.add(reset);

		JButton search = new JButton("Search");
		search.addActionListener(new SearchPerformer(this));
		search.setFont(search.getFont().deriveFont(Font.BOLD));
		bottomPanel.add(search);

		this.add(bottomPanel, BorderLayout.SOUTH);
		this.loadLayout(Util.getDisplayLayout());
	}

	public int getWidth() {
		return grid.x * (int)width.getValue();
	}
	public int getHeight() {
		return grid.y * (int)height.getValue();
	}

	private void initIcons() {
		final List<Image> icons = new ArrayList<Image>();
		for(String res : new String[] {"16", "20", "32", "40"}) {
			icons.add(new ImageIcon(this.getClass().getResource("/img/icon_" + res + ".png")).getImage());
		}
		this.setIconImages(icons);
	}

	private void loadLayout(Point grid) {
		this.grid = grid;
		cols[grid.x - 1].setSelected(true);
		rows[grid.y - 1].setSelected(true);

		for(int row = 0; row < rows.length; row++) {
			for(int col = 0; col < cols.length; col++) {
				indicators[row][col].setEnabled(row < grid.y && col < grid.x);
			}
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == reset) {
			width.setValue(Util.MONITOR.getWidth());
			height.setValue(Util.MONITOR.getHeight());
		} else if(e.getSource() == presets) {
			Resolution res = (Resolution)presets.getSelectedItem();

			if(res != null) {
				width.setValue(res.x);
				height.setValue(res.y);
			}
		} else if(e.getSource() instanceof JRadioButton) {
			for(int i = 0; i < rows.length && i < cols.length; i++) {
				if(i < rows.length && rows[i] == e.getSource()) {
					loadLayout(new Point(grid.x, i+1));
					break;
				} else if(i < cols.length && cols[i] == e.getSource()) {
					loadLayout(new Point(i+1, grid.y));
					break;
				}
			}
		}
	}

	@Override
	public void stateChanged(ChangeEvent e) {
		if(e.getSource() == width || e.getSource() == height) {
			presets.setSelectedIndex(((DefaultComboBoxModel<Resolution>) presets.getModel()).getIndexOf(
				new Resolution((int) width.getValue(), (int) height.getValue())));
		}
	}

	public static final void main(String[] args) throws ClassNotFoundException,
			InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException {
		UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		new WallpaperFinder().setVisible(true);
	}
}
