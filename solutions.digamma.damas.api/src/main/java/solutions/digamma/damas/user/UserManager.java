package solutions.digamma.damas.user;

import solutions.digamma.damas.entity.CrudManager;
import solutions.digamma.damas.entity.SearchEngine;

/**
 * User management service.
 *
 * @author Ahmad Shahwan
 */
public interface UserManager extends CrudManager<User>, SearchEngine<User> {
}
