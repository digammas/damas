package solutions.digamma.damas.user;

import solutions.digamma.damas.CrudManager;
import solutions.digamma.damas.SearchEngine;

/**
 * User management service.
 *
 * @author Ahmad Shahwan
 */
public interface UserManager extends CrudManager<User>, SearchEngine<User> {
}
