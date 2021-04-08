package solutions.digamma.damas.servlet;

import com.sun.net.httpserver.HttpExchange;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

final class RequestWrapper extends HttpServletRequestWrapper {
    private final HttpExchange ex;
    private final Map<String, List<String>> postData;
    private final ServletInputStream is;
    private final Map<String, Object> attributes = new HashMap<>();

    RequestWrapper(
            HttpServletRequest request,
            HttpExchange ex,
            Map<String,
            List<String>> postData,
            ServletInputStream is) {
        super(request);
        this.ex = ex;
        this.postData = postData;
        this.is = is;
    }

    @Override
    public String getHeader(String name) {
        return ex.getRequestHeaders().getFirst(name);
    }

    @Override
    public Enumeration<String> getHeaders(String name) {
        return Collections.enumeration(ex.getRequestHeaders().get(name));
    }

    @Override
    public Enumeration<String> getHeaderNames() {
        return Collections.enumeration(ex.getRequestHeaders().keySet());
    }

    @Override
    public Object getAttribute(String name) {
        return attributes.get(name);
    }

    @Override
    public void setAttribute(String name, Object value) {
        this.attributes.put(name, value);
    }

    @Override
    public Enumeration<String> getAttributeNames() {
        return Collections.enumeration(attributes.keySet());
    }

    @Override
    public String getMethod() {
        return ex.getRequestMethod();
    }

    @Override
    public ServletInputStream getInputStream() {
        return is;
    }

    @Override
    public BufferedReader getReader() {
        return new BufferedReader(new InputStreamReader(getInputStream()));
    }

    @Override
    public String getPathInfo() {
        return ex.getRequestURI().getPath();
    }

    @Override
    public String getParameter(String name) {
        List<String> list = postData.get(name);
        if (list == null || list.isEmpty()) {
            return null;
        }
        if (list.size() > 1) {
            return String.join(",", list);
        }
        return list.get(0);
    }

    @Override
    public Map<String, String[]> getParameterMap() {
        return postData.entrySet().stream()
                .map(e -> Map.entry(
                        e.getKey(),
                        e.getValue().toArray(new String[0])))
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue));
    }

    @Override
    public Enumeration<String> getParameterNames() {
        return Collections.enumeration(postData.keySet());
    }
}
