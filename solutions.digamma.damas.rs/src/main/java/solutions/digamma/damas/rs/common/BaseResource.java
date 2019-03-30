package solutions.digamma.damas.rs.common;

import solutions.digamma.damas.login.Token;

import javax.ws.rs.HeaderParam;

/**
 * Generic REST resource.
 *
 * @author Ahmad Shahwan
 */
public abstract class BaseResource {

    @HeaderParam("Authorization")
    protected String authHeader;

    private static final String SCHEME = "bearer ";

    /**
     * Constructor.
     */
    public BaseResource() {
    }

    /**
     * Retrieve authentication token from request. If request holds no token,
     * return {@code null}.
     *
     * @return AuthResource token if any, {@code null} otherwise.
     */
    protected Token getToken() {
        if (authHeader != null) {
            authHeader = this.authHeader.trim();
            if (authHeader.toLowerCase().startsWith(SCHEME)){
                String token = authHeader.substring(SCHEME.length()).trim();
                if (token.length() != 0) {
                    return new AuthenticationToken(token);
                }
            }
        }
        return new AuthenticationToken();
    }
}
