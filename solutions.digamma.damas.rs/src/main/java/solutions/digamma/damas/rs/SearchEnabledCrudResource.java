package solutions.digamma.damas.rs;

import solutions.digamma.damas.DocumentException;
import solutions.digamma.damas.Entity;
import solutions.digamma.damas.FullManager;
import solutions.digamma.damas.Page;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.List;

/**
 * Search-enable CRUD REST resource. The same as CRUD REST resource with search
 * facilities.
 *
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
    public Page<S> retrieve(
            @QueryParam("offset") @DefaultValue("0") int offset,
            @QueryParam("size") @DefaultValue("30") int size)
            throws DocumentException {
        return wrap(this.getManager()
                .find(this.getToken(), offset, size, this.getQuery()));
    }

    protected Page<S> wrap(Page<E> page) throws DocumentException {
        List<S> objects = new ArrayList<>(page.getObjects().size());
        for (E entity : page.getObjects()) {
            objects.add(wrap(entity));
        }
        return new Page<S>() {
            @Override
            public int getTotal() {
                return page.getTotal();
            }

            @Override
            public int getSize() {
                return page.getSize();
            }

            @Override
            public int getOffset() {
                return page.getOffset();
            }

            @Override
            public List<S> getObjects() {
                return objects;
            }
        };
    }
}
