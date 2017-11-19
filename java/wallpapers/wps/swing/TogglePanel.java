package wps.swing;

import java.awt.Color;

import javax.swing.JPanel;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;

public class TogglePanel extends JPanel {
	private static final long serialVersionUID = 2696474071827110081L;

	private final Color active, inactive;
	private static final Border ACTIVE_BORDER = new BevelBorder(BevelBorder.RAISED),
		INACTIVE_BORDER = new BevelBorder(BevelBorder.LOWERED);

	public TogglePanel(Color active, Color inactive) {
		this.active = active;
		this.inactive = inactive;
	}

	@Override
	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);

		if(isEnabled()) {
			setBackground(active);
			setBorder(ACTIVE_BORDER);
		} else {
			setBackground(inactive);
			setBorder(INACTIVE_BORDER);
		}
	}
}
