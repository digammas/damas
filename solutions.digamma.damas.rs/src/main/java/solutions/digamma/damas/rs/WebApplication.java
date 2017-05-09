package solutions.digamma.damas.rs;

import solutions.digamma.damas.rs.auth.AuthResource;
import solutions.digamma.damas.rs.content.DocumentResource;
import solutions.digamma.damas.rs.content.FolderResource;

import javax.inject.Singleton;
import javax.ws.rs.core.Application;
import java.util.HashSet;
import java.util.Set;

/**
 * REST Web application.
 *
 * @author Ahmad Shahwan
 */
@Singleton
public class WebApplication extends Application {

    @Override
    public Set<Class<?>> getClasses() {
        final Set<Class<?>> classes = new HashSet<>();
        classes.add(GenericExceptionMapper.class);
        classes.add(LogFilter.class);
        classes.add(AuthResource.class);
        classes.add(DocumentResource.class);
        classes.add(FolderResource.class);
        return classes;
    }
}
