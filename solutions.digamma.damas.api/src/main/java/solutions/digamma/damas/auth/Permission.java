package solutions.digamma.damas.auth;

import solutions.digamma.damas.content.File;

/**
 * Access control entity. A permission defines the set of privileges assigned to
 * a given subject on a given object.
 *
 * @author Ahmad Shahwan
 */
public interface Permission {

    /**
     * Set of access rights applied by this permission entry.
     *
     * @return
     */
    AccessRight getAccessRights();

    /**
     * Set access rights applied by this permission entry.
     *
     * @param value
     */
    void setAccessRights(AccessRight value);

    /**
     * Subject ID.
     *
     * @return
     */
    String getSubjectId();

    /**
     * Object, or file, ID.
     *
     * @return
     */
    default String getObjectId() {
        return getObject() == null ? null : getObject().getId();
    }

    /**
     * Object on which this permission applies.
     *
     * @return
     */
    File getObject();
}
