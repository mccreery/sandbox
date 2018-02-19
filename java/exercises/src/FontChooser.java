import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

@SuppressWarnings("serial")
public class FontChooser extends JFrame {
	public FontChooser() {
		setTitle("Font Chooser");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.X_AXIS));

		JPanel checks = new JPanel();
		checks.setLayout(new BoxLayout(checks, BoxLayout.Y_AXIS));
		checks.add(new JCheckBox("Bold"));
		checks.add(new JCheckBox("Italic"));
		add(checks);

		JComboBox<String> font = new JComboBox<String>(new String[] {
			"Times", "Helvetica", "Courier"
		});
		font.setMaximumSize(font.getPreferredSize());
		add(font);

		JTextField field = new JTextField(20);
		field.setMaximumSize(field.getPreferredSize());
		add(field);
		add(new JButton("OK"));

		pack();
	}

	public static void main(String[] args) {
		new FontChooser().setVisible(true);
	}
}
