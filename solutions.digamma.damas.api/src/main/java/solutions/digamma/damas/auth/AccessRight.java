package solutions.digamma.damas.auth;

import java.util.EnumSet;

/**
 * Access privilege.
 * An access privilege can be granted to a given subject, on given objects.
 * Subjects are users and groups. Objects can be a document or a folder.
 *
 * @author Ahmad Shahwan
 */
public enum AccessRight {
    READ,
    WRITE,
    SHARE;

    /**
     * None of these.
     *
     * @return empty enum set
     */
    public static EnumSet<AccessRight> none() {
        return EnumSet.noneOf(AccessRight.class);
    }

    /**
     * All of these.
     *
     * @return all access rights
     */
    public static EnumSet<AccessRight> all() {
        return EnumSet.allOf(AccessRight.class);
    }
}
