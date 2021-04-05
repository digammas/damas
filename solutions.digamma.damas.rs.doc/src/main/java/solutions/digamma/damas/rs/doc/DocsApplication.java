package solutions.digamma.damas.rs.doc;

import java.util.Collections;
import java.util.Set;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

/**
 * Application that publishes documentation endpoint.
 */
@ApplicationPath("docs")
public class DocsApplication extends Application {

    @Override
    public Set<Class<?>> getClasses() {
        return Collections.singleton(DocsResource.class);
    }
}
