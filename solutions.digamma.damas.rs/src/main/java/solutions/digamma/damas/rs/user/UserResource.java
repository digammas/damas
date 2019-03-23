package solutions.digamma.damas.rs.user;

import solutions.digamma.damas.common.WorkspaceException;
import solutions.digamma.damas.entity.CrudManager;
import solutions.digamma.damas.rs.common.CrudResource;
import solutions.digamma.damas.user.User;
import solutions.digamma.damas.user.UserManager;

import javax.inject.Inject;
import javax.ws.rs.Path;

@Path("users")
public class UserResource extends CrudResource<User, UserSerialization> {

    @Inject
    private UserManager manager;

    @Override
    protected CrudManager<User> getManager() {
        return this.manager;
    }

    @Override
    protected UserSerialization wrap(User entity) throws WorkspaceException {
        return UserSerialization.from(entity);
    }
}
