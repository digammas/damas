package solutions.digamma.damas.rs.user;

import solutions.digamma.damas.entity.CrudManager;
import solutions.digamma.damas.entity.SearchEngine;
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
    protected SearchEngine<User> getSearchEngine() {
        return this.manager;
    }

    @Override
    protected UserSerialization wrap(User entity) {
        return UserSerialization.from(entity);
    }
}
