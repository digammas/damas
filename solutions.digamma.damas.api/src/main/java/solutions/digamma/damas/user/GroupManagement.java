package solutions.digamma.damas.user;

import solutions.digamma.damas.CrudManager;
import solutions.digamma.damas.SearchEngine;

/**
 * Group management service.
 *
 * @author Ahmad Shahwan
 */
public interface GroupManagement
        extends CrudManager<Group>, SearchEngine<Group> {
}
