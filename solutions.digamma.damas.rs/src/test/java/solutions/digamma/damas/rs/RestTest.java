package solutions.digamma.damas.rs;


import org.glassfish.jersey.test.JerseyTest;
import org.jboss.weld.environment.se.Weld;
import org.jboss.weld.environment.se.WeldContainer;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import solutions.digamma.damas.rs.auth.Credentials;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;

/**
 * @author Ahmad Shahwan
 */
public class RestTest extends JerseyTest {

    private static WeldContainer container;

    @BeforeClass
    public static void setUpClass() {
        container = new Weld().initialize();
    }

    @AfterClass
    public static void tearDownClass() {
        container.close();
    }

    @Override
    protected Application configure() {
        return container.select(Application.class).get();
    }

    @Test
    public void testAuth() {
        Credentials cred = new Credentials("admin", "admin");
        Authentication auth = target("auth").request().post(
                Entity.entity(cred, MediaType.APPLICATION_JSON),
                Authentication.class);
        String token = auth.getToken();
        assert StubProviders.TOKEN.equals(token) :
                "Different token from what expected.";
    }
}
