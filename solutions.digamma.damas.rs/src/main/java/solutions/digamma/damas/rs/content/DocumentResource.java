package solutions.digamma.damas.rs.content;

import solutions.digamma.damas.common.WorkspaceException;
import solutions.digamma.damas.content.Document;
import solutions.digamma.damas.content.DocumentManager;
import solutions.digamma.damas.rs.common.Authenticated;
import solutions.digamma.damas.rs.common.CrudResource;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import java.io.InputStream;

/**
 * Document REST endpoint.
 *
 * @label Document
 * @author Ahmad Shahwan
 */
@Path("documents")
public class DocumentResource
        extends CrudResource<Document, DocumentSerialization> {

    @Inject
    protected DocumentManager manager;

    /**
     * Provide detailed description of folder.
     */
    @QueryParam("full")
    private boolean full;

    @Override
    protected DocumentManager getManager() {
        return this.manager;
    }

    /**
     * Upload content of an existing document, erase the old content if any.
     *
     * @param id        document ID
     * @param content   content's input stream
     * @throws WorkspaceException
     */
    @PUT
    @Path("/{id}/upload")
    @Consumes(MediaType.APPLICATION_OCTET_STREAM)
    @Authenticated
    public void upload(
            @PathParam("id") String id,
            InputStream content)
            throws WorkspaceException {
        this.manager.upload(id, content);
    }

    @GET
    @Path("/{id}/download")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    @Authenticated
    public InputStream download(
            @PathParam("id") String id,
            InputStream content)
            throws WorkspaceException {
        return this.manager.download(id).getStream();
    }

    @Override
    protected DocumentSerialization wrap(Document entity) {
        return DocumentSerialization.from(entity, this.full);
    }
}
