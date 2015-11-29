import javax.swing.*;
import java.awt.event.*;
import java.awt.GridLayout;
import java.util.ArrayList;


public class UserPanel extends JApplet implements ActionListener
{
      private Backend backend;             //instance of Backend
      private ArrayList<User> users;       //array of users
      private ArrayList<JButton> buttons;  //array of user buttons
      private JPanel mainPan;              //main panel for this class

      private User staticUser;             //user at a static ip address

      public UserPanel(ArrayList<User> u, Backend b)
      //PRE:
      //POST:
      {
         users = u;
         backend = b;
         
         mainPan = new JPanel();
         mainPan.setLayout(new BoxLayout(mainPan, BoxLayout.PAGE_AXIS)); //from top to bottom
         //mainPan.setLayout(new GridLayout(2,1));
         add(mainPan);

         // initialize array of buttons
         buttons = new ArrayList<JButton>();

         buttons.add(new JButton("Add an user by IP"));
         mainPan.add(buttons.get(0));
         
         buttons.add(new JButton("Find online users"));
         mainPan.add(buttons.get(1));
        
         buttons.get(0).addActionListener(this);
         buttons.get(1).addActionListener(this);
         
         updatePanel();

      }

      public void updatePanel()
      {
         // remove all old buttons prior to adding new ones
         if (buttons != null)
         {
            for (int i = 2; i < buttons.size(); i++)
            {
                mainPan.remove(buttons.get(i));
            }
         }

         // iterate through each user
         for (int i = 2; i < users.size(); i++)
         {
            // add a button for each user action
            if(users.get(i) != null)
            {
                buttons.add(new JButton(users.get(i).getUsername()));
                mainPan.add(buttons.get(i));
                buttons.get(i).addActionListener(this);
            }
         }
      }

      public void actionPerformed(ActionEvent e)
      {

         if(e.getSource() == buttons.get(0))     // User selected add IP manually button
         {
            String host = addIP();           // Prompt user for IP
           
            staticUser = new User(host, host);  // Create new user based on static IP

            backend.addStaticUser(staticUser);  // Add static user to list

         }

         if(e.getSource() == buttons.get(1))    // User selected button to find online users
         {


         }

      }

      private String addIP()
      //POST: FCTVAL == host string
      {  
         String host;     // IP address from user

         try 
         {   
             //String to store the user's input
             host = JOptionPane.showInputDialog("Please enter an IP"
                     + " address (ie. 192.168.0.111).");
             System.out.println(host);
    
             // TODO - Could add regular expresion here to verify input is valid

             while(host.equals("") || host == null || host.equals(" ")) // keep prompting
             {                                                                  
                host = JOptionPane.showInputDialog(null, "Please enter an IP addres"
                                                   + " (ie. 192.168.0.111).");
                if(host == null)
                    host = ""; 
             }   
         }   
         catch(NullPointerException npe)       //An error occured, so catch the exception
         {   
             host = "";
         }   

         return host;
      }   
}
