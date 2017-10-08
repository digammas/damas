package solutions.digamma.damas.jcr.session;

import solutions.digamma.damas.auth.Token;
import solutions.digamma.damas.inspection.NotNull;

import java.math.BigInteger;
import java.security.SecureRandom;

/**
 * Statistically secure token.
 *
 * @author Ahmad Shahwan
 */
public class SecureToken implements Token {

    @NotNull
    private String token;

    private static SecureRandom random = new SecureRandom();

    /**
     * Constructor.
     */
    public SecureToken() {
        this.token = SecureToken.nextToken();
    }

    @NotNull
    public String getSecret() {
        return this.token;
    }

    private static String nextToken() {
        return new BigInteger(132, random).toString(64);
    }

    @Override
    public boolean equals(Object other) {
        /* Two token with no secret do not equal each other. */
        return
            other instanceof Token &&
            this.getSecret() != null &&
            this.getSecret().equals(((Token) other).getSecret());
    }
}
