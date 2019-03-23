package solutions.digamma.damas.rs.content;

import solutions.digamma.damas.common.WorkspaceException;
import solutions.digamma.damas.content.Document;
import solutions.digamma.damas.content.DocumentManager;
import solutions.digamma.damas.rs.common.CrudResource;

import javax.inject.Inject;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;

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

    @Override
    public DocumentSerialization retrieve(String id)
            throws WorkspaceException {
        return wrap(super.retrieve(id));
    }

    @Override
    protected DocumentSerialization wrap(Document entity)
            throws WorkspaceException {
        return DocumentSerialization.from(entity, this.full);
    }
}
