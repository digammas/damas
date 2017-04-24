package solutions.digamma.damas.rs.auth;

import java.io.Serializable;

/**
 * Credentials object.
 *
 * @author Ahmad Shahwan
 */
public class Credentials implements Serializable {

    private String username;
    private String password;

    /**
     * Username.
     *
     * @return
     */
    public String getUsername() {
        return username;
    }

    /**
     * Set username.
     *
     * @param username Username.
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * User password.
     *
     * @return
     */
    public String getPassword() {
        return password;
    }

    /**
     * Set user password.
     *
     * @param password
     */
    public void setPassword(String password) {
        this.password = password;
    }
}
