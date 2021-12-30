package solutions.digamma.damas.session;

/**
 * A used session information along side a session token.
 *
 * An object of this type is returned upon successful authentication. Both
 * session information and token are usually needed for actions following once
 * user is logged in.
 */
public interface UserToken extends UserSession, Token {
}
