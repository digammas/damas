package solutions.digamma.damas.rs;

import solutions.digamma.damas.DocumentException;
import solutions.digamma.damas.Entity;
import solutions.digamma.damas.EntityManager;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * Read REST endpoint.
 *
 * @author Ahmad Shahwan
 */
public abstract class EntityResource<T extends Entity> extends BaseResource {

    /**
     * Resource entity manager.
     * Can be/should be overridden by subclasses.
     *
     * @return
     */
    abstract protected EntityManager<T> getManager();

    /**
     * Retrieve entity.
     *
     * @param id Entity's identifier.
     * @return Entity object.
     * @throws DocumentException
     */
    @GET
    @Path("/{id}")
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public T retrieve(@PathParam("id") String id) throws DocumentException {
        return this.getManager().retrieve(this.getToken(), id);
    }

}
