package solutions.digamma.damas.user;

/**
 * A group of users.
 *
 * @author Ahmad Shahwan
 */
public interface Group extends Subject {

    /**
     * Group's unique name. Group name is mutable and different form its
     * identifier.
     *
     * @return                      group's name
     */
    String getName();

    /**
     * Set group's name.
     *
     * @param value                 group's new name
     */
    void setName(String value);
}
