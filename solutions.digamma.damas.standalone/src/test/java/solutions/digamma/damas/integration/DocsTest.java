package solutions.digamma.damas.integration;

import java.net.URI;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import org.junit.Test;
import org.junit.runner.RunWith;
import solutions.digamma.damas.cdi.ContainerRunner;
import solutions.digamma.damas.config.Configuration;
import solutions.digamma.damas.config.Fallback;

@RunWith(ContainerRunner.class)
public class DocsTest {

    protected final static MediaType MEDIA_TYPE = MediaType.TEXT_HTML_TYPE;

    protected WebTarget target;

    @Inject
    @Configuration("http.port") @Fallback("8080")
    private Integer port;

    @PostConstruct
    public void init() {
        URI url = URI.create(String.format(
                "http://localhost:%d/docs/", this.port));
        this.target = ClientBuilder.newClient().target(url);
    }

    @Test
    public void testDocsPublished() {
        String html = this.target
                .path("")
                .request()
                .accept(MEDIA_TYPE)
                .get()
                .readEntity(String.class);
        String doctype = "<!DOCTYPE html>";
        assert html.startsWith(doctype);
    }
}
