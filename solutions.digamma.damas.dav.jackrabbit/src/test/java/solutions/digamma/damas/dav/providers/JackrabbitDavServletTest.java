package solutions.digamma.damas.dav.providers;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import junit.framework.TestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import solutions.digamma.damas.cdi.ContainerRunner;
import solutions.digamma.damas.config.Configuration;
import solutions.digamma.damas.config.Fallback;
import solutions.digamma.damas.http.HttpServerBootstrapper;

@RunWith(ContainerRunner.class)
public class JackrabbitDavServletTest extends TestCase {

    @Inject
    @Configuration("http.port") @Fallback("8080")
    private Integer port;

    HttpClient httpClient;
    HttpRequest.Builder httpRequestBuilder;

    @Inject
    private HttpServerBootstrapper<?> starter;

    @PostConstruct
    public void init() {
        String uri = String.format(
                "http://localhost:%d/webdav/default/content", this.port);
        this.httpClient = HttpClient.newHttpClient();
        this.httpRequestBuilder = HttpRequest.newBuilder(URI.create(uri));
    }

    @Test
    public void testUnauthorized() throws IOException, InterruptedException {
        HttpRequest request = this.httpRequestBuilder
                .GET().build();
        int status = this.httpClient
                .send(request, HttpResponse.BodyHandlers.discarding())
                .statusCode();
        assert status == 401 : "Expected unauthorized response status";
    }

    @Test
    public void testAuthorized() throws IOException, InterruptedException {
        HttpRequest request = this.httpRequestBuilder
                .header("Authorization", "Basic YWRtaW46YWRtaW4=")
                .GET().build();
        int status = this.httpClient
                .send(request, HttpResponse.BodyHandlers.discarding())
                .statusCode();
        assert status == 200 : "Expected success response status";
    }
}
