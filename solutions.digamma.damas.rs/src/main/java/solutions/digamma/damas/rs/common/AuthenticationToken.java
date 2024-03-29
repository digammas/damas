package solutions.digamma.damas.rs.common;

import solutions.digamma.damas.session.Token;

import java.io.Serializable;

/**
 * Authentication object.
 *
 * @author Ahmad Shahwan
 */
public class AuthenticationToken implements Serializable, Token {

    private String secret;

    /**
     * Default constructor.
     */
    public AuthenticationToken() {
    }

    /**
     * Constructor.
     *
     * @param token Authentication secret.
     */
    public AuthenticationToken(String token) {
        this.secret = token;
    }

    /**
     * Authentication secret.
     *
     * @return
     */
    @Override
    public String getSecret() {
        return secret;
    }

    /**
     * Set authentication secret.
     *
     * @param secret
     */
    public void setSecret(String secret) {
        this.secret = secret;
    }
}
