package solutions.digamma.damas.content;

import solutions.digamma.damas.entity.CrudManager;
import solutions.digamma.damas.entity.SearchEngine;

/**
 * Folder manager.
 *
 * @author Ahmad Shahwan
 */
public interface FolderManager extends
        CrudManager<Folder>, SearchEngine<Folder>, PathFinder<Folder> {
}
