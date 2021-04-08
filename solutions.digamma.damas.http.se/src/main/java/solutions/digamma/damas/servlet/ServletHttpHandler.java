package solutions.digamma.damas.servlet;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.IOException;
import java.lang.reflect.Proxy;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Basic servlet support.
 *
 * This is not a full servlet implementation. For use in production use, please
 * use a real servlet container.
 */
public class ServletHttpHandler implements HttpHandler {

    private final Servlet servlet;

    public ServletHttpHandler(Servlet servlet) {
        this.servlet = servlet;
    }

    private String urlDecode(String encoded) {
        return URLDecoder.decode(encoded, StandardCharsets.UTF_8);
    }

    @Override
    public void handle(HttpExchange ex) throws IOException {
        final ServletInputStream is =
                new RawServletInputStream(ex.getRequestBody());

        Map<String, List<String>> queryParams = this.parseParams(ex.getRequestURI().getQuery());

        RequestWrapper req = new RequestWrapper(
                createProxy(HttpServletRequest.class), ex, queryParams, is);

        ResponseWrapper resp = new ResponseWrapper(
                createProxy(HttpServletResponse.class), ex);

        try {
            servlet.service(req, resp);
            resp.complete();
        } catch (ServletException e) {
            throw new IOException(e);
        }
    }

    private Map<String, List<String>> parseParams(String query) {
        if (query == null) {
            return Collections.emptyMap();
        }
        return Pattern.compile("&")
                .splitAsStream(query)
                .map(s -> Arrays.copyOf(s.split("=", 2), 2))
                .collect(Collectors.groupingBy(
                        a -> urlDecode(a[0]),
                        Collectors.mapping(
                                a -> urlDecode(a[1]),
                                Collectors.toList())));
    }

    private static <T> T createProxy(Class<T> klass) {
        return klass.cast(Proxy.newProxyInstance(
            ServletHttpHandler.class.getClassLoader(),
            new Class<?>[] { klass },
             (o, m, a) -> { throw new UnsupportedOperationException(); }
        ));
    }
}
