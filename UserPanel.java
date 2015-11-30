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
      private ArrayList<User> users;       //array of users
      private ArrayList<JButton> buttons;  //array of user buttons
      private JPanel mainPan;              //main panel for this class

      private User staticUser;             //user at a static ip address
      private int numUsers;                //total number of users online

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
      // POST: updates UserPanel with the correct number of user buttons
      {
         // remove all old buttons prior to adding new ones
         if (buttons != null)
         {
            while(buttons.size() > 2)
            {
                mainPan.remove(buttons.get(buttons.size() - 1));
                buttons.remove(buttons.size() - 1);
            }
         }

         // iterate through each user
         for (int i = 0; i < users.size(); i++)
         {
            int j = i + 2;
            // add a button for each user action
            if(users.get(i) != null)
            {
                buttons.add(new JButton(users.get(i).getUsername()));
                mainPan.add(buttons.get(j));
                buttons.get(j).addActionListener(this);
            }
         }
      }

      public void actionPerformed(ActionEvent e)
      {

         if(e.getSource() == buttons.get(0))    // User selected add IP manually button
         {
            String host = addIP();              // Prompt user for IP
           
            staticUser = new User(host, host);  // Create new user based on static IP

            backend.addStaticUser(staticUser);  // Add static user to list

            users = backend.getUsers();         // Update this users list
         
            updatePanel();

            mainPan.revalidate();

         }

         if(e.getSource() == buttons.get(1))    // User selected button to find online users
         {
           
            // TODO - Call scan for IP method from backend and updatePanel

         }

         if(e.getSource() instanceof JButton &&
            e.getSource() != buttons.get(0)  &&
            e.getSource() != buttons.get(1)  )
         {
            users = backend.getUsers();         // Update this users list

            for(int i = 0; i < users.size(); i++)
            {
                if( ((JButton)e.getSource()).getText().equals(users.get(i).getUsername()))
                {
                    //Display chat info in chatPanel
                    // pass username and ip to chat panel method that will display a message panel

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
