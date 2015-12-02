// Programmers:  Stephen Selke, Chris Griffith, Karen Bykowski
// Assignment:   Project 4 - Messenger App
// Date:         December 3, 2015
// Description:  Backend for the messenger application

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
   private UIWindow window;                              // front end connection


	public Backend(UIWindow window) 
   //PRE:  window is initialized
   //POST: constructs an instance of the backend class 
   {  ////
      InetAddress[] n;                          // 
      Enumeration<NetworkInterface> addresses;  // 
      NetworkInterface ni;                      //
      Enumeration<InetAddress> e;               //
      InetAddress ia;                           //
      InetAddress myAddress;                    //Current user's IP address
   
      setupThread();
      incomingListener.start();
      users = new ArrayList<User>();
      
      setMyUsername();          //get the user's desired username

      this.window = window;

      try 
      { 

      	////////////// testing for chris
         n = InetAddress.getAllByName("google.com");

      	//System.out.println("Address: " + n.toString());
         for(InetAddress iiiiii : n) 
         {
          	 	//System.out.println("~  " + iiiiii.getLocalHost().getHostAddress());
         }
          
         addresses = NetworkInterface.getNetworkInterfaces();

         while(addresses.hasMoreElements()                 // get the addresses
              && (ni = addresses.nextElement()) != null) 
         {
            e = ni.getInetAddresses();
          	 
          	 //System.out.println("Address: " + ni.toString());
          	 while(e.hasMoreElements() && (ia = e.nextElement()) != null) 
             {
          	 	//System.out.println("  " + ia.getLocalHost().getHostAddress());
          	 }
          	
         }
        ////////////// testing for chris
          
         myAddress = InetAddress.getLocalHost();
         myIP = myAddress.getHostAddress();
			
        //System.out.println("My address: " + myAddress.getHostAddress());
        System.out.println("My address: " + myIP);   /////////////////////REMOVE when complete; for debugging
	    //me = new User("soyfestivo", myAddress.getHostAddress());
          
         me = new User(myUsername, myAddress.getHostAddress(), this);
      }
      catch(Exception ex) 
      {
         System.err.println("Error Getting my own IP" + ex.toString());
         System.exit(0);
      }

      scanLAN(16);

		/*Thread console = new Thread() {
			public void run() {
				Scanner scanner = new Scanner(System.in);
				boolean foundCommand;
				boolean running = true;
				try {
					while(running) {
						String input = scanner.nextLine();

						if(input.indexOf("exit") == 0 || input.equals("disconnect")) {
							System.exit(0);
						}
						if(input.indexOf("send") != -1) {
							String[] a = input.split(" ");
							sendMessage(new User(a[1], a[2]), a[3]); // send soyfestivo 192.168.0.111 HELLO!!!!!
						}
					}
				}
				catch(Exception e) {

				}
			}
		};

		console.start();*/
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
		private int id;      //
		private int min;     //
		private int max;     //

		public MiniScan(int min, int max) 
        //PRE:
        //POST:
        {
			super();

			this.min = min;
			this.max = max;
			start();
		}

		@Override
		public void run() 
        //PRE:
        //POST:
        {
			scanRange(min, max);
		}
	}

	public void scanRange(int min, int max) 
    //PRE:
    //POST:
    {
      User u;            // User the current user is trying to connect with
      String[] myIPArr;  //
      String prefix;     //   
      
		myIPArr = me.getHost().split("\\.");
		prefix = myIPArr[0] + "." + myIPArr[1] + "." + myIPArr[2] + ".";
		for(int subAddress = min; subAddress <= max; subAddress++) 
      {
			if(!me.getHost().equals(prefix + subAddress)) // don't add yourself
         { 
				u = handshake(prefix + subAddress);
				if(u != null) 
            {
					System.out.println("Reply: " + u.getUsername() + "@" + u.getHost());
					addUser(u);
				}
			}
		}
	}

	private void addUser(User u) 
   //PRE:
   //POST:
   {
		for(User user : users) 
      {
			if(user.getHost().equals(u.getHost())) 
         {
				users.remove(user);
				users.add(u);
				return;
			}
		}
		users.add(u);
	}

	public User addStaticUser(String host) 
   //PRE: host is a valid 
   //POST: FCTVAL == u; the User you are connecting with
   {
      User u;     // User the current user is trying to connect with
         
      u = handshake(host);
      if(u != null)     // If you can connect with u
      {
         addUser(u);  // Add them to users array
      }
      return u;
   }


	public void scanLAN(int split) 
   //PRE:
   //POST:
   {
      int groupSize;    // 
      
      groupSize = 256 / split;

      for(int i = 0; i < split; i++) 
      {
         new MiniScan(groupSize * i, (groupSize * i) + groupSize - 1);
      }
   }


	private User handshake(String host) 
   //PRE:
   //POST:
   {
      String username;
      char c;
   
		//System.out.println("handshake " + host);
      try 
      {
         Socket connection = new Socket();
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
   //PRE:
   //POST:
   {
      incomingListener = new Thread() 
      {
   		public void run() 
         //PRE:
         //POST:
         {
            ServerSocket openSocket;   //
            Socket connection;         //
            InputStream in;            //
            OutputStream out;          //
         
				openSocket = null;
				try 
            {
					openSocket = new ServerSocket(PUBLIC_PORT);
				}
	
   			catch(Exception e) 
            {
					System.err.println("Cannot bind to port %d".format(""+PUBLIC_PORT));
					System.exit(0);
				}

				while(true) 
            {
				   try 
               {
				   	connection = openSocket.accept();
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
   //PRE:
   //POST:
   {
      String header = "";
		String message = "";
		char c;

		try 
      {
			while((c = (char) in.read()) != 0x0)      // read in header
         {
		   	header += c;
			}
         
			if(header.equals(PROTOCOL_WHOIS))         //
         {
				out.write(me.getUsername().getBytes());
				out.write(0x0);
				out.flush();
			}
         
			else if(header.indexOf(PROTOCOL_FROM + ":") != -1) 
         {
				String[] parse;                          // 
            
            parse = header.split(":");
				while((c = (char) in.read()) != 0x0)     // read message 
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
   //PRE:
   //POST:
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
		System.out.println("@" + username + ": " + message); //////////////// REMOVE when complese
	}


	public boolean sendMessage(User user, String message) 
   //PRE:
   //POST:
   {
      String header;          // header of the incoming packet
      String m;               // 
      Socket connection;      //
      OutputStream out;       //
      InputStream in;         //
      char c;                 //used for parsing the header
      
		try 
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
