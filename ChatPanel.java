import java.util.ArrayList;
import java.awt.Graphics;
import javax.swing.*;
import java.awt.event.*;
import java.awt.GridLayout;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.util.ArrayList;

public class ChatPanel extends JPanel implements ActionListener
{
   private JButton sendButton;      //button to send message
   private JTextArea msgField;      //text area for user to enter message to send
	private User user;   //array of Ussers
   private int numUsers;            //number of users
   private User me;                 //this user's information
   private int numMsgs = 500;             //number of messages in a conversation
   private Backend backend;
   
   
   public ChatPanel(User u, Backend b)
   {
      JPanel mainPan;          //main panel for messages, message text field, and send button
      JPanel msgPan;           //panel for all of the messages   
      JPanel sendPan;          //panel holding components for sending message
      JScrollPane scrollMsg;   //scroll pane for msgPanel
      JScrollPane scrollText;  //scroll pane for text area


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
      sendPan.add(scrollText, BorderLayout.CENTER);
      //send button
      sendButton = new JButton("Send");
      sendPan.add(sendButton, BorderLayout.EAST);
      sendButton.addActionListener(this);


      msgPan = new JPanel();
      msgPan.setLayout(new BoxLayout(msgPan, BoxLayout.PAGE_AXIS)); // from top to bottom
      scrollMsg = new JScrollPane(msgPan);
      mainPan.add(scrollMsg, BorderLayout.CENTER);
      

      //users[0] = new User(kbyk, );
      JPanel pans[] = new JPanel[numMsgs];
      
      for(int i = 0; i < 10; i++)
      {
        //pans[i] = new JPanel();
        //pans[i].add(new JLabel("<html>testing " + i + " asldk<br>jfwoieslksldfwoiefj<br><br>dslkcnaliweraofsdvnwir92834urwe<br>fsdjfhasdfhsadjfkasjhdfas<br><br><br></html>"));
        //pans[i].add(new JLabel("<html>users[0]<br>"blah blah blah blah"</html>);
        msgPan.add(new Message("Hello, this is a test!", "soyfestivo")); 
      }
      
   }

   public void actionPerformed(ActionEvent e)
    // POST: FCTVAL == executes the parseInput function, in the event that the button is pressed
    {
        // Will only execute if the button is pressed
      if(e.getSource() == sendButton)
        {
         backend.sendMessage(user, msgField.getText());
         //parentClass.actionPerformed(this, parseInput());
         //textBox.setText("");
      }
   }
   
   public void paint(Graphics g)
   {
      super.paint(g);
   
   }
   
   

} //end class