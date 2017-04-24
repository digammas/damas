package solutions.digamma.damas.rs;

import solutions.digamma.damas.auth.Token;

import java.io.Serializable;

/**
 * Authentication object.
 *
 * @author Ahmad Shahwan
 */
public class Authentication implements Serializable, Token {

    private String token;

    /**
     * Default constructor.
     */
    public Authentication() {
    }

    /**
     * Constructor.
     *
     * @param token Authentication token.
     */
    public Authentication(String token) {
        this.token = token;
    }

    /**
     * Authentication token.
     *
     * @return
     */
    public String getToken() {
        return token;
    }

    /**
     * Set authentication token.
     *
     * @param token
     */
    public void setToken(String token) {
        this.token = token;
    }
}
