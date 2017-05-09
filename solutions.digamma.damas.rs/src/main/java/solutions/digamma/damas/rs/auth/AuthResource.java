package solutions.digamma.damas.rs.auth;

import solutions.digamma.damas.DocumentException;
import solutions.digamma.damas.auth.LoginManager;
import solutions.digamma.damas.auth.Token;
import solutions.digamma.damas.rs.Authentication;
import solutions.digamma.damas.rs.BaseResource;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

/**
 * Authentication REST endpoint.
 *
 * @label Authentication
 * @author Ahmad Shahwan
 */
@Path("auth")
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
    public String info() throws DocumentException {
        throw new DocumentException("Hand-made error.");
    }

    /**
     * Authenticate user.
     *
     * @param cred User credentials.
     * @return Authentication object, containing token.
     * @throws DocumentException
     */
    @POST
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Token login(Credentials cred) throws DocumentException {
        return this.manager.login(cred.getUsername(), cred.getPassword());
    }

    /**
     * Disconnect user, invalidating their session.
     * 
     * @throws DocumentException
     */
    @DELETE
    public void logout() throws DocumentException {
        this.manager.logout(this.getToken());
    }
}
