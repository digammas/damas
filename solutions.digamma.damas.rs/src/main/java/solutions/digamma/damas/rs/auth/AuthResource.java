package solutions.digamma.damas.rs.auth;

import solutions.digamma.damas.common.WorkspaceException;
import solutions.digamma.damas.login.LoginManager;
import solutions.digamma.damas.login.Token;
import solutions.digamma.damas.rs.BaseResource;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * Authentication REST endpoint.
 *
 * @author Ahmad Shahwan
 * @label Authentication
 */
@Path("login")
public class AuthResource extends BaseResource {

    @Inject
    private LoginManager manager;

    /**
     * Constructor.
     */
    public AuthResource() {
        super();
    }

    @GET
    public String info() throws WorkspaceException {
        throw new WorkspaceException("Hand-made error.");
    }

    /**
     * Authenticate user.
     *
     * @param cred User credentials.
     * @return Authentication object, containing token.
     * @throws WorkspaceException
     */
    @POST
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Token login(Credentials cred) throws WorkspaceException {
        return this.manager.login(cred.getUsername(), cred.getPassword());
    }

    /**
     * Disconnect user, invalidating their session.
     *
     * @throws WorkspaceException
     */
    @DELETE
    public void logout() throws WorkspaceException {
        this.manager.logout(this.getToken());
    }
}
