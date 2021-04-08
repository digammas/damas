package solutions.digamma.damas.servlet;

import com.sun.net.httpserver.HttpExchange;
import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

final class ResponseWrapper extends HttpServletResponseWrapper {
    final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    final ServletOutputStream servletOutputStream =
            new RawServletOutputStream(this.outputStream);

    private final HttpExchange ex;
    private final PrintWriter printWriter;
    private int status = HttpServletResponse.SC_OK;

    ResponseWrapper(HttpServletResponse response, HttpExchange ex) {
        super(response);
        this.ex = ex;
        printWriter = new PrintWriter(servletOutputStream);
    }

    @Override
    public void setContentType(String type) {
        ex.getResponseHeaders().add("Content-Type", type);
    }

    @Override
    public void setHeader(String name, String value) {
        ex.getResponseHeaders().add(name, value);
    }

    @Override
    public ServletOutputStream getOutputStream() {
        return servletOutputStream;
    }

    @Override
    public void setContentLength(int len) {
        ex.getResponseHeaders().add("Content-Length", Integer.toString(len));
    }

    @Override
    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public void sendError(int sc, String msg) {
        this.status = sc;
        if (msg != null) {
            printWriter.write(msg);
        }
    }

    @Override
    public void sendError(int sc) {
        sendError(sc, null);
    }

    @Override
    public PrintWriter getWriter() {
        return printWriter;
    }

    public void complete() {
        try {
            printWriter.flush();
            ex.sendResponseHeaders(status, outputStream.size());
            if (outputStream.size() > 0) {
                ex.getResponseBody().write(outputStream.toByteArray());
            }
            ex.getResponseBody().flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            ex.close();
        }
    }
}
