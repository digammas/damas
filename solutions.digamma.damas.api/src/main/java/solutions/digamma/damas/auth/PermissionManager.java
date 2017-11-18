package solutions.digamma.damas.auth;

import solutions.digamma.damas.common.WorkspaceException;
import solutions.digamma.damas.entity.CrudManager;
import solutions.digamma.damas.login.Token;

import java.util.EnumSet;
import java.util.List;

/**
 * Access control manager. This service is used to add, remove and modify
 * permission assignments to files.
 *
 * @author Ahmad Shahwan
 */
public interface PermissionManager {

    /**
     * Retrieve a permission applied at a given file, for a given subject. If no
     * such permission exists, this method returns {@code null}.
     *
     * @param token         access token
     * @param objectId        file id
     * @param subjectId     subject id
     * @return              permission applied at file for subject, or {@code
     *                      null} if no such permission
     * @throws WorkspaceException
     */
    Permission retrieve(Token token, String objectId, String subjectId)
            throws WorkspaceException;

    /**
     * Retrieve all permissions applied to a given file.
     *
     * @param token         access token
     * @param objectId        file id
     * @return              a list of all permissions at that file
     * @throws WorkspaceException
     */
    List<Permission> retrieve(Token token, String objectId)
            throws WorkspaceException;

    /**
     * Update permission at a given file for a given subject.
     * Beside updating access rights, this method has the side effect of giving
     * the updated permission the highest priority at the file on which it
     * applies. To reorganise priorities, use method {@code updateAll()}.
     *
     * File ID and Subject ID of the passed pattern identify the permission to
     * be updated. Permission is updated with access right set. If permission
     * does not exist, it will be created.
     *
     * If the set of access rights passed throw the parameter {@code entity} is
     * {@code null}, the method does nothing.
     *
     * @param token         access token
     * @param pattern       modification patern
     * @return              newly updated, may be created, permission
     * @throws WorkspaceException
     */
    Permission update(Token token, Permission pattern)
            throws WorkspaceException;

    /**
     * Update permissions at a given file.
     * Calling this method has the effect of calling {@code update()} several
     * times, in reverse order of the passed list. It does thus reorder
     * priorities putting the first element at the highest priority.
     *
     * Apart from lowering their priorities, this methods doesn't change
     * existing permission at file that are not mentioned in the list.
     *
     * @param token
     * @param fileId
     * @param permissions
     * @throws WorkspaceException
     */
    void update(Token token, String fileId, List<Permission> permissions)
            throws WorkspaceException;

    /**
     * Remove permission object at a given file, for a given subject.
     *
     * @param token         access token
     * @param objectId      file ID
     * @param subjectId     subject ID
     * @throws WorkspaceException
     */
    void delete(Token token, String objectId, String subjectId)
            throws WorkspaceException;
}
