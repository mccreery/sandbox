package wps;

import java.awt.Point;

public class Resolution extends Point {
	private static final long serialVersionUID = -8461780617103489407L;
	private final String label;

	public Resolution(int x, int y) {
		this(x, y, null);
	}
	public Resolution(int x, int y, String label) {
		super(x, y);

		if(label != null && !label.isEmpty()) {
			this.label = String.format("%dx%d (%s)", x, y, label);
		} else {
			this.label = String.format("%dx%d", x, y);
		}
	}

	@Override
	public String toString() {
		return label;
	}
}
