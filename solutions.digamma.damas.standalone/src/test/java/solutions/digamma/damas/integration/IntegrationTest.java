package solutions.digamma.damas.integration;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import solutions.digamma.damas.config.Configuration;
import solutions.digamma.damas.config.Fallback;
import solutions.digamma.damas.standalone.Launcher;

public abstract class IntegrationTest {

    protected final static String USERNAME = "admin";
    protected final static String PASSWORD = "admin";

    protected final static String AUTH_HEADER = "Authorization";
    protected final static String AUTH_SCHEME = "bearer";

    protected final static MediaType MEDIA_TYPE =
            MediaType.APPLICATION_JSON_TYPE;

    protected String token;
    protected String rootId;
    protected WebTarget target;

    @Inject
    @Configuration("http.port") @Fallback("8080")
    private Integer port;

    /**
     * Kick start.
     */
    @Inject
    private Launcher launcher;

    protected String getAuthHeaderValue() {
        return this.getAuthHeaderValue(this.token);
    }

    protected String getAuthHeaderValue(String token) {
        assert token != null;
        return String.format("%s %s", AUTH_SCHEME, token);
    }

    @PostConstruct
    public void init() {
        URI url = URI.create(String.format(
                "http://localhost:%d/rest/", this.port));
        this.target = ClientBuilder.newClient().target(url);
        this.authenticate();
        this.getRootId();
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
            Map<?, ?> auth = target
                    .path("auth")
                    .request(MEDIA_TYPE)
                    .post(entity(login))
                    .readEntity(HashMap.class);
            this.token = (String) auth.get("secret");
        }
        assert this.token != null;
        return this.token;
    }

    private String getRootId() {
        Map<?, ?> answer = target
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
                .path("auth")
                .request()
                .header(AUTH_HEADER, this.getAuthHeaderValue())
                .delete()
                .getStatus();
        assert status / 100 == 2;
    }

    protected <T> Entity<T> entity(T object) {
        return Entity.entity(object, MEDIA_TYPE);
    }

    protected <T> Entity<T> entity(T object, MediaType mediaType) {
        return Entity.entity(object, mediaType);
    }
}
