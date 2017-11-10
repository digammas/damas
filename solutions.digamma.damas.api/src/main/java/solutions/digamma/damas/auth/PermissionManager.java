package solutions.digamma.damas.auth;

import solutions.digamma.damas.entity.CrudManager;

/**
 * Access control manager. This service is used to add, remove and modify
 * permission assignments to files.
 *
 * @author Ahmad Shahwan
 */
public interface PermissionManager extends CrudManager<Permission> {
}
