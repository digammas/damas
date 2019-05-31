package solutions.digamma.damas.user;

import solutions.digamma.damas.entity.CrudManager;
import solutions.digamma.damas.search.Filter;
import solutions.digamma.damas.search.SearchEngine;

/**
 * Group management service.
 *
 * @author Ahmad Shahwan
 */
public interface GroupManager
        extends CrudManager<Group>, SearchEngine<Group, Filter> {
}
