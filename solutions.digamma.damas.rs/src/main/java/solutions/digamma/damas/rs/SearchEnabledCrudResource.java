package solutions.digamma.damas.rs;

import solutions.digamma.damas.DocumentException;
import solutions.digamma.damas.Entity;
import solutions.digamma.damas.FullManager;
import solutions.digamma.damas.Page;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

/**
 * @author Ahmad Shahwan
 */
abstract public class SearchEnabledCrudResource<T extends Entity>
        extends CrudResource<T> {

    @Override
    abstract protected FullManager<T> getManager();

    /**
     * Search query. Subclasses are expected to override this method.
     *
     * @return
     */
    protected Object getQuery() {
        return null;
    }

    @GET
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Page<T> retrieve(
            @QueryParam("offset") @DefaultValue("0") int offset,
            @QueryParam("size") @DefaultValue("30") int size)
            throws DocumentException {
        return this.getManager()
                .find(this.getToken(), offset, size, this.getQuery());
    }
}
