package solutions.digamma.damas.integration;

import solutions.digamma.damas.config.Configuration;
import solutions.digamma.damas.config.Fallback;
import solutions.digamma.damas.rs.providers.JerseyProvider;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public abstract class IntegrationTest {

    protected final static String USERNAME = "admin";
    protected final static String PASSWORD = "admin";

    protected final static String AUTH_HEADER = "Authorization";
    protected final static String AUTH_SCHEME = "bearer";

    protected final static MediaType MEDIA_TYPE = MediaType.APPLICATION_JSON_TYPE;

    protected String token;
    protected String rootId;
    protected WebTarget target;

    @Inject
    @Configuration("http.port") @Fallback("8080")
    private Integer port;

    @Inject @Configuration("http.path") @Fallback("dms")
    private String path;

    @Inject
    private JerseyProvider webapp;

    @Inject
    private Logger logger;

    protected String getAuthHeaderValue() {
        assert this.token != null;
        return String.format("%s %s", AUTH_SCHEME, this.token);
    }

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
                    .post(entity(login))
                    .readEntity(HashMap.class);
            this.token = auth.get("secret");
        }
        assert this.token != null;
        return this.token;
    }

    @SuppressWarnings("unchecked")
    private String getRootId() {
        Map<String, Object> answer = target
                .path("folders/at/")
                .request(MEDIA_TYPE)
                .header(AUTH_HEADER, this.getAuthHeaderValue())
                .get()
                .readEntity(Map.class);
        this.rootId = (String) answer.get("id");
        return this.rootId;
    }

    private void disconnect() {
        if (this.token == null) {
            return;
        }
        int status = target
                .path("login")
                .request()
                .header(AUTH_HEADER, this.getAuthHeaderValue())
                .delete()
                .getStatus();
        assert status / 100 == 2;
    }

    protected <T> Entity entity(T object) {
        return Entity.entity(object, MEDIA_TYPE);
    }
}
