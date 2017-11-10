package solutions.digamma.damas.auth;

import solutions.digamma.damas.common.WorkspaceException;
import solutions.digamma.damas.content.File;
import solutions.digamma.damas.entity.Created;
import solutions.digamma.damas.entity.Entity;
import solutions.digamma.damas.entity.Modifiable;
import solutions.digamma.damas.user.Subject;

import java.util.EnumSet;

/**
 * Access control entity. A permission defines the set of privileges assigned to
 * a given subject on a given object.
 *
 * @author Ahmad Shahwan
 */
public interface Permission extends Entity, Created, Modifiable {

    EnumSet<Privilege> getPrivileges() throws WorkspaceException;

    void setPrivileges(EnumSet<Privilege> value) throws WorkspaceException;

    Subject getSubject() throws WorkspaceException;

    default void setSubject(Subject value) throws WorkspaceException {
        setSubjectId(value == null ? null : value.getId());
    }

    default String getSubjectId() throws WorkspaceException {
        return getSubject() == null ? null : getSubject().getId();
    }

    void setSubjectId(String value) throws WorkspaceException;

    File getObject() throws WorkspaceException;

    default void setObject(File value) throws WorkspaceException {
        setObjectId(value == null ? null : value.getId());
    }

    default String getObjectId() throws WorkspaceException {
        return getObject() == null ? null : getSubject().getId();
    }

    void setObjectId(String value) throws WorkspaceException;
}
