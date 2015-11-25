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
public class UIWindow extends JFrame {
	private Backend backend;
	private ChatPanel chatPanel; // main panel
	private ArrayList<User> users;

	public UIWindow(String title) 
	//PRE:  title is valid
	//POST: new UIWindow created
	{
		super(title);						      //call constructor of superclass 
      
      int width = 600;                    //width of the frame
      int height = 400;                   //height of the frame

		setLayout(new BorderLayout());		//set layout of frame to border layout
		backend = new Backend();

		backend.addStaticUser(new User("soyfestivo", "192.168.56.1")); // for demoing
		users = backend.getUsers();

		chatPanel = new ChatPanel(users.get(0), backend);
      
      add(new JButton("temp"), BorderLayout.WEST); //////////remove
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);	 //set "close" functionality to close frame
		setSize(width, height);								    //set size of frame
		add(chatPanel, BorderLayout.CENTER);			    //add the option bar to UI
		//pack();											          //force resize elements of UI
		setVisible(true);								          //make UI visible
	}


	public static void main(String[] args) {
		new UIWindow("CS 342 Final Project, Messaging app");
	}
}