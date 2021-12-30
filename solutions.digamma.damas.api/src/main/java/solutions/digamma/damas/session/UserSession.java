package solutions.digamma.damas.session;

import java.util.Date;

/**
 * Information about current used session.
 *
 * An object of this type is returned when the who-am-I API is queried.
 */
public interface UserSession {

    String getUserLogin();

    Date getCreationDate();

    Date getExpirationDate();
}
