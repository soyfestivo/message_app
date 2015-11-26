import javax.swing.*;
import java.awt.*;

public class Message extends JPanel {
	public Message(String m, String username) {
   //PRE:
   //POST:
      JTextArea msg;                                        //text area for new messages
   
		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		//add(new JLabel(m));
		//add(new JLabel("@"+username));
      msg = new JTextArea("@" + username + ":\n" + m + "\n");   
      add(msg);
      msg.setEditable(false);
      msg.setLineWrap(true);
      msg.setWrapStyleWord(true);
	}
}