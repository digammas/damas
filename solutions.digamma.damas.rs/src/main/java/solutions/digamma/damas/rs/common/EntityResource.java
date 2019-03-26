package solutions.digamma.damas.rs.common;

import solutions.digamma.damas.common.WorkspaceException;
import solutions.digamma.damas.entity.Entity;
import solutions.digamma.damas.entity.EntityManager;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * Basic entity REST resource.
 * This endpoint provide retrieval only endpoint. It also provide the base of
 * other entity resources.
 *
 * @param <E> Entity type.
 * @param <S> Entity serialization type.
 *
 * @author Ahmad Shahwan
 */
public abstract class EntityResource<E extends Entity, S extends E>
        extends BaseResource {

    /**
     * Resource entity manager.
     * Can be/should be overridden by subclasses.
     *
     * @return
     */
    abstract protected EntityManager<E> getManager();

    /**
     * Retrieve entity.
     *
     * @param id Entity's identifier.
     * @return Entity object.
     * @throws WorkspaceException
     */
    @GET
    @Path("/{id}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public S retrieve(@PathParam("id") String id) throws WorkspaceException {
        return wrap(this.getManager().retrieve(this.getToken(), id));
    }

    abstract protected S wrap(E entity);
}
