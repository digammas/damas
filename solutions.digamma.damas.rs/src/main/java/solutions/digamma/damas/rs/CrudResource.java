package solutions.digamma.damas.rs;

import solutions.digamma.damas.CrudManager;
import solutions.digamma.damas.DocumentException;
import solutions.digamma.damas.Entity;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

/**
 * CRUD REST endpoint.
 *
 * @author Ahmad Shahwan
 */
public abstract class CrudResource<T extends Entity> extends EntityResource<T> {

    @Override
    abstract protected CrudManager<T> getManager();

    /**
     * Create new entity.
     *
     * @param entity Entity object.
     * @return Newly created entity.
     * @throws DocumentException
     */
    @PUT
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public T create(
            T entity)
            throws DocumentException {
        return this.getManager().create(this.getToken(), entity);
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
    public T update(
            @QueryParam("id") String id,
            T entity)
            throws DocumentException {
        return this.getManager().update(this.getToken(), id, entity);
    }

    /**
     * Delete existing entity.
     *
     * @param id Entity identifier.
     * @throws DocumentException
     */
    @PUT
    @Path("/{id}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public void delete(
            @QueryParam("id") String id)
            throws DocumentException {
        this.getManager().delete(this.getToken(), id);
    }
}
