package solutions.digamma.damas.rs.content;

import solutions.digamma.damas.DocumentException;
import solutions.digamma.damas.content.Document;
import solutions.digamma.damas.content.DocumentManager;
import solutions.digamma.damas.rs.CrudResource;

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
public class DocumentResource extends CrudResource<Document, DocumentUpdater> {

    @Inject
    protected DocumentManager manager;

    /**
     * Provide detailed description of folder.
     */
    private @QueryParam("full") Boolean full;

    @Override
    protected DocumentManager getManager() {
        return this.manager;
    }

    @Override
    public Document retrieve(String id)
            throws DocumentException {
        Document document = super.retrieve(id);
        if (this.full != null && this.full) {
            document = document.expand();
        }
        return document;
    }
}
