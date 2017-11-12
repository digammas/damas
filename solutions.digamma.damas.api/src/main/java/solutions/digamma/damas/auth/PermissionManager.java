package solutions.digamma.damas.auth;

import solutions.digamma.damas.common.WorkspaceException;
import solutions.digamma.damas.entity.CrudManager;
import solutions.digamma.damas.login.Token;

import java.util.List;

/**
 * Access control manager. This service is used to add, remove and modify
 * permission assignments to files.
 *
 * @author Ahmad Shahwan
 */
public interface PermissionManager extends CrudManager<Permission> {

    /**
     * Retrieve a permission applied at a given file, for a given subject. If no
     * such permission exists, this method returns {@code null}.
     *
     * @param token         access token
     * @param fileId        file id
     * @param subjectId     subject id
     * @return              permission applied at file for subject, or {@code
     *                      null} if no such permission
     * @throws WorkspaceException
     */
    Permission retrieve(Token token, String fileId, String subjectId)
            throws WorkspaceException;

    /**
     * Retrieve all permissions applied to a given file.
     *
     * @param token         access token
     * @param fileId        file id
     * @return              a list of all permissions at that file
     * @throws WorkspaceException
     */
    List<Permission> retrieveAt(Token token, String fileId)
            throws WorkspaceException;

    /**
     * Update access rights of a given permission.
     * Beside updating access rights, this method has the side effect of giving
     * the updated permission the highest priority at the file on which it
     * applies. To reorganise priorities, use method {@code updateAll()}.
     *
     * If the set of access rights passed throw the parameter {@code entity} is
     * {@code null}, the method does nothing.
     *
     * @param token
     * @param id                    ID of entity to be updated.
     * @param entity
     * @return
     * @throws WorkspaceException
     */
    Permission update(Token token, String id, Permission entity)
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
    void updateAt(Token token, String fileId, List<Permission> permissions)
            throws WorkspaceException;
}
