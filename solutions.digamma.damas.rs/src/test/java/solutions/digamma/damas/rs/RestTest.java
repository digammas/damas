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
import solutions.digamma.damas.common.WorkspaceException;
import solutions.digamma.damas.content.Comment;
import solutions.digamma.damas.content.Document;
import solutions.digamma.damas.content.Folder;
import solutions.digamma.damas.session.Token;
import solutions.digamma.damas.rs.auth.Credentials;
import solutions.digamma.damas.rs.common.AuthenticationToken;
import solutions.digamma.damas.rs.content.CommentSerialization;
import solutions.digamma.damas.rs.content.DocumentSerialization;
import solutions.digamma.damas.rs.content.FolderSerialization;
import solutions.digamma.damas.rs.user.GroupSerialization;
import solutions.digamma.damas.rs.user.UserSerialization;
import solutions.digamma.damas.user.Group;
import solutions.digamma.damas.user.User;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
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

    private final MediaType ct;

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
    }

    @Test
    public void testAuth() {
        Credentials cred = new Credentials("admin", "admin");
        Token auth = target("auth").request().post(
                Entity.entity(cred, this.ct),
                AuthenticationToken.class);
        String token = auth.getSecret();
        assert StubProviders.TOKEN.equals(token) :
                "Different token from what expected.";
    }

    @Test
    public void testDocument() {
        Document file;
        file = target("documents/%s", StubProviders.DOCUMENT_ID)
                .request()
                .accept(this.ct)
                .get(DocumentSerialization.class);
        assert file != null : "Error GETting document.";
        file = target("documents")
            .request()
            .post(Entity.entity(file, this.ct), DocumentSerialization.class);
        assert file != null : "Error POSTing document.";
        file = target("documents/%s", StubProviders.DOCUMENT_ID)
            .request()
            .put(Entity.entity(file, this.ct), DocumentSerialization.class);
        assert file != null : "Error PUTing document.";
        target("documents/%s", StubProviders.DOCUMENT_ID)
                .request()
                .delete();
    }

    @Test
    public void testFolder() {
        Folder file;
        file = target("folders/%s", StubProviders.FOLDER_ID)
                .request()
                .accept(this.ct)
                .get(FolderSerialization.class);
        assert file != null : "Error GETting folder.";
        file = target("folders")
                .request()
                .post(Entity.entity(file, this.ct), FolderSerialization.class);
        assert file != null : "Error POSTing folder.";
        file = target("folders/%s", StubProviders.FOLDER_ID)
                .request()
                .put(Entity.entity(file, this.ct), FolderSerialization.class);
        assert file != null : "Error PUTing folder.";
        target("folders/%s", StubProviders.FOLDER_ID)
                .request()
                .delete();
    }

    @Test
    public void testFullDocumentResponseContainsMetadata() {
        String response = target("documents/%s", StubProviders.DOCUMENT_ID)
                .queryParam("full", true)
                .request()
                .accept(this.ct)
                .get(String.class);
        assert response.contains("metadata") : "Expecting metadata in details.";
    }

    @Test
    public void testShortDocumentResponseContainsMetadata() {
        String response = target("documents/%s", StubProviders.DOCUMENT_ID)
                .queryParam("full", false)
                .request()
                .accept(this.ct)
                .get(String.class);
        assert !response.contains("metadata") : "Metadata in short version.";
    }

    @Test
    public void testFullDocumentEntityContainsMetadata()
            throws WorkspaceException {
        Document file = target("documents/%s", StubProviders.DOCUMENT_ID)
                .queryParam("full", true)
                .request()
                .accept(this.ct)
                .get(DocumentSerialization.class);
        assert file.getMetadata() != null : "Expecting metadata in details.";
    }

    @Test
    public void testShortDocumentEntityContainsMetadata()
            throws WorkspaceException {
        Document file = target("documents/%s", StubProviders.DOCUMENT_ID)
                .queryParam("full", false)
                .request()
                .accept(this.ct)
                .get(DocumentSerialization.class);
        assert file.getMetadata() == null : "Metadata in short version.";
    }

    @Test
    public void testComment() {
        Comment cmnt;
        cmnt = target("comments/%s", StubProviders.COMMENT_ID)
                .request()
                .accept(this.ct)
                .get(CommentSerialization.class);
        assert cmnt != null : "Error GETting comment.";
        cmnt = target("comments")
                .request()
                .post(Entity.entity(cmnt, this.ct), CommentSerialization.class);
        assert cmnt != null : "Error POSTing comment.";
        cmnt = target("comments/%s", StubProviders.COMMENT_ID)
                .request()
                .put(Entity.entity(cmnt, this.ct), CommentSerialization.class);
        assert cmnt != null : "Error PUTing comment.";
        target("comments/%s", StubProviders.COMMENT_ID)
                .request()
                .delete();
    }

    @Test
    public void testUser() {
        User user;
        user = target("users/%s", StubProviders.USER_ID)
                .request()
                .accept(this.ct)
                .get(UserSerialization.class);
        assert user != null : "Error GETting user.";
        user = target("users")
                .request()
                .post(Entity.entity(user, this.ct), UserSerialization.class);
        assert user != null : "Error POSTing user.";
        user = target("users/%s", StubProviders.USER_ID)
                .request()
                .put(Entity.entity(user, this.ct), UserSerialization.class);
        assert user != null : "Error PUTing user.";
        target("users/%s", StubProviders.USER_ID)
                .request()
                .delete();
    }

    @Test
    public void testGroup() {
        Group group;
        group = target("groups/%s", StubProviders.GROUP_ID)
                .request()
                .accept(this.ct)
                .get(GroupSerialization.class);
        assert group != null : "Error GETting group.";
        group = target("groups")
                .request()
                .post(Entity.entity(group, this.ct), GroupSerialization.class);
        assert group != null : "Error POSTing group.";
        group = target("groups/%s", StubProviders.GROUP_ID)
                .request()
                .put(Entity.entity(group, this.ct), GroupSerialization.class);
        assert group != null : "Error PUTing group.";
        target("groups/%s", StubProviders.GROUP_ID)
                .request()
                .delete();
    }


    private WebTarget target(String pattern, Object... args) {
        return target(String.format(pattern, args));
    }

}
