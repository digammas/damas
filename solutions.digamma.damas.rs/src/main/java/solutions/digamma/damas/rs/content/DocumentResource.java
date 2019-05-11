package solutions.digamma.damas.rs.content;

import solutions.digamma.damas.common.WorkspaceException;
import solutions.digamma.damas.content.Document;
import solutions.digamma.damas.content.DocumentManager;
import solutions.digamma.damas.rs.common.Authenticated;
import solutions.digamma.damas.rs.common.CrudResource;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
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

    /**
     * Wild-card media type.
     */
    private final static MediaType WILDCARD_MEDIA_TYPE = new MediaType();

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
     * Copy an existing document under a given folder
     *
     * @param id        source document ID
     * @param entity    model entity containing new parent ID
     * @throws WorkspaceException
     */
    @POST
    @Path("/{id}/copy")
    @Authenticated
    public DocumentSerialization copy(
            @PathParam("id") String id,
            DocumentSerialization entity)
            throws WorkspaceException {
        return wrap(this.manager.copy(id, entity.getParentId()));
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
    @Authenticated
    public void upload(
            @PathParam("id") String id,
            InputStream content)
            throws WorkspaceException {
        this.manager.upload(id, content);
    }

    @GET
    @Path("/{id}/download")
    @Authenticated
    public Response download(
            @PathParam("id") String id,
            InputStream content)
            throws WorkspaceException {
        Document document = this.manager.retrieve(id);
        InputStream is = this.manager.download(id).getStream();
        MediaType type = toMediaType(document.getMimeType());
        return Response.ok(is, type).build();
    }

    @Override
    protected DocumentSerialization wrap(Document entity) {
        return DocumentSerialization.from(entity, this.full);
    }

    private MediaType toMediaType(String value) {
        if (value == null) {
            return WILDCARD_MEDIA_TYPE;
        }
        try {
            return MediaType.valueOf(value);
        } catch (IllegalArgumentException e) {
            return WILDCARD_MEDIA_TYPE;
        }
    }
}
