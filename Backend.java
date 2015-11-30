// Programmers:  Stephen Selke, Chris Griffith, Karen Bykowski
// Assignment:   Project 4 - Messenger App
// Date:         December 3, 2015
// Description:  Backend for the messenger application
//                 

import java.net.*;
import java.io.*;
import java.lang.Runnable;
import java.lang.Thread;
import java.util.ArrayList;
import java.util.Scanner;
import javax.swing.JOptionPane;

public class Backend {
	private final int PUBLIC_PORT = 7007;                 //port to connect to
	private final String PROTOCOL_WHOIS = "WhoIs";        //
	private final String PROTOCOL_FROM = "MessageFrom";   //
	private final String PROTOCOL_RECIEVED = "GotIt";     //
	private User me;                                      //current User
	private ArrayList<User> users;                        //list of all people current User can chat with
	private Thread incomingListener;                      //
   private String myIP;                                  //current User's IP address
   private String myUsername;                            //current User's username
   private String inMessage;                             //incoming message
   private String inUser;                                //User inMessage is from
   private boolean msgRcvd;                              //a new incoming message has been received


	/*public static void main(String[] args) {
   //PRE:
   //POST:
		new Backend();
	}*/
   
   private void setMyUsername()
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
   //POST: FCTVAL == myUsername
   {
      return myUsername;
   }

	public Backend() 
   //PRE:
   //POST:
   {
		setupThread();
		incomingListener.start();
		users = new ArrayList<User>();

		try 
      {
         setMyUsername();
			InetAddress myAddress = InetAddress.getLocalHost();
         myIP = myAddress.getHostAddress();
			//System.out.println("My address: " + myAddress.getHostAddress());
         System.out.println("My address: " + myIP);
			//me = new User("soyfestivo", myAddress.getHostAddress());
         me = new User(myUsername, myAddress.getHostAddress());
		}
		catch(Exception e) 
      {
			System.err.println("Error Getting my own IP");
			System.exit(0);
		}

		scanLAN(16);
		//sendMessage(new User("soyfestivo", "192.168.0.111"), "Hello OMG so cool it worked!!!");

		
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

	public ArrayList<User> getUsers() 
   //PRE:
   //POST:
   {
		return users;
	}

   //Description: 
	private class MiniScan extends Thread 
   {
		private int id;
		private int min;
		private int max;

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
		String[] myIPArr = me.getHost().split("\\.");
		String prefix = myIPArr[0] + "." + myIPArr[1] + "." + myIPArr[2] + ".";
		for(int subAddress = min; subAddress <= max; subAddress++) {
			if(!me.getHost().equals(prefix + subAddress)) { // don't add yourself
				User u = handshake(prefix + subAddress);
				if(u != null) {
					System.out.println("Reply: " + u.getUsername() + "@" + u.getHost());
					users.add(u);
				}
			}
		}
	}

	public User addStaticUser(String host) 
   //PRE:
   //POST:
   {
   		User u = handshake(host);
   		if(u != null) {
   			users.add(u);
   		}
   		return u;
	}

	public void scanLAN(int split) 
   //PRE:
   //POST:
   {
		int groupSize = 256 / split;
		for(int i = 0; i < split; i++) 
      {
			new MiniScan(groupSize * i, (groupSize * i) + groupSize - 1);
		}
	}

	private User handshake(String host) 
   //PRE:
   //POST:
   {
		//System.out.println("handshake " + host);
		try {
			Socket connection = new Socket();
			connection.connect(new InetSocketAddress(host, PUBLIC_PORT), 1000);
			OutputStream out = connection.getOutputStream();
			InputStream in = connection.getInputStream();

			out.write(PROTOCOL_WHOIS.getBytes());
			out.write(0x0);
			out.flush();
			String username = "";
			char c;

			// read response
			while((c = (char) in.read()) != 0x0) 
         {
				username += c;
			}
			out.close();
			in.close();
			connection.close();
			return new User(username, host);
		}
		catch(Exception e) {}
		return null;
	}

	private void setupThread() 
   //PRE:
   //POST:
   {
		incomingListener = new Thread() {
			public void run() 
         //PRE:
         //POST:
         {
				ServerSocket openSocket = null;
				try 
            {
					openSocket = new ServerSocket(PUBLIC_PORT);
				}
				catch(Exception e) {
					System.err.println("Cannot bind to port %d".format(""+PUBLIC_PORT));
					System.exit(0);
				}

				while(true) 
            {
					try 
               {
						Socket connection = openSocket.accept();
						InputStream in = connection.getInputStream();
						OutputStream out = connection.getOutputStream();
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

		try {
			// read in header
			while((c = (char) in.read()) != 0x0) 
         {
				header += c;
			}
			if(header.equals(PROTOCOL_WHOIS)) 
         {
				out.write(me.getUsername().getBytes());
				out.write(0x0);
				out.flush();
			}
			else if(header.indexOf(PROTOCOL_FROM + ":") != -1) 
         {
				String[] parse = header.split(":");
				// read message
				while((c = (char) in.read()) != 0x0) 
            {
					message += c;
				}
            
            inUser = parse[1];
            inMessage = message;
            
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
      msgRcvd = true;
		System.out.println("@" + username + ": " + message);
	}

	public boolean sendMessage(User user, String message) 
   //PRE:
   //POST:
   {
		try 
      {
			Socket connection = new Socket(user.getHost(), PUBLIC_PORT);
			OutputStream out = connection.getOutputStream();
			InputStream in = connection.getInputStream();

			String m = PROTOCOL_FROM + ":" + me.getUsername();
			out.write(m.getBytes());
			out.write(0x0);
			out.write(message.getBytes());
			out.write(0x0);
			out.flush();
			String header = "";
			char c;

			// read in header
			while((c = (char) in.read()) != 0x0) 
         {
				header += c;
			}
			if(header.equals(PROTOCOL_RECIEVED)) 
         {
				return true;
			}
			out.close();
			in.close();
			connection.close();
		}
		catch(Exception e) {}
		return false;
	}
   
   public String getMyIP()
   //POST: FCTVAL == myIP
   {
      return myIP;
   }
   
   public boolean isMsgRcvd()
   //POST: FCTVAL == msgRcvd
   {
      return msgRcvd;
   }
   
   public String getInUser()
   //POST: FCTVAL == inUser
   {
      return inUser;
   }
   
   public String getInMessage()
   //POST: FCTVAL == inMessage
   {
      msgRcvd = false;
      return inMessage;
   }
}