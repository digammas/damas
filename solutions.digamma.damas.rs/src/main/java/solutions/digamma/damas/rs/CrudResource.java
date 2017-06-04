package solutions.digamma.damas.rs;

import solutions.digamma.damas.CrudManager;
import solutions.digamma.damas.DocumentException;
import solutions.digamma.damas.Entity;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * CRUD REST endpoint.
 *
 * @author Ahmad Shahwan
 */
public abstract class CrudResource<E extends Entity, S extends E>
        extends EntityResource<E, S> {

    @Override
    abstract protected CrudManager<E> getManager();

    /**
     * Create new entity.
     *
     * @param entity Entity object.
     * @return Newly created entity.
     * @throws DocumentException
     */
    @POST
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public S create(
            S entity)
            throws DocumentException {
        return wrap(this.getManager().create(this.getToken(), entity));
    }

    /**
     * Update existing entity.
     *
     * @param id Entity identifier.
     * @param entity Entity object, containing only fields to be modified.
     * @return Newly modifier entity object.
     * @throws DocumentException
     */
    @PUT
    @Path("/{id}")
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public S update(
            @PathParam("id") String id,
            S entity)
            throws DocumentException {
        return wrap(this.getManager().update(this.getToken(), id, entity));
    }

    /**
     * Delete existing entity.
     *
     * @param id Entity identifier.
     * @throws DocumentException
     */
    @DELETE
    @Path("/{id}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public void delete(
            @PathParam("id") String id)
            throws DocumentException {
        this.getManager().delete(this.getToken(), id);
    }
}
