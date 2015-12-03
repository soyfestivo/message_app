// Programmers:  Stephen Selke, Chris Griffith, Karen Bykowski
// Assignment:   Project 4 - Messenger App
// Date:         December 3, 2015
// Description:  Backend for the messenger application. Handles finding other Users,
//                  sending messages, and receiving messages.

import java.net.*;
import java.io.*;
import java.lang.Runnable;
import java.lang.Thread;
import java.util.ArrayList;
import java.util.Scanner;
import javax.swing.JOptionPane;
import java.util.Enumeration;

public class Backend 
{
	private final int PUBLIC_PORT = 7007;                 //port to connect to
	private final String PROTOCOL_WHOIS = "WhoIs";        //who is string
	private final String PROTOCOL_FROM = "MessageFrom";   //from string
	private final String PROTOCOL_RECIEVED = "GotIt";     //received string
	private User me;                                      //current User
	private ArrayList<User> users;                        //list of all people current User can chat with
	private Thread incomingListener;                      //thread for listening
   private String myIP;                                  //current User's IP address
   private String myUsername;                            //current User's username
   private UIWindow window;                              //front end connection


	public Backend(UIWindow window) 
   //PRE:  window is initialized
   //POST: constructs an instance of the backend class 
   { 
      InetAddress myAddress;                //Current user's IP address
   
      setupThread();
      incomingListener.start();
      users = new ArrayList<User>();
      
      setMyUsername();                     //get the user's desired username

      this.window = window;

      try                                 //try to get current User's IP
      {   
         myAddress = InetAddress.getLocalHost();
         myIP = myAddress.getHostAddress();
         me = new User(myUsername, myAddress.getHostAddress(), this);
      }
      catch(Exception ex) 
      {
         System.err.println("Error Getting my own IP" + ex.toString());
         System.exit(0);
      }

      scanLAN(16);                        // scan for new Users
   }

   private void setMyUsername()
   //PRE:  myUsername is delcared
   //POST: sets myUsername to user's chosen username
   {
      String username;     // String representation of the current User
      
      username = "";       // set username to empty string
      
      while(username.equals("") || username == null || username.equals(" ")) //keep prompting
      {                                                                      //   until valid
         username = JOptionPane.showInputDialog(null, "What would you like your username to be?\n"
                                                + "The length must be 10 characters or less.");
         
         if(username == null)                                                //if username == null
         {
            username = "";                                                   //set username to empty
         }
         
         if(username.length() > 10)                                          //if the username is
         {                                                                   //  longer than 10 chars
            JOptionPane.showMessageDialog(null, "Username must be less than 10 characters.",
                                          "Username too long", JOptionPane.ERROR_MESSAGE);
            username = "";
         }
      }
      
      myUsername = username;      
   }
   
   
   public String getMyUsername()
   //POST: FCTVAL == myUsername
   {
      return myUsername;
   }


	public ArrayList<User> getUsers() 
    //POST: FCTVAL == users
   {
		return users;
	}

	private class MiniScan extends Thread 
   {
	   //private int id;      //
      private int min;     // minimum of scan range
		private int max;     // max of scan range

		public MiniScan(int min, int max) 
        //PRE: min and max are valid 0-255
        //POST: Scans the network
        {
			super();

			this.min = min;
			this.max = max;
			start();
		}

		@Override
		public void run() 
      //POST: Runs scanRange function
      {
			scanRange(min, max);
		}
	}

	public void scanRange(int min, int max) 
   //PRE: min and max are valid 0-255
   //POST: Will scan IPs: prefix+range(min, max)
   {
      User u;            // User the current User is trying to connect with
      String[] myIPArr;  // current User's IP split into chunks
      String prefix;     // the prefix of the current ip
      
		myIPArr = me.getHost().split("\\.");
		prefix = myIPArr[0] + "." + myIPArr[1] + "." + myIPArr[2] + ".";
		for(int subAddress = min; subAddress <= max; subAddress++) 
      {
			if(!me.getHost().equals(prefix + subAddress)) // don't add yourself
         { 
				u = handshake(prefix + subAddress);
		
      		if(u != null)                             // if u isn't null
            {
					addUser(u);
				}
			}
		}
	}

	private void addUser(User u) 
   //PRE: user is initialized
   //POST: A new user will be added if it does not already exist
   {
		for(User user : users) 
      {
			if(user.getHost().equals(u.getHost())) 
         { 
         	if(!user.getUsername().equals(u.getUsername())) //only re-add if they changed their username
         	{
               users.remove(user);
               users.add(u);
         	}
				return;
			}
		}
		users.add(u);
	}

	public User addStaticUser(String host) 
   //PRE: host is a valid IP address
   //POST: FCTVAL == u; the User you are connecting with
   {
      User u;           // User the current user is trying to connect with
         
      u = handshake(host);
      if(u != null)     // If you can connect with u
      {
         addUser(u);    // Add them to users array
      }
      return u;
   }


	public void scanLAN(int split) 
   //PRE: split is the thread count
   //POST: scan local area network, with split # of threads
   {
      int groupSize;    // size of scan range
      
      groupSize = 256 / split;

      for(int i = 0; i < split; i++) 
      {
         new MiniScan(groupSize * i, (groupSize * i) + groupSize - 1);
      }
   }


	private User handshake(String host) 
   //PRE: host is a valid IP address
   //POST: Try to make a connection with host, returns new user instance from host
   {
      String username;     // String representation for the user
      char c;              // used in parsing the username
      Socket connection;   // Socket to connect with
   
      try 
      {
         connection = new Socket();
         connection.connect(new InetSocketAddress(host, PUBLIC_PORT), 1000);
         OutputStream out = connection.getOutputStream();
         InputStream in = connection.getInputStream();

         out.write(PROTOCOL_WHOIS.getBytes());
         out.write(0x0);
         out.flush();
         username = "";

         while((c = (char) in.read()) != 0x0)  // read response
         {
            username += c;
         }

         out.close();
			in.close();
			connection.close();
			return new User(username, host, this);
      }
		catch(Exception e) {}
		return null;
	}

	private void setupThread() 
   //POST: a new listening thread will be created
   {
      incomingListener = new Thread() 
      {
   		public void run() 
         //POST: thread will run
         {
            ServerSocket openSocket;   // the server socket for listening
            Socket connection;         // a given connection
            InputStream in;            // the input stream of the connection
            OutputStream out;          // the output stream of the connection
         
				openSocket = null;         // try to connect
				try 
            {
					openSocket = new ServerSocket(PUBLIC_PORT);
				}
	
   			catch(Exception e)         // we couldn't listen on the given port
            {
					System.err.println("Cannot bind to port %d".format(""+PUBLIC_PORT));
					System.exit(0);
				}

				while(true)               // main loop; loops until window is closed
            {
				   try 
               {
				   	connection = openSocket.accept(); // blocking accept function, waiting for 
						in = connection.getInputStream();
						out = connection.getOutputStream();
						handelIncomingRequest(in, out);
						in.close();
						out.close();
						connection.close();
					}
					catch(Exception e) {}
				}
			}
		};
	}


	private void handelIncomingRequest(InputStream in, OutputStream out) 
   //PRE: in is valid, out is valid
   //POST: We will handle the request properly
   {
      String header = "";     // header of the network packet
		String message = "";    // message sent in the network packet
		char c;                 // used to parse the header

		try 
      {
			while((c = (char) in.read()) != 0x0)               // read in header
         {
		   	header += c;
			}
         
			if(header.equals(PROTOCOL_WHOIS))                  // if it is the WHOIS protocol
         {
				out.write(me.getUsername().getBytes());
				out.write(0x0);
				out.flush();
			}
         
			else if(header.indexOf(PROTOCOL_FROM + ":") != -1) // else if the from protocol is >= 0
         {
				String[] parse;                          
            
            parse = header.split(":");
				while((c = (char) in.read()) != 0x0)            // read message 
            {
					message += c;
				}
            
				messageNotify(parse[1], message);
				out.write(PROTOCOL_RECIEVED.getBytes());
				out.write(0x0);
				out.flush();
			}
		}
		catch(Exception e) {}
	}


	private void messageNotify(String username, String message) 
   //PRE:  username and message is valid
   //POST: the user's ChatPanel will be updated
   {
      for(User u : users) 
      {
        	if(u.getUsername().equals(username))                  // if User's username == username
         {         
      		u.getChatPanel().msgReceived(message, username);   // call msgReceived in ChatPanel to
      		window.changeToUser(u);                       
      		return;                                            // display the message in ChatPanel
      	}
      }
	}


	public boolean sendMessage(User user, String message) 
   //PRE: user is valid
   //POST: the message will be sent
   {
      String header;          // header of the incoming packet
      String m;               // String for telling who is sending the packet
      Socket connection;      // socket to connect with
      OutputStream out;       // output stream of bytes
      InputStream in;         // input stream of bytes
      char c;                 // used for parsing the header
      
		try                                       // try to send the message
      {
		   connection = new Socket(user.getHost(), PUBLIC_PORT);
			out = connection.getOutputStream();
			in = connection.getInputStream();

			m = PROTOCOL_FROM + ":" + me.getUsername();
			out.write(m.getBytes());
			out.write(0x0);
			out.write(message.getBytes());
			out.write(0x0);
			out.flush();
			header = "";

			while((c = (char) in.read()) != 0x0)   // read in header
         {
				header += c;
			}
         
			if(header.equals(PROTOCOL_RECIEVED))   // if header == "GotIt", return true
         {
				return true;
			}
         
			out.close();
			in.close();
         connection.close();
		}
      
		catch(Exception e) {}
		return false;                            // else return false
	}
   
   
   public String getMyIP()
   //POST: FCTVAL == myIP
   {
      return myIP;
   }
}
