package solutions.digamma.damas.auth;

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

    EnumSet<Privilege> getPrivileges();

    void setPrivileges(EnumSet<Privilege> value);

    Subject getSubject();

    void setSubject(Subject value);

    File getObject();

    void setObject(File value);
}
