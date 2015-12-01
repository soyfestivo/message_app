// Programmers:  Stephen Selke, Chris Griffith, Karen Bykowski
// Assignment:   Project 4 - Messenger App
// Date:         December 3, 2015
// Description:  
//        

import java.util.ArrayList;
import java.awt.Graphics;
import javax.swing.*;
import java.awt.event.*;
import java.awt.GridLayout;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Color;
import java.util.ArrayList;

public class ChatPanel extends JApplet implements ActionListener
{
   private JButton sendButton;      //button to send message
   private JTextArea msgField;      //text area for user to enter message to send
   private JScrollPane scrollMsg;   //scroll pane for msgPanel
	private User user;               //other User that the current User is chatting with
   private User me;                 //this user's information
   private JPanel msgPan;           //panel for all of the messages   
   private Backend backend;         //instance of Backend
   private String myUsername;       //current User's username   

   
   public ChatPanel(User u, Backend b, String uname)
   //PRE: u, b, uname are initialized
   //POST: Sets backend = b, user = u, and myUsername = myUsername from Backend.
   //      Creates a panel that stores all of the messages with User uname, a JTextArea for typing
   //      a message to send to User uname, and a send button.
   {
      JPanel mainPan;          //main panel for messages, area for sending messages, and send button
      
      JPanel sendPan;          //panel holding components for sending message
      JScrollPane scrollText;  //scroll pane for text area   

      backend = b;
      user = u;
      myUsername = backend.getMyUsername();
                                                      //panel for this entire JApplet
      mainPan = new JPanel();
      mainPan.setLayout(new BorderLayout());
      add(mainPan);
                                                      //set up panel the user interacts with
      sendPan = new JPanel();
      sendPan.setLayout(new BorderLayout());
      mainPan.add(sendPan, BorderLayout.SOUTH);
      
                                                      //set up text area for sending messages
      msgField = new JTextArea(2, 25);
      scrollText = new JScrollPane(msgField);
      msgField.setLineWrap(true);
      msgField.setWrapStyleWord(true);
      sendPan.add(scrollText, BorderLayout.CENTER);


                                                      //set up send button
      sendButton = new JButton("Send");
      sendPan.add(sendButton, BorderLayout.EAST);
      sendButton.addActionListener(this);

                                                      //set up area for all messages from this chat
      msgPan = new JPanel();
      msgPan.setLayout(new BoxLayout(msgPan, BoxLayout.PAGE_AXIS)); //from top to bottom
      scrollMsg = new JScrollPane(msgPan);                          //add scroll bar
      mainPan.add(scrollMsg, BorderLayout.CENTER);    
   }
   
                                                //actionPerformed for sendButton
   public void actionPerformed(ActionEvent e)
    // POST: In the event that sendButton is pressed, sendMessage function from backend is called;
    //       this sends the message to User uname.
    {
      if(e.getSource() == sendButton)     // Will only execute if the button is pressed
      {
         msgPan.add(new Message(msgField.getText(), myUsername, Color.CYAN)); 
         backend.sendMessage(user, msgField.getText());
         //parentClass.actionPerformed(this, parseInput());
         msgField.setText("");            //sets msgField to be blank after message has been sent
         afterMessage();
      }
   }
   
   
   public void msgReceived(String message, String username)
   //PRE:  message and username are initialized
   //POST: adds a new Message to msgPan with the text message and username
   {
      msgPan.add(new Message(message, username, Color.YELLOW));
      afterMessage();
   }
   
   
/*   public void newMessage()
   //PRE:
   //POST: If a new message has been received, calls msgReceived which creates a new Message
   //      with the message and username
   {
      if(backend.isMsgRcvd())             //if a new message has been received
      {
         msgReceived(backend.getInUser(), backend.getInMessage());
      }
   }*/
   
   
   private void afterMessage()
   //PRE:  msgPan and scrollMsg are initialized 
   //POST: a blank JLabel is added to msgPan, msgField text is set to empty, 
   //      the panel is revalidated, and the scollMsg is pushed on to the bottom
   {
         this.revalidate();                                      //refresh
         JScrollBar bar = scrollMsg.getVerticalScrollBar();      //move the scroll bar to the bottom
         bar.setValue(bar.getMaximum());
   }  
   
} //end class