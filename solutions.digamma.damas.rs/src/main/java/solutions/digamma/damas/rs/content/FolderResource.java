package solutions.digamma.damas.rs.content;

import solutions.digamma.damas.common.WorkspaceException;
import solutions.digamma.damas.content.Folder;
import solutions.digamma.damas.content.FolderManager;
import solutions.digamma.damas.rs.SearchEnabledCrudResource;

import javax.inject.Inject;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;

/**
 * Folder REST endpoint.
 *
 * @label Folder
 * @author Ahmad Shahwan
 */
@Path("folders")
public class FolderResource
        extends SearchEnabledCrudResource<Folder, FolderSerialization> {

    @Inject
    protected FolderManager manager;

    /**
     * Provide detailed description of folder.
     */
    private @QueryParam("full") Boolean full;

    /**
     * Depth of retrieved content. For full depth, provide zero. If this
     * parameter is omitted no content will be shown.
     */
    private @QueryParam("depth") Integer depth;

    @Override
    protected FolderManager getManager() {
        return this.manager;
    }

    @Override
    protected FolderManager getSearchEngine() {
        return this.manager;
    }

    @Override
    protected FolderManager getPathFinder() {
        return this.manager;
    }

    @Override
    public FolderSerialization retrieve(String id)
            throws WorkspaceException {
        Folder folder = super.retrieve(id);
        if (this.full != null && this.full) {
            folder = folder.expand();
        }
        if (this.depth != null) {
            if (this.depth == 0) {
                folder.expandContent();
            } else {
                folder.expandContent(this.depth);
            }
        }
        return wrap(folder);
    }

    @Override
    protected FolderSerialization wrap(Folder entity) throws WorkspaceException {
        return new FolderSerialization(entity);
    }
}
