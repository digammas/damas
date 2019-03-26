package solutions.digamma.damas.rs.user;

import solutions.digamma.damas.entity.CrudManager;
import solutions.digamma.damas.rs.common.CrudResource;
import solutions.digamma.damas.user.Group;
import solutions.digamma.damas.user.GroupManager;

import javax.inject.Inject;
import javax.ws.rs.Path;

@Path("groups")
public class GroupResource extends CrudResource<Group, GroupSerialization> {

    @Inject
    private GroupManager manager;

    @Override
    protected CrudManager<Group> getManager() {
        return this.manager;
    }

    @Override
    protected GroupSerialization wrap(Group entity) {
        return GroupSerialization.from(entity);
    }
}
