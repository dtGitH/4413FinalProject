package model;

public class User {
	private int id;
	private String uuid;
	private String username;
	private String email;
	private String password;
	private String role; // New role field

	// Constructor for new users (without ID)
	public User(String uuid, String username, String email, String password, String role) {
		this.uuid = uuid;
		this.username = username;
		this.email = email;
		this.password = password;
		this.role = role;
	}

	// Constructor with ID (for existing users)
	public User(int id, String uuid, String username, String email, String password, String role) {
		this.id = id;
		this.uuid = uuid;
		this.username = username;
		this.email = email;
		this.password = password;
		this.role = role;
	}

	// Getters and Setters
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}
}
