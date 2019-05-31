package solutions.digamma.damas.rs.user;

import solutions.digamma.damas.common.WorkspaceException;
import solutions.digamma.damas.entity.CrudManager;
import solutions.digamma.damas.search.Filter;
import solutions.digamma.damas.search.SearchEngine;
import solutions.digamma.damas.rs.common.Authenticated;
import solutions.digamma.damas.rs.common.SearchEnabledCrudResource;
import solutions.digamma.damas.user.User;
import solutions.digamma.damas.user.UserManager;

import javax.inject.Inject;
import javax.ws.rs.Path;

@Path("users")
public class UserResource extends SearchEnabledCrudResource<User, UserSerialization> {

    @Inject
    private UserManager manager;

    @Override
    protected CrudManager<User> getManager() {
        return this.manager;
    }

    @Override
    protected SearchEngine<User, Filter> getSearchEngine() {
        return this.manager;
    }

    @Override
    protected UserSerialization wrap(User entity) {
        return UserSerialization.from(entity);
    }

    @Authenticated
    @Override
    public UserSerialization create(UserSerialization entity)
            throws WorkspaceException {
        UserSerialization user = super.create(entity);
        this.updatePassword(user.id, entity.getPassword());
        return user;
    }


    @Authenticated
    @Override
    public UserSerialization update(String id, UserSerialization entity)
            throws WorkspaceException {
        UserSerialization user = super.update(id, entity);
        this.updatePassword(user.id, entity.getPassword());
        return user;
    }

    private void updatePassword(String id, String password)
            throws WorkspaceException {
        if (password != null) {
            this.manager.updatePassword(id, password);
        }
    }
}
