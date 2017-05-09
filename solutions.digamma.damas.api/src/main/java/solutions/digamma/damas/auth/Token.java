package solutions.digamma.damas.auth;

import solutions.digamma.damas.inspection.Nonnull;

/**
 * User session token.
 *
 * @author Ahmad Shahwan
 */
public interface Token {

    @Nonnull String getSecret();
}
