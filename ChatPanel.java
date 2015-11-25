import java.util.ArrayList;
import java.awt.Graphics;
import javax.swing.*;
import java.awt.event.*;
import java.awt.GridLayout;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.util.ArrayList;

public class ChatPanel extends JApplet implements ActionListener
{
   private JButton sendButton;      //button to send message
   private JTextArea msgField;      //text area for user to enter message to send
   private JScrollPane scrollMsg;   //scroll pane for msgPanel
	private User user;               //other User that the current User is chatting with
   private User me;                 //this user's information
   //private int numMsgs;             //number of messages in a conversation
   private JPanel msgPan;           //panel for all of the messages   
   private Backend backend;         //instance of Backend
   
   
   public ChatPanel(User u, Backend b)
   {
      JPanel mainPan;          //main panel for messages, message text field, and send button
      
      JPanel sendPan;          //panel holding components for sending message
      JScrollPane scrollText;  //scroll pane for text area

      String myUsername;       //current User's username
      
      myUsername = getMyUsername();    

      backend = b;
      user = u;
      mainPan = new JPanel();
      mainPan.setLayout(new BorderLayout());
      add(mainPan);
     
      sendPan = new JPanel();
      sendPan.setLayout(new BorderLayout());
      mainPan.add(sendPan, BorderLayout.SOUTH);
      //text to send
      msgField = new JTextArea(2, 25);
      scrollText = new JScrollPane(msgField);
      msgField.setLineWrap(true);
      msgField.setWrapStyleWord(true);
      sendPan.add(scrollText, BorderLayout.CENTER);


      //send button
      sendButton = new JButton("Send");
      sendPan.add(sendButton, BorderLayout.EAST);
      sendButton.addActionListener(this);


      msgPan = new JPanel();
      msgPan.setLayout(new BoxLayout(msgPan, BoxLayout.PAGE_AXIS)); // from top to bottom
      scrollMsg = new JScrollPane(msgPan);
      mainPan.add(scrollMsg, BorderLayout.CENTER);    
      
//////////////// for testing REMOVE when complete /////////////////      
      /*for(int i = 0; i < 20; i++)
      {
        //pans[i] = new JPanel();
        //pans[i].add(new JLabel("<html>testing " + i + " asldk<br>jfwoieslksldfwoiefj<br><br>dslkcnaliweraofsdvnwir92834urwe<br>fsdjfhasdfhsadjfkasjhdfas<br><br><br></html>"));
        //pans[i].add(new JLabel("<html>users[0]<br>"blah blah blah blah"</html>);
        msgPan.add(new Message("Hello, this is a test #" + i + "!", "soyfestivo")); 
      }*/
      
   }

   public void actionPerformed(ActionEvent e)
    // POST: FCTVAL == executes the parseInput function, in the event that the button is pressed
    {
        // Will only execute if the button is pressed
      if(e.getSource() == sendButton)
      {
         //numMsgs++;
         msgPan.add(new JLabel("  "));                            //added for spacing between messages
         msgPan.add(new Message(msgField.getText(), "soyfestivo")); 
         //backend.sendMessage(user, msgField.getText());
         //parentClass.actionPerformed(this, parseInput());
         //textBox.setText("");
         msgField.setText("");                                   //clear the text in msgField
         this.revalidate();                                      //refresh
         JScrollBar bar = scrollMsg.getVerticalScrollBar();      //move the scroll bar to the bottom
         bar.setValue(bar.getMaximum());
      }
   }
   
   
/*   public void paint(Graphics g)
   {
      super.paint(g);
   
   } */
   
   private String getMyUsername()
   //PRE:
   //POST: FCTVAL == username; returns the string the user wants as their username
   {
      String username = "";
      
      while(username.equals("") || username.equals(null) || username.equals(" ")) //keep prompting
      {                                                                           //until valid
         username = JOptionPane.showInputDialog(null, "What would you like your username to be?");
         
         if(username == null)
            username = "";
      }
      
      return username;
   }

} //end class