package solutions.digamma.damas.rs;


import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.jboss.weld.environment.se.Weld;
import org.jboss.weld.environment.se.WeldContainer;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import solutions.digamma.damas.content.Document;
import solutions.digamma.damas.content.File;
import solutions.digamma.damas.rs.auth.Credentials;
import solutions.digamma.damas.rs.content.DocumentUpdater;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.Configuration;
import javax.ws.rs.core.MediaType;
import java.util.Set;

/**
 * Unit test for REST API.
 *
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
        Application app = container.select(Application.class).get();
        Set<Class<?>> classes = app.getClasses();
        // classes.add(JacksonObjectMapperProvider.class);
        return new ResourceConfig(classes);
    }

    private void auth(MediaType contentType) {
        Credentials cred = new Credentials("admin", "admin");
        Authentication auth = target("auth").request().post(
                Entity.entity(cred, contentType),
                Authentication.class);
        String token = auth.getToken();
        assert StubProviders.TOKEN.equals(token) :
                "Different token from what expected.";
    }

    private void document(MediaType contentType) {
        Document file;
        file = target("documents/" + StubProviders.DOCUMENT_ID)
                .request()
                .accept(contentType)
                .get(DocumentUpdater.class);
        assert file != null : "Error GETting document.";
        file = target("documents")
                .request()
                .post(Entity.entity(file, contentType), DocumentUpdater.class);
        assert file != null : "Error POSTing document.";
        file = target("documents/" + StubProviders.DOCUMENT_ID)
                .request()
                .put(Entity.entity(file, contentType), DocumentUpdater.class);
        assert file != null : "Error PUTing document.";
        target("documents/" + StubProviders.DOCUMENT_ID)
                .request()
                .delete();
    }

    @Test
    public void testAuthJson() {
        auth(MediaType.APPLICATION_JSON_TYPE);
    }

    @Test
    public void testAuthXml() {
        auth(MediaType.APPLICATION_XML_TYPE);
    }

    @Test
    public void testDocumentJson() {
        document(MediaType.APPLICATION_JSON_TYPE);
    }

    public void testDocumentXml() {
        document(MediaType.APPLICATION_XML_TYPE);
    }
}
