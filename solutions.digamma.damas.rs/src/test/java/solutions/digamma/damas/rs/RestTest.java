package solutions.digamma.damas.rs;


import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.jboss.weld.environment.se.Weld;
import org.jboss.weld.environment.se.WeldContainer;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import solutions.digamma.damas.auth.Token;
import solutions.digamma.damas.content.Document;
import solutions.digamma.damas.content.Folder;
import solutions.digamma.damas.rs.auth.Credentials;
import solutions.digamma.damas.rs.content.DocumentSerialization;
import solutions.digamma.damas.rs.content.FolderSerialization;
import solutions.digamma.damas.rs.serialization.XmlMessageBodyFeature;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;

/**
 * Unit test for REST API.
 *
 * @author Ahmad Shahwan
 */
@RunWith(Parameterized.class)
public class RestTest extends JerseyTest {

    private static WeldContainer container;

    private MediaType ct;

    public RestTest(MediaType parameter) {
        super();
        this.ct = parameter;
    }

    @Parameterized.Parameters
    public static MediaType[] data() {
        return new MediaType[]{
                MediaType.APPLICATION_JSON_TYPE,
                MediaType.APPLICATION_XML_TYPE
        };
    }

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

    @Override
    protected void configureClient(ClientConfig clientConfig) {
        clientConfig.register(XmlMessageBodyFeature.class);
    }

    @Test
    public void testAuth() {
        Credentials cred = new Credentials("admin", "admin");
        Token auth = target("auth").request().post(
                Entity.entity(cred, this.ct),
                Authentication.class);
        String token = auth.getSecret();
        assert StubProviders.TOKEN.equals(token) :
                "Different token from what expected.";
    }

    @Test
    public void testDocument() {
        Document file;
        file = target("documents/" + StubProviders.DOCUMENT_ID)
                .request()
                .accept(this.ct)
                .get(DocumentSerialization.class);
        assert file != null : "Error GETting document.";
        file = target("documents")
                .request()
                .post(Entity.entity(file, this.ct), DocumentSerialization.class);
        assert file != null : "Error POSTing document.";
        file = target("documents/" + StubProviders.DOCUMENT_ID)
                .request()
                .put(Entity.entity(file, this.ct), DocumentSerialization.class);
        assert file != null : "Error PUTing document.";
        target("documents/" + StubProviders.DOCUMENT_ID)
                .request()
                .delete();
    }

    @Test
    public void testFolder() {
        Folder file;
        file = target("folders/" + StubProviders.FOLDER_ID)
                .request()
                .accept(this.ct)
                .get(FolderSerialization.class);
        assert file != null : "Error GETting folder.";
        file = target("folders")
                .request()
                .post(Entity.entity(file, this.ct), FolderSerialization.class);
        assert file != null : "Error POSTing folder.";
        file = target("folders/" + StubProviders.FOLDER_ID)
                .request()
                .put(Entity.entity(file, this.ct), FolderSerialization.class);
        assert file != null : "Error PUTing folder.";
        target("folders/" + StubProviders.FOLDER_ID)
                .request()
                .delete();
    }
}
