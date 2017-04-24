package solutions.digamma.damas.rs.content;

import solutions.digamma.damas.content.Document;
import solutions.digamma.damas.content.DocumentManager;
import solutions.digamma.damas.rs.CrudResource;

import javax.inject.Inject;
import javax.ws.rs.Path;

/**
 * Document REST endpoint.
 *
 * @author Ahmad Shahwan
 */
@Path("document")
public class DocumentResource extends CrudResource<Document> {

    @Inject
    protected DocumentManager manager;

    @Override
    protected DocumentManager getManager() {
        return this.manager;
    }
}
