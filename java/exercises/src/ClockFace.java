import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.AffineTransform;
import java.util.Calendar;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.Timer;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

@SuppressWarnings("serial")
public class ClockFace extends JPanel {
	private int seconds;

	public ClockFace() {
		setPreferredSize(new Dimension(300, 300));
	}

	public void setTime(int hour, int minute, int second) {
		seconds = ((hour % 12) * 3600 + (minute % 60) * 60 + (second % 60));
		repaint();
	}

	public void tick() {
		seconds = (seconds + 1) % 43200;
		repaint();
	}

	private void drawHand(Graphics g, double theta, int radius) {
		AffineTransform state = ((Graphics2D)g).getTransform();

		((Graphics2D)g).rotate(theta);
		g.drawLine(0, 0, 0, -radius);

		((Graphics2D)g).setTransform(state);
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		int radius = getWidth() / 2;

		g.drawOval(0, 0, radius * 2, radius * 2);
		g.translate(radius, radius);

		AffineTransform state = ((Graphics2D)g).getTransform();

		for(int i = 0; i < 60; i++) {
			if(i % 5 == 0) {
				g.drawLine(0, -radius, 0, -radius + 10);

				int hour = i / 5;
				if(hour == 0) hour = 12;

				g.drawString(String.valueOf(hour), 0, -radius + 25);
			} else {
				g.drawLine(0, -radius, 0, -radius + 5);
			}
			((Graphics2D)g).rotate(Math.toRadians(6));
		}
		((Graphics2D)g).setTransform(state);

		drawHand(g, seconds * Math.PI / 6  / 3600, radius / 2);
		drawHand(g, seconds * Math.PI / 30 / 60, radius * 3 / 4);
		drawHand(g, seconds * Math.PI / 30, radius * 4 / 5);
	}

	public static void main(String[] args) {
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);

		ClockFace face = new ClockFace();
		Calendar calendar = Calendar.getInstance();
		face.setTime(calendar.get(Calendar.HOUR), calendar.get(Calendar.MINUTE), calendar.get(Calendar.SECOND));

		Timer timer = new Timer(1000, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				face.tick();
			}
		});
		timer.start();

		frame.add(face);

		JSpinner hour = new JSpinner();
		JSpinner minute = new JSpinner();
		JSpinner second = new JSpinner();

		JButton toggle = new JButton("Stop");

		toggle.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(timer.isRunning()) {
					timer.stop();
					toggle.setText("Start");
				} else {
					timer.start();
					toggle.setText("Stop");
				}
			}
		});

		ChangeListener timeChange = new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				face.setTime((int)hour.getValue(), (int)minute.getValue(), (int)second.getValue());
			}
		};

		hour.setModel(new LoopingSpinnerModel(12, 1, 12, 1));
		minute.setModel(new LoopingSpinnerModel(0, 0, 59, 1).setRollover(hour.getModel()));
		second.setModel(new LoopingSpinnerModel(0, 0, 59, 1).setRollover(minute.getModel()));

		minute.setEditor(new JSpinner.NumberEditor(minute, "00"));
		second.setEditor(new JSpinner.NumberEditor(second, "00"));

		hour.addChangeListener(timeChange);
		minute.addChangeListener(timeChange);
		second.addChangeListener(timeChange);

		JPanel controls = new JPanel();
		controls.setLayout(new GridLayout(1, 4));
		controls.add(hour);
		controls.add(minute);
		controls.add(second);
		controls.add(toggle);
		frame.add(controls, BorderLayout.SOUTH);

		frame.pack();
		frame.setVisible(true);
	}
}

@SuppressWarnings({"rawtypes", "unchecked", "serial"})
class LoopingSpinnerModel extends SpinnerNumberModel {
	private SpinnerModel rollover;

	public LoopingSpinnerModel() {
		super();
	}

	public LoopingSpinnerModel(Number value, Comparable minimum, Comparable maximum, Number stepSize) {
		super(value, minimum, maximum, stepSize);
	}

	public LoopingSpinnerModel(int value, int minimum, int maximum, int stepSize) {
		super(value, minimum, maximum, stepSize);
	}

	public LoopingSpinnerModel(double value, double minimum, double maximum, double stepSize) {
		super(value, minimum, maximum, stepSize);
	}

	public LoopingSpinnerModel setRollover(SpinnerModel rollover) {
		this.rollover = rollover;
		return this;
	}

	@Override
	public Object getNextValue() {
		Object next = super.getNextValue();
		Comparable maximum = getMaximum();

		if(next == null || maximum != null && maximum.compareTo(next) < 0) {
			next = getMinimum();
			if(rollover != null) rollover.setValue(rollover.getNextValue());
		}
		return next;
	}

	@Override
	public Object getPreviousValue() {
		Object prev = super.getPreviousValue();
		Comparable minimum = getMinimum();

		if(prev == null || minimum != null && minimum.compareTo(prev) > 0) {
			prev = getMaximum();
			if(rollover != null) rollover.setValue(rollover.getPreviousValue());
		}
		return prev;
	}
}
