// Programmers:  Stephen Selke, Chris Griffith, Karen Bykowski
// Assignment:   Project 4 - Messenger App
// Date:         December 3, 2015
// Description:  Panel for displaying each of the messages

import javax.swing.*;
import java.awt.*;

public class Message extends JPanel 
{
	public Message(String m, String username, Color color) 
   //PRE: m, username, and color are initialized
   //POST: Fills a JTextArea with color, the username of the sender, and their message
   {
      JTextArea msg;                              //text area for new messages
   
      setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
      msg = new JTextArea("@" + username + ":\n" + m + "\n");   
      add(msg);
      msg.setEditable(false);       //can't edit text
      msg.setLineWrap(true);        //wrap inside msg, don't flow over the end
      msg.setWrapStyleWord(true);
      
      msg.setBackground(color);
   }
}
