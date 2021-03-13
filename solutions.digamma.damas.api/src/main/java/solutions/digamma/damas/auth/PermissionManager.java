package solutions.digamma.damas.auth;

import solutions.digamma.damas.common.WorkspaceException;

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
     * @param objectId        file id
     * @param subjectId     subject id
     * @return              permission applied at file for subject, or {@code
     *                      null} if no such permission
     * @throws WorkspaceException
     */
    Permission retrieve(String objectId, String subjectId)
            throws WorkspaceException;

    /**
     * Retrieve all permissions applied to a given file.
     *
     * @param objectId        file id
     * @return              a list of all permissions at that file
     * @throws WorkspaceException
     */
    List<Permission> retrieve(String objectId)
            throws WorkspaceException;

    /**
     * Update permission at a given file for a given subject.
     * <p/>
     * File ID and Subject ID of the passed pattern identify the permission to
     * be updated. Permission is updated with access right set. If permission
     * does not exist, it will be created.
     * <p/>
     * If the set of access rights passed throw the parameter {@code entity} is
     * {@code null}, the method does nothing.
     *
     * @param pattern       modification patern
     * @return              newly updated, may be created, permission
     * @throws WorkspaceException
     */
    Permission update(Permission pattern)
            throws WorkspaceException;

    /**
     * Update permissions at a given file.
     *
     * @param fileId        file ID
     * @param permissions   permission list
     * @throws WorkspaceException
     */
    void update(String fileId, List<Permission> permissions)
            throws WorkspaceException;

    /**
     * Update permissions at a given file, allowing recursion.
     *
     * If the file is a folder, and {@code recursive} is {@code true} update
     * is propagated to all sub-folder and documents.
     *
     * @param fileId        file ID
     * @param permissions   permission list
     * @param recursive     whether to update sub-folders and documents
     * @throws WorkspaceException
     */
    void update(String fileId, List<Permission> permissions, boolean recursive)
            throws WorkspaceException;

    /**
     * Remove permission object at a given file, for a given subject.
     *
     * @param objectId      file ID
     * @param subjectId     subject ID
     * @throws WorkspaceException
     */
    void delete(String objectId, String subjectId)
            throws WorkspaceException;
}
