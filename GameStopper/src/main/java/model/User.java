package model;

/**
 * Represents a user in the system. The user can have a unique UUID, username, email,
 * password, and role. This class is used for managing user-related information.
 */
public class User {
    private int id; // The unique ID of the user in the database
    private String uuid; // The unique UUID of the user
    private String username; // The username of the user
    private String email; // The email address of the user
    private String password; // The password for the user's account
    private String role; // The role of the user, e.g., "admin", "customer"

    /**
     * Constructs a new User without an ID, used for creating new users.
     *
     * @param uuid The UUID of the user.
     * @param username The username of the user.
     * @param email The email of the user.
     * @param password The password of the user.
     * @param role The role of the user (e.g., "admin", "customer").
     */
    public User(String uuid, String username, String email, String password, String role) {
        this.uuid = uuid;
        this.username = username;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    /**
     * Constructs an existing User with an ID.
     *
     * @param id The ID of the user.
     * @param uuid The UUID of the user.
     * @param username The username of the user.
     * @param email The email of the user.
     * @param password The password of the user.
     * @param role The role of the user (e.g., "admin", "customer").
     */
    public User(int id, String uuid, String username, String email, String password, String role) {
        this.id = id;
        this.uuid = uuid;
        this.username = username;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    /**
     * Gets the ID of the user.
     *
     * @return The ID of the user.
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the ID of the user.
     *
     * @param id The ID of the user.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Gets the UUID of the user.
     *
     * @return The UUID of the user.
     */
    public String getUuid() {
        return uuid;
    }

    /**
     * Sets the UUID of the user.
     *
     * @param uuid The UUID of the user.
     */
    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    /**
     * Gets the username of the user.
     *
     * @return The username of the user.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets the username of the user.
     *
     * @param username The username of the user.
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Gets the email of the user.
     *
     * @return The email of the user.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the email of the user.
     *
     * @param email The email of the user.
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Gets the password of the user.
     *
     * @return The password of the user.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the password of the user.
     *
     * @param password The password of the user.
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Gets the role of the user.
     *
     * @return The role of the user.
     */
    public String getRole() {
        return role;
    }

    /**
     * Sets the role of the user.
     *
     * @param role The role of the user.
     */
    public void setRole(String role) {
        this.role = role;
    }
}
