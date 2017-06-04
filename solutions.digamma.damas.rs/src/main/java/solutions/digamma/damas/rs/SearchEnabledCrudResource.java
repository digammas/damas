package solutions.digamma.damas.rs;

import solutions.digamma.damas.DocumentException;
import solutions.digamma.damas.Entity;
import solutions.digamma.damas.FullManager;
import solutions.digamma.damas.Page;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.ListIterator;

/**
 * @author Ahmad Shahwan
 */
abstract public class SearchEnabledCrudResource<E extends Entity, S extends E>
        extends CrudResource<E, S> {

    @Override
    abstract protected FullManager<E> getManager();

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
    public Page<E> retrieve(
            @QueryParam("offset") @DefaultValue("0") int offset,
            @QueryParam("size") @DefaultValue("30") int size)
            throws DocumentException {
        return wrap(this.getManager()
                .find(this.getToken(), offset, size, this.getQuery()));
    }

    protected Page<E> wrap(Page<E> page) throws DocumentException {
        List<E> objects = page.getObjects();
        for (final ListIterator<E> i = objects.listIterator(); i.hasNext();) {
            final E entity = i.next();
            i.set(wrap(entity));
        }
        return page;
    }
}
