
public class User {
	private String username;
	private String host;
	private ChatPanel myPanel;

	public User(String username, String host) {
		this.username = username;
		this.host = host;
	}

	public String getUsername() {
		return username;
	}

	public String getHost() {
		return host;
	}
}