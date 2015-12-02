// Programmers:  Stephen Selke, Chris Griffith, Karen Bykowski
// Assignment:   Project 4 - Messenger App
// Date:         December 3, 2015
// Description:  Main GUI for the messenger application

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
	    super(title);				      //call constructor of superclass 
      
        int width;                        //width of the frame
        int height;                       //height of the frame
      
        width = 600;
        height = 400;

		setLayout(new BorderLayout());		      //set layout of frame to border layout
		backend = new Backend(this);

        myUsername = backend.getMyUsername();     // get the current user's username

		backend.addStaticUser(backend.getMyIP()); // add current User
		users = backend.getUsers();

		chatPanel = users.get(0).getChatPanel();
        userPanel = new UserPanel(users, backend, this);
        chatPanel.msgReceived("You can send messages to yourself here or select one "
                            + "of the users from the left", "chat_bot");

		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);	 //set "close" functionality to close frame
		setSize(width, height);						     //set size of frame
		add(chatPanel, BorderLayout.CENTER);			    
        add(userPanel, BorderLayout.WEST);
		//pack();									     //force resize elements of UI
		setVisible(true);								 //make UI visible
      
        try                                              //give time to find users
        {
           Thread.sleep(4000);
        }
        catch(InterruptedException ie) {} 
   
        userPanel.updatePanel();                        //add buttons for online users
        userPanel.revalidate();
      
	}


   public void changeToUser(User user) 
   //PRE: user is a valid User
   //POST: will change the ChatPanel to the new user's chatPanel
   {
        remove(chatPanel);                              //remove current ChatPanel
        add(user.getChatPanel(), BorderLayout.CENTER);  //add the new ChatPanel
        chatPanel = user.getChatPanel();
        revalidate();
   }

   public static void main(String[] args) 
   //POST: Creates a new instance of UIWindow
   {
		new UIWindow("CS 342 Final Project, Messaging app");
   }
}
