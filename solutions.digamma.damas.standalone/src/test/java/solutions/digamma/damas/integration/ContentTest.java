package solutions.digamma.damas.integration;

import org.junit.Test;
import org.junit.runner.RunWith;
import solutions.digamma.damas.cdi.ContainerRunner;
import solutions.digamma.damas.config.Configuration;
import solutions.digamma.damas.config.Fallback;
import solutions.digamma.damas.rs.providers.JerseyProvider;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Content integration test.
 *
 * @author Ahmad Shahwan
 */
@RunWith(ContainerRunner.class)
@Singleton
public class ContentTest {

    private final static String USERNAME = "admin";
    private final static String PASSWORD = "admin";

    private final static String AUTH_HEADER = "Authorization";
    private final static String AUTH_SCHEME = "bearer";

    private final static MediaType MEDIA_TYPE =MediaType.APPLICATION_JSON_TYPE;

    private String token;
    private String rootId;
    private WebTarget target;

    @Inject @Configuration("http.port") @Fallback("8080")
    private Integer port;

    @Inject @Configuration("http.path") @Fallback("dms")
    private String path;

    @Inject
    private JerseyProvider webapp;

    @Inject
    private Logger logger;

    @PostConstruct
    public void init() {
        URI url = URI.create(String.format(
                "http://localhost:%d/%s/rest/", this.port, this.path));
        this.target = ClientBuilder.newClient().target(url);
        this.authenticate();
        this.getRootId();
    }

    @PreDestroy
    public void dispose() {
        this.disconnect();
    }

    @SuppressWarnings("unchecked")
    private String authenticate() {
        if (this. token == null) {
            Map<String, Object> login = new HashMap<>(2);
            login.put("username", USERNAME);
            login.put("password", PASSWORD);
            Map<String, String> auth = target
                .path("login")
                .request(MEDIA_TYPE)
                .post(Entity.json(login))
                .readEntity(HashMap.class);
            this.token = auth.get("secret");
        }
        assert this.token != null;
        return this.token;
    }

    @SuppressWarnings("unchecked")
    private String getRootId() {
        Map<String, Object> answer = target
                .path("folders/path/")
                .request(MEDIA_TYPE)
                .header(AUTH_HEADER, this.getAuthHeaderValue())
                .get()
                .readEntity(Map.class);
        this.rootId = (String) answer.get("id");
        return this.rootId;
    }

    private void disconnect() {
        if (this.token != null) {
            int status = target
                .path("login")
                .request()
                .header(AUTH_HEADER, this.getAuthHeaderValue())
                .delete()
                .getStatus();
            assert status / 100 == 2;
        }
    }

    private String getAuthHeaderValue() {
        assert this.token != null;
        return String.format("%s %s", AUTH_SCHEME, this.token);
    }

    @Test
    public void testBadAuth() {
        Map<String, Object> login = new HashMap<>(2);
        login.put("username", "villain");
        login.put("password", "bad");
        int status = target
                .path("login")
                .request(MEDIA_TYPE)
                .post(Entity.json(login))
                .getStatus();
        assert status == 401;
    }

    @Test
    public void testUnauthorizedAccess() {
        int status = target
                .path("folders")
                .request(MEDIA_TYPE)
                .get()
                .getStatus();
        assert status == 401;
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testRenameFolder() {
        Map<String, Object> answer;
        Map<String, Object> body = new HashMap<>();
        String name = "test_folder";
        body.put("parentId", this.rootId);
        body.put("name", name);
        answer = target
                .path("folders")
                .request(MEDIA_TYPE)
                .header(AUTH_HEADER, this.getAuthHeaderValue())
                .post(Entity.json(body))
                .readEntity(Map.class);
        String id = (String) answer.get("id");
        assert id != null;
        answer = target
                .path("folders")
                .path(id)
                .request(MEDIA_TYPE)
                .header(AUTH_HEADER, this.getAuthHeaderValue())
                .get()
                .readEntity(Map.class);
        assert name.equals(answer.get("name"));
        name = "folder_test";
        body.clear();
        body.put("name", name);
        assert target
                .path("folders")
                .path(id)
                .request(MEDIA_TYPE)
                .header(AUTH_HEADER, this.getAuthHeaderValue())
                .put(Entity.json(body))
                .getStatus() / 100 == 2;
        answer = target
                .path("folders")
                .path(id)
                .request(MEDIA_TYPE)
                .header(AUTH_HEADER, this.getAuthHeaderValue())
                .get()
                .readEntity(Map.class);
        assert name.equals(answer.get("name"));
        assert target
                .path("folders")
                .path(id)
                .request(MEDIA_TYPE)
                .header(AUTH_HEADER, this.getAuthHeaderValue())
                .delete()
                .getStatus() / 100 == 2;
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testMoveFolder() {
        Map<String, Object> answer;
        Map<String, Object> body = new HashMap<>();
        String name = "test_folder";
        body.put("parentId", this.rootId);
        body.put("name", name);
        answer = target
                .path("folders")
                .request(MEDIA_TYPE)
                .header(AUTH_HEADER, this.getAuthHeaderValue())
                .post(Entity.json(body))
                .readEntity(Map.class);
        String id = (String) answer.get("id");
        assert id != null;
        answer = target
                .path("folders")
                .path(id)
                .request(MEDIA_TYPE)
                .header(AUTH_HEADER, this.getAuthHeaderValue())
                .get()
                .readEntity(Map.class);
        assert name.equals(answer.get("name"));
        body.clear();
        body.put("name", "temp");
        body.put("parentId", this.rootId);
        String tempId = (String) target
                .path("folders")
                .request(MEDIA_TYPE)
                .header(AUTH_HEADER, this.getAuthHeaderValue())
                .post(Entity.json(body))
                .readEntity(Map.class).get("id");
        body.clear();
        body.put("parentId", tempId);
        assert target
                .path("folders")
                .path(id)
                .request(MEDIA_TYPE)
                .header(AUTH_HEADER, this.getAuthHeaderValue())
                .put(Entity.json(body))
                .getStatus() / 100 == 2;
        answer = target
                .path("folders")
                .path(id)
                .request(MEDIA_TYPE)
                .header(AUTH_HEADER, this.getAuthHeaderValue())
                .get()
                .readEntity(Map.class);
        assert tempId.equals(answer.get("parentId"));
        assert target
                .path("folders")
                .path(tempId)
                .request(MEDIA_TYPE)
                .header(AUTH_HEADER, this.getAuthHeaderValue())
                .delete()
                .getStatus() / 100 == 2;
    }
}
