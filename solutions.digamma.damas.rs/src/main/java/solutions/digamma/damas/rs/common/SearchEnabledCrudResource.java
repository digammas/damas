package solutions.digamma.damas.rs.common;

import solutions.digamma.damas.common.WorkspaceException;
import solutions.digamma.damas.entity.Entity;
import solutions.digamma.damas.entity.Page;
import solutions.digamma.damas.entity.SearchEngine;

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

    abstract protected SearchEngine<E> getSearchEngine();

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
    @Authenticated
    public Page<S> retrieve(
            @QueryParam("offset") @DefaultValue("0") int offset,
            @QueryParam("size") @DefaultValue("30") int size)
            throws WorkspaceException {
        return wrap(this.getSearchEngine()
                .find(offset, size, this.getQuery()));
    }

    protected Page<S> wrap(Page<E> page) throws WorkspaceException {
        List<S> objects = new ArrayList<>(page.getObjects().size());
        for (E entity : page.getObjects()) {
            objects.add(wrap(entity));
        }
        return new Page<>() {
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
