package solutions.digamma.damas.logging;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.util.logging.StreamHandler;
import javax.inject.Singleton;

@Singleton
public class StreamHandlerProvider extends StreamHandler {

    public static final OutputStream OUT = new ByteArrayOutputStream();

    public StreamHandlerProvider() {
        super();
        this.setOutputStream(this.OUT);
    }


}
