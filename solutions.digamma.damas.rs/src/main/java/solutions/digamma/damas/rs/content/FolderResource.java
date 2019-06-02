package solutions.digamma.damas.rs.content;

import solutions.digamma.damas.common.WorkspaceException;
import solutions.digamma.damas.content.Folder;
import solutions.digamma.damas.content.FolderManager;
import solutions.digamma.damas.rs.common.Authenticated;
import solutions.digamma.damas.rs.common.SearchEnabledCrudResource;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
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

    @Authenticated
    @Override
    public FolderSerialization retrieve(String id)
            throws WorkspaceException {
        Folder folder = this.manager.retrieve(id);
        if (this.depth != null) {
            if (this.depth == 0) {
                folder.expandContent();
            } else {
                folder.expandContent(this.depth);
            }
        }
        return wrap(folder);
    }


    /**
     * Copy an existing folder under a given folder
     *
     * @param id        source folder ID
     * @param entity    model entity containing new parent ID
     * @throws WorkspaceException
     */
    @POST
    @Path("/{id}/copy")
    @Authenticated
    public FolderSerialization copy(
            @PathParam("id") String id,
            DocumentSerialization entity)
            throws WorkspaceException {
        return wrap(this.manager.copy(id, entity.getParentId()));
    }

    @GET
    @Path("at/{path: .*}")
    @Authenticated
    public FolderSerialization find(@PathParam("path") String path)
            throws WorkspaceException {
        return wrap(this.manager.find("/".concat(path)));
    }

    @Override
    protected FolderSerialization wrap(Folder entity) {
        return FolderSerialization.from(entity, this.full);
    }
}
