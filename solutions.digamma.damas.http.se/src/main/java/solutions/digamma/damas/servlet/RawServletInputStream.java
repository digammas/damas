package solutions.digamma.damas.servlet;

import java.io.IOException;
import java.io.InputStream;
import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;

public class RawServletInputStream extends ServletInputStream {

    private final InputStream is;
    private boolean finished = false;

    public RawServletInputStream(InputStream is) {
        this.is = is;
    }

    @Override
    public boolean isFinished() {
        return this.finished;
    }

    @Override
    public boolean isReady() {
        return !this.finished;
    }

    @Override
    public void setReadListener(ReadListener listener) {
    }

    @Override
    public int read() throws IOException {
        int value = this.is.read();
        if (value < 0) {
            this.finished = true;
        }
        return value;
    }
}
