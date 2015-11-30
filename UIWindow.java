// Programmers:  Stephen Selke, Chris Griffith, Karen Bykowski
// Assignment:   Project 4 - Messenger App
// Date:         December 3, 2015
// Description:  Main GUI for the messenger application
//        

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JOptionPane;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.util.ArrayList;

//class for user interface
public class UIWindow extends JFrame 
{
	private Backend backend;         //insance of Backend class
	private ChatPanel chatPanel;     //panel for displaying messages and send button and text area
   private UserPanel userPanel;     //panel to display the online Users
	private ArrayList<User> users;   //array of Users
   private String myUsername;       //curent User's username - users(0) 

	public UIWindow(String title) 
	//PRE:  title is initialized
	//POST: new UIWindow created
	{
		super(title);						      //call constructor of superclass 
      
      int width = 600;                    //width of the frame
      int height = 400;                   //height of the frame
      

		setLayout(new BorderLayout());		//set layout of frame to border layout
		backend = new Backend();

      myUsername = backend.getMyUsername();

		backend.addStaticUser(backend.getMyIP()); // add current User
		users = backend.getUsers();

		chatPanel = new ChatPanel(users.get(0), backend, myUsername);
      userPanel = new UserPanel(users, backend);
      chatPanel.msgReceived("You can send messages to yourself here or select one of the users from the left", "yourself");

		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);	 //set "close" functionality to close frame
		setSize(width, height);								    //set size of frame
		add(chatPanel, BorderLayout.CENTER);			    
      add(userPanel, BorderLayout.WEST);
		//pack();											          //force resize elements of UI
		setVisible(true);								          //make UI visible
      
	}


/*   private void setMyUsername()
   //PRE:  myUsername is delcared
   //POST: sets myUsername to user's chosen username
   {
      String username = "";
      
      while(username.equals("") || username == null || username.equals(" ")) //keep prompting
      {                                                                      //   until valid
         username = JOptionPane.showInputDialog(null, "What would you like your username to be?");
         
         if(username == null)
            username = "";
      }
      myUsername = username;      
      //return username;
   }
   
   public String getMyUsername()
   //PRE:  myUsername is non-blank and not null
   //POST: FCTVAL == myUsername
   {
      return myUsername;
   } */


   public static void main(String[] args) 
   {
		new UIWindow("CS 342 Final Project, Messaging app");
	}
}