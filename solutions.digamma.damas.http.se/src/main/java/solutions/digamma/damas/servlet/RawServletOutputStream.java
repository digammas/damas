package solutions.digamma.damas.servlet;

import java.io.IOException;
import java.io.OutputStream;
import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;

public class RawServletOutputStream extends ServletOutputStream {

    private OutputStream os;

    public RawServletOutputStream(OutputStream os) {
        this.os = os;
    }

    @Override
    public boolean isReady() {
        return true;
    }

    @Override
    public void setWriteListener(WriteListener writeListener) {
    }

    @Override
    public void write(int b) throws IOException {
        this.os.write(b);
    }
}
