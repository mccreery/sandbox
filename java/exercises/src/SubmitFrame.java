import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

@SuppressWarnings("serial")
public class SubmitFrame extends JFrame {
	private final JTextField field = new JTextField(30);
	private final JButton submit = new JButton("Submit");
	private final JButton cancel = new JButton("Cancel");

	public SubmitFrame() {
		setTitle("Simple Submit Cancel Form");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		initFlow();
		//initBorder();
		//initGrid();
	}

	private void initFlow() {
		setLayout(new FlowLayout());
		add(field);
		add(submit);
		add(cancel);
		pack();
	}

	private void initBorder() {
		setLayout(new BorderLayout());
		add(field);
		add(getButtonPanel(), BorderLayout.SOUTH);
		pack();
	}

	private void initGrid() {
		setLayout(new GridLayout(2, 1));
		add(field);
		add(getButtonPanel());
		pack();
	}

	private JPanel getButtonPanel() {
		JPanel buttons = new JPanel();
		FlowLayout layout = new FlowLayout();
		layout.setAlignment(FlowLayout.RIGHT);

		buttons.add(submit);
		buttons.add(cancel);
		return buttons;
	}

	public static void main(String[] args) {
		new SubmitFrame().setVisible(true);
	}
}
