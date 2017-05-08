package solutions.digamma.damas.rs.auth;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 * Credentials object.
 *
 * @author Ahmad Shahwan
 */
@XmlRootElement
public class Credentials implements Serializable {

    private String username;
    private String password;

    /**
     * No-args constructor.
     */
    public Credentials() {
    }

    /**
     * Short-hand constructor.
     *
     * @param username Username.
     * @param password Password.
     */
    public Credentials(String username, String password) {
        this.username = username;
        this.password = password;
    }

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
