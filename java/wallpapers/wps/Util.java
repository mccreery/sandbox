package wps;

import java.awt.Component;
import java.awt.DisplayMode;
import java.awt.GraphicsEnvironment;
import java.awt.Point;

import javax.swing.JOptionPane;

public final class Util {
	public static Point getDisplayLayout() {
		final int count = GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices().length;
		final int height = (count + 2) / 3;

		return new Point(count > 3 ? 3 : count, height > 3 ? 3 : height);
	}

	public static final DisplayMode MONITOR = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDisplayMode();

	public static final Resolution[] RESOLUTIONS = {
		new Resolution(640, 480),
		new Resolution(800, 600),
		new Resolution(1024, 768),
		new Resolution(1280, 720, "720p"),
		new Resolution(1280, 800),
		new Resolution(1366, 768),
		new Resolution(1440, 900),
		new Resolution(1680, 1050),
		new Resolution(1920, 1080, "1080p"),
		new Resolution(1920, 1200),
		new Resolution(2560, 1440, "2k"),
		new Resolution(3840, 2160, "4k"),
		new Resolution(7680, 4320, "8k")
	};

	public static void error(Component component, String message) {
		JOptionPane.showMessageDialog(component, message, "Error", JOptionPane.ERROR_MESSAGE);
	}
}
