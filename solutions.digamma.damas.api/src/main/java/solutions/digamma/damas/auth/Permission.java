package solutions.digamma.damas.auth;

import solutions.digamma.damas.common.WorkspaceException;
import solutions.digamma.damas.content.File;

import java.util.EnumSet;

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
     * @throws WorkspaceException
     */
    EnumSet<AccessRight> getAccessRights() throws WorkspaceException;

    /**
     * Set access rights applied by this permission entry.
     *
     * @param value
     * @throws WorkspaceException
     */
    void setAccessRights(EnumSet<AccessRight> value) throws WorkspaceException;

    /**
     * Subject ID.
     *
     * @return
     * @throws WorkspaceException
     */
    String getSubjectId() throws WorkspaceException;

    /**
     * Object, or file, ID.
     *
     * @return
     * @throws WorkspaceException
     */
    default String getObjectId() throws WorkspaceException {
        return getObject() == null ? null : getObject().getId();
    }

    /**
     * Object on which this permission applies.
     *
     * @return
     * @throws WorkspaceException
     */
    File getObject() throws WorkspaceException;
}
