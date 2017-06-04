package solutions.digamma.damas.content;

import solutions.digamma.damas.CrudManager;
import solutions.digamma.damas.PathFinder;
import solutions.digamma.damas.SearchEngine;

/**
 * Folder manager.
 *
 * @author Ahmad Shahwan
 */
public interface FolderManager extends
        CrudManager<Folder>, SearchEngine<Folder>, PathFinder<Folder> {
}
