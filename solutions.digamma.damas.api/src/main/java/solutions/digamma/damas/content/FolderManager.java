package solutions.digamma.damas.content;

import solutions.digamma.damas.common.WorkspaceException;
import solutions.digamma.damas.entity.CrudManager;
import solutions.digamma.damas.search.Filter;
import solutions.digamma.damas.search.SearchEngine;

/**
 * Folder manager.
 *
 * @author Ahmad Shahwan
 */
public interface FolderManager extends
        CrudManager<Folder>, SearchEngine<Folder, Filter>, PathFinder<Folder> {

    /**
     * Copy a folder to a destination folder.
     *
     * @param sourceId          source folder's ID
     * @param destinationId     destination folder's ID
     * @return                  new created folder
     * @throws WorkspaceException
     */
    Folder copy(String sourceId, String destinationId)
            throws WorkspaceException;
}
