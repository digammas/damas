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
                "http://localhost:%d/%s/", this.port, this.path));
        this.target = ClientBuilder.newClient().target(url);
        this.authenticate();
    }

    @PreDestroy
    public void dispose() {
        this.disconnect();
    }

    private String authenticate() {
        if (this. token == null) {
            Map<String, Object> login = new HashMap<>(2);
            login.put("username", USERNAME);
            login.put("password", PASSWORD);
            Map<String, String> auth = target
                .path("auth")
                .request(MEDIA_TYPE)
                .post(Entity.json(login))
                .readEntity(HashMap.class);
            this.token = auth.get("secret");
        }
        assert this.token != null;
        return this.token;
    }

    private void disconnect() {
        if (this.token != null) {
            int status = target
                .path("auth")
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

    public void testBadAuth() {
        Map<String, Object> login = new HashMap<>(2);
        login.put("username", "villain");
        login.put("password", "bad");
        int status = target
                .path("auth")
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
    public void testCreateFolder() {
        String answer = target
                .path("folders")
                .request(MEDIA_TYPE)
                .header(AUTH_HEADER, this.getAuthHeaderValue())
                .get()
                .readEntity(String.class);
        this.logger.info(answer);
    }
}
