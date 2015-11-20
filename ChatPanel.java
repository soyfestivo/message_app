import java.awt.Graphics;
import javax.swing.*;
import java.awt.GridLayout;
import java.awt.BorderLayout;
import java.awt.FlowLayout;

public class ChatPanel extends JApplet
{
   private JButton sendButton;   //button to send message
   private JTextArea msgField;   //text area for user to enter message to send
   
   
   public void init()
   {
      JPanel mainPan;          //main panel for messages, message text field, and send button
      JPanel msgPan;           //panel for all of the messages   
      JPanel sendPan;          //panel holding components for sending message
      JScrollPane scrollMsg;   //scroll pane for msgPanel
      JScrollPane scrollText;  //scroll pane for text area
   
   
      mainPan = new JPanel();
      mainPan.setLayout(new BorderLayout());
      add(mainPan);
     
      sendPan = new JPanel();
      sendPan.setLayout(new BorderLayout());
      mainPan.add(sendPan, BorderLayout.SOUTH);
      //text to send
      msgField = new JTextArea(4, 25);
      scrollText = new JScrollPane(msgField);
      sendPan.add(scrollText, BorderLayout.CENTER);
      //send button
      sendButton = new JButton("Send");
      sendPan.add(sendButton, BorderLayout.EAST);
 

      msgPan = new JPanel();
      msgPan.setLayout(new GridLayout(30, 1));
      scrollMsg = new JScrollPane(msgPan);
      mainPan.add(scrollMsg, BorderLayout.CENTER);
      


      JPanel pans[] = new JPanel[30];
      
      for(int i = 0; i < 30; i++)
      {
        pans[i] = new JPanel();
        pans[i].add(new JLabel("<html>testing " + i + " asldk<br>jfwoieslksldfwoiefj<br><br>dslkcnaliweraofsdvnwir92834urwe<br>fsdjfhasdfhsadjfkasjhdfas<br><br><br></html>"));
        msgPan.add(pans[i]); 
      }
      
      
   }
   
   public void paint(Graphics g)
   {
      super.paint(g);
   
   }
   
   

} //end class