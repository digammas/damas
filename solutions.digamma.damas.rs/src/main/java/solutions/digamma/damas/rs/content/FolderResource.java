package solutions.digamma.damas.rs.content;

import solutions.digamma.damas.content.Folder;
import solutions.digamma.damas.content.FolderManager;
import solutions.digamma.damas.rs.SearchEnabledCrudResource;

import javax.inject.Inject;
import javax.ws.rs.Path;

/**
 * @author Ahmad Shahwan
 */
@Path("folder")
public class FolderResource extends SearchEnabledCrudResource<Folder> {

    @Inject
    protected FolderManager manager;

    @Override
    protected FolderManager getManager() {
        return this.manager;
    }
}
