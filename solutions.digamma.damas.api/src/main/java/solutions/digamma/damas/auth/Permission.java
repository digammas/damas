package solutions.digamma.damas.auth;

import solutions.digamma.damas.common.WorkspaceException;
import solutions.digamma.damas.content.File;
import solutions.digamma.damas.entity.Entity;

import java.util.EnumSet;

/**
 * Access control entity. A permission defines the set of privileges assigned to
 * a given subject on a given object.
 *
 * @author Ahmad Shahwan
 */
public interface Permission extends Entity {

    EnumSet<AccessRight> getAccessRights() throws WorkspaceException;

    void setAccessRights(EnumSet<AccessRight> value) throws WorkspaceException;

    String getSubjectId() throws WorkspaceException;

    File getObject() throws WorkspaceException;

    default String getObjectId() throws WorkspaceException {
        return getObject() == null ? null : getObject().getId();
    }
}
