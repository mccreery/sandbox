import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;

import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

@SuppressWarnings("serial")
public class FontChooser2 extends JFrame {
	private final ActionListener update = new ActionListener() {
		@Override
		public void actionPerformed(java.awt.event.ActionEvent e) {
			StringBuilder fullName = new StringBuilder();
			fullName.append(face.getSelectedItem());

			String family = fullName.toString();
			int style = 0;

			if(bold.isSelected()) {
				fullName.append(' ').append(bold.getText());
				style |= Font.BOLD;
			}
			if(italic.isSelected()) {
				fullName.append(' ').append(italic.getText());
				style |= Font.ITALIC;
			}
			fullName.append(' ').append(size.getValue()).append("pt");

			text.setText(fullName.toString());
			text.setFont(new Font(family, style, (int)size.getValue()));
		}
	};

	private final JSpinner size = new JSpinner();

	private final JCheckBox bold   = new JCheckBox("Bold");
	private final JCheckBox italic = new JCheckBox("Italic");

	private final JComboBox<String> face = new JComboBox<String>();

	private final JTextField text = new JTextField(20);

	public FontChooser2() {
		setTitle("Font Chooser");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.X_AXIS));

		bold.addActionListener(update);
		italic.addActionListener(update);
		face.addActionListener(update);

		JPanel checks = new JPanel();
		checks.setLayout(new BoxLayout(checks, BoxLayout.Y_AXIS));
		checks.add(bold);
		checks.add(italic);
		add(checks);

		face.setMaximumSize(face.getPreferredSize());
		//text.setMaximumSize(text.getPreferredSize());
		face.setModel(new DefaultComboBoxModel<String>(GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames()));

		add(face);
		
		size.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				update.actionPerformed(null);
			}
		});
		size.setModel(new SpinnerNumberModel(text.getFont().getSize(), 0, 300, 1));
		size.setMaximumSize(size.getPreferredSize());
		add(size);

		add(text);

		JButton ok = new JButton("OK");
		ok.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println(text.getText());
				dispatchEvent(new WindowEvent(FontChooser2.this, WindowEvent.WINDOW_CLOSING));
			}
		});
		add(ok);

		update.actionPerformed(null);
		pack();
	}

	public static void main(String[] args) {
		new FontChooser2().setVisible(true);
	}
}
