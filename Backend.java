import java.net.*;
import java.io.*;
import java.lang.Runnable;
import java.lang.Thread;
import java.util.ArrayList;
import java.util.Scanner;

public class Backend {
	private User me;
	private ArrayList<User> users;
	private Thread incomingListener;
	private final int PUBLIC_PORT = 7007;
	private final String PROTOCOL_WHOIS = "WhoIs";
	private final String PROTOCOL_FROM = "MessageFrom";
	private final String PROTOCOL_RECIEVED = "GotIt";

	public static void main(String[] args) {
		new Backend();
	}

	public Backend() {
		setupThread();
		incomingListener.start();

		try {
			InetAddress myAddress = InetAddress.getLocalHost();
			System.out.println("My address: " + myAddress.getHostAddress());
			me = new User("soyfestivo", myAddress.getHostAddress());
		}
		catch(Exception e) {
			System.err.println("Error Getting my own IP");
			System.exit(0);
		}

		scanLAN(16);
		//sendMessage(new User("soyfestivo", "192.168.0.111"), "Hello OMG so cool it worked!!!");

		
		Thread console = new Thread() {
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

		console.start();

	}

	private class MiniScan extends Thread {
		private int id;
		private int min;
		private int max;

		public MiniScan(int min, int max) {
			super();

			this.min = min;
			this.max = max;
			start();
		}

		@Override
		public void run() {
			scanRange(min, max);
		}
	}

	public void scanRange(int min, int max) {
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

	public void scanLAN(int split) {
		users = new ArrayList<User>();
		int groupSize = 256 / split;
		for(int i = 0; i < split; i++) {
			new MiniScan(groupSize * i, (groupSize * i) + groupSize - 1);
		}
	}

	private User handshake(String host) {
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
			while((c = (char) in.read()) != 0x0) {
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

	private void setupThread() {
		incomingListener = new Thread() {
			public void run() {
				ServerSocket openSocket = null;
				try {
					openSocket = new ServerSocket(PUBLIC_PORT);
				}
				catch(Exception e) {
					System.err.println("Cannot bind to port %d".format(""+PUBLIC_PORT));
					System.exit(0);
				}

				while(true) {
					try {
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

	private void handelIncomingRequest(InputStream in, OutputStream out) {
		String header = "";
		String message = "";
		char c;

		try {
			// read in header
			while((c = (char) in.read()) != 0x0) {
				header += c;
			}
			if(header.equals(PROTOCOL_WHOIS)) {
				out.write(me.getUsername().getBytes());
				out.write(0x0);
				out.flush();
			}
			else if(header.indexOf(PROTOCOL_FROM + ":") != -1) {
				String[] parse = header.split(":");
				// read message
				while((c = (char) in.read()) != 0x0) {
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

	private void messageNotify(String username, String message) {
		System.out.println("@" + username + ": " + message);
	}

	public boolean sendMessage(User user, String message) {
		try {
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
			while((c = (char) in.read()) != 0x0) {
				header += c;
			}
			if(header.equals(PROTOCOL_RECIEVED)) {
				return true;
			}
			out.close();
			in.close();
			connection.close();
		}
		catch(Exception e) {}
		return false;
	}
}