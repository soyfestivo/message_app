// Programmers:  Stephen Selke, Chris Griffith, Karen Bykowski
// Assignment:   Project 4 - Messenger App
// Date:         December 3, 2015
// Description:  Panel for displaying each of the messages
//        

import javax.swing.*;
import java.awt.*;

public class Message extends JPanel {
	public Message(String m, String username) {
   //PRE: m and username are initialized
   //POST: fills a JTextArea with the sender of the message and their message
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