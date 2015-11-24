import javax.swing.*;
import java.awt.*;

public class Message extends JPanel {
	public Message(String m, String username) {
		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		add(new JLabel(m));
		add(new JLabel("@"+username));
	}
}