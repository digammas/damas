package solutions.digamma.damas.user;

import solutions.digamma.damas.entity.CrudManager;
import solutions.digamma.damas.entity.SearchEngine;

/**
 * Group management service.
 *
 * @author Ahmad Shahwan
 */
public interface GroupManagement
        extends CrudManager<Group>, SearchEngine<Group> {
}
