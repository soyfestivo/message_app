// Programmers:  Stephen Selke, Chris Griffith, Karen Bykowski
// Assignment:   Project 4 - Messenger App
// Date:         December 3, 2015
// Description:  Contains all the information for each person running the application
//        

public class User 
{
	private String username;               //identifier for this User
	private String host;                   //this User's IP address
    private ChatPanel myPanel;             //instance of ChatPanel for this User

	public User(String username, String host, Backend backendInstance) 
   //PRE: username and host are initialized
   //POST: sets username as username and host as host
   {
		this.username = username;
		this.host = host;
		myPanel = new ChatPanel(this, backendInstance, username);
	}

	public String getUsername() 
   //POST: FCTVAL == username
   {
		return username;
	}

	public ChatPanel getChatPanel()
	//POST: FCTVAL == chatpanel
	{
		return myPanel;
	}

	public String getHost() 
   //POST: FCTVAL == host
   {
		return host;
	}
}