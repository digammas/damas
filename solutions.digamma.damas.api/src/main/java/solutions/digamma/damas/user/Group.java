package solutions.digamma.damas.user;

/**
 * A group of users.
 *
 * @author Ahmad Shahwan
 */
public interface Group extends Subject {

    /**
     * Group's unique name. Group name is immutable and constitutes its
     * identifier.
     *
     * @return                      group's name
     */
    String getName();

    /**
     * Group's descriptive label.
     *
     * @return                      group's label
     */
    String getLabel();

    /**
     * Set group's label.
     *
     * @param value                 group's new label
     */
    void setLabel(String value);
}
