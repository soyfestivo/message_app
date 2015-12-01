// Programmers:  Stephen Selke, Chris Griffith, Karen Bykowski
// Assignment:   Project 4 - Messenger App
// Date:         December 3, 2015
// Description:  
//        

import javax.swing.*;
import java.awt.event.*;
import java.awt.GridLayout;
import java.util.ArrayList;


public class UserPanel extends JApplet implements ActionListener
{
      private Backend backend;             //instance of Backend
      private UIWindow window;             //instance of UI Window
      private ArrayList<User> users;       //array of users
      private ArrayList<JButton> buttons;  //array of user buttons
      private JPanel mainPan;              //main panel for this class

      private User staticUser;             //user at a static ip address
      private int numUsers;                //total number of users online

      public UserPanel(ArrayList<User> u, Backend b, UIWindow window)
      //PRE:
      //POST:
      {
         JLabel myIP;                      //lable at top of panel for ip

         this.users = u;
         this.backend = b;
         this.window = window;

         mainPan = new JPanel();
         mainPan.setLayout(new BoxLayout(mainPan, BoxLayout.PAGE_AXIS)); //from top to bottom
         add(mainPan);

         myIP = new JLabel("My IP: " + users.get(0).getHost());
         mainPan.add(myIP);

                                                                        // initialize array of buttons
         buttons = new ArrayList<JButton>();

         buttons.add(new JButton("Add a user by IP"));
         mainPan.add(buttons.get(0));
         
         buttons.add(new JButton("Find online users"));
         mainPan.add(buttons.get(1));

         mainPan.add(new JLabel("Users List:"));
        
         buttons.get(0).addActionListener(this);
         buttons.get(1).addActionListener(this);
         
         updatePanel();

      }

      public void updatePanel()
      // POST: updates UserPanel with the correct number of user buttons
      {
                                                // remove all old buttons prior to adding new ones
         if (buttons != null)                   // if there are buttons
         {
            while(buttons.size() > 2)           // if there is more than the "Add a user by IP" and
            {                                   //    "Find online users" buttons
                mainPan.remove(buttons.get(buttons.size() - 1));
                buttons.remove(buttons.size() - 1);
            }
         }

         for (int i = 0; i < users.size(); i++) // iterate through each user
         {
            int j = i + 2;
                                               // add a button for each user action
            if(users.get(i) != null)           // if users isn't empty, add a button for each User
            {           
                buttons.add(new JButton(users.get(i).getUsername()));
                mainPan.add(buttons.get(j));
                buttons.get(j).addActionListener(this);
            }
         }
      }

                                                       // Handles any button clicks
      public void actionPerformed(ActionEvent e)
      {
         String host;         // User's IP

         if(e.getSource() == buttons.get(0))           // User selected add IP manually button
         {
            host = addIP();                            // Prompt user for IP
           
            staticUser = backend.addStaticUser(host);  // Add static user to list

            users = backend.getUsers();                // Update this users list
         
            updatePanel();

            mainPan.revalidate();

         }

         if(e.getSource() == buttons.get(1))    // User selected button to find online users
         {  
            int split;                          // Amount to split the group size into when
                                                //    searching for Users
            
            split = 16;
         
            backend.scanLAN(split);
            repaint();
            
            try                                // Sleep for 4 seconds to give time to find users
            {
               Thread.sleep(4000);
            }
            catch(InterruptedException ie) {}
            
            updatePanel();                     // Update the panel with the buttons for all found Users
            mainPan.revalidate();

         }

         if(e.getSource() instanceof JButton &&     // If a button other than "Add a user by IP"
            e.getSource() != buttons.get(0)  &&     //    or "Find online users" was clicked
            e.getSource() != buttons.get(1)  )
         {
            users = backend.getUsers();            // Update this users list

            for(int i = 0; i < users.size(); i++)  // Loop through the users
            {
                                                  // Find out which User the button is associated with
                if( ((JButton)e.getSource()).getText().equals(users.get(i).getUsername()))
                {
                    //Display chat info in chatPanel
                    // pass username and ip to chat panel method that will display a message panel
                  //System.out.println("clicked:" + users.get(i).getUsername());
                    window.changeToUser(users.get(i));
                    //user.get(i).getChatPanel();
            
                }
            }

         }

      }
      

      private String addIP()
      //POST: FCTVAL == host string
      {  
         String host;     // IP address from user
             
         host = ""; 
    
         // TODO - Could add regular expresion here to verify input is valid

         while(host.equals("") || host == null || host.equals(" ")) // keep prompting
         {                                                                  
             host = JOptionPane.showInputDialog(null, "Please enter an IP addres"
                                                   + " (ie. 192.168.0.111).");
             if(host == null)
                host = ""; 
         }   

         return host;
      }   
}
