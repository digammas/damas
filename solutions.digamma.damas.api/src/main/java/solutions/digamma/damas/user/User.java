package solutions.digamma.damas.user;


import java.util.List;

/**
 * User entity.
 *
 * @author Ahmad Shahwan
 */
public interface User extends Subject {

    String getLogin();

    /**
     * User's email address.
     *
     * @return                      email address
     */
    String getEmailAddress();

    /**
     * Set user's email address.
     *
     * @param value                 new email address.
     */
    void setEmailAddress(String value);

    /**
     * User's first name.
     *
     * @return                      first name
     */
    String getFirstName();

    /**
     * Set user's last name.
     *
     * @param value                 first name
     */
    void setFirstName(String value);

    /**
     * User's last name.
     *
     * @return                      last name
     */
    String getLastName();

    /**
     * Set user's last name.
     *
     * @param value
     */
    void setLastName(String value);

    /**
     * List all group names to which the user belongs.
     *
     * @return                      list of group names
     */
    List<String> getMemberships();
}
