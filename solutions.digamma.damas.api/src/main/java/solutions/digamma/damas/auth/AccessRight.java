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
    /**
     * Access right allowing its owner to read content.
     */
    READ,
    /**
     * Access right allowing its owner to read and modify content.
     * <p/>
     * This access right include {@link AccessRight#READ}.
     */
    WRITE,
    /**
     * Access right allowing its owner to read, modify and maintain items.
     * Maintenance include privilege such as access to access right information,
     * sharing and locking.
     *
     * <p/>
     * This access right include {@link AccessRight#READ} and {@link
     * AccessRight#WRITE}.
     */
    MAINTAIN;

    /**
     * None of these.
     *
     * @return empty enum set
     */
    public static EnumSet<AccessRight> none() {
        return EnumSet.noneOf(AccessRight.class);
    }
}
