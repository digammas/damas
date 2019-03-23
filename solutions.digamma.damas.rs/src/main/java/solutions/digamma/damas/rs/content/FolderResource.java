package solutions.digamma.damas.rs.content;

import solutions.digamma.damas.common.WorkspaceException;
import solutions.digamma.damas.content.Folder;
import solutions.digamma.damas.content.FolderManager;
import solutions.digamma.damas.rs.common.SearchEnabledCrudResource;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
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
    @QueryParam("full")
    private boolean full;


    /**
     * Depth of retrieved content. For full depth, provide zero. If this
     * parameter is omitted no content will be shown.
     */
    @QueryParam("depth")
    private Integer depth;

    @Override
    protected FolderManager getManager() {
        return this.manager;
    }

    @Override
    protected FolderManager getSearchEngine() {
        return this.manager;
    }

    @Override
    public FolderSerialization retrieve(String id)
            throws WorkspaceException {
        FolderSerialization folder = wrap(super.retrieve(id));
        if (this.depth != null) {
            if (this.depth == 0) {
                folder.expandContent();
            } else {
                folder.expandContent(this.depth);
            }
        }
        return folder;
    }

    @GET
    @Path("at/{path: .+}")
    public FolderSerialization find(
            @PathParam("path") String path)
            throws WorkspaceException {
        return wrap(this.manager.find(this.getToken(), path));
    }

    @GET
    @Path("at")
    public FolderSerialization find()
            throws WorkspaceException {
        return wrap(this.manager.find(this.getToken(), "."));
    }

    @Override
    protected FolderSerialization wrap(Folder entity)
            throws WorkspaceException {
        return FolderSerialization.from(entity, this.full);
    }
}
