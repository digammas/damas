package solutions.digamma.damas.auth;

import solutions.digamma.damas.inspection.NotNull;

/**
 * User session token.
 *
 * @author Ahmad Shahwan
 */
public interface Token {

    @NotNull String getSecret();
}
