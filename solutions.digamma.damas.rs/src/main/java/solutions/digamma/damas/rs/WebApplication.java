package solutions.digamma.damas.rs;

import solutions.digamma.damas.rs.auth.AuthResource;
import solutions.digamma.damas.rs.content.DocumentResource;
import solutions.digamma.damas.rs.content.FolderResource;
import solutions.digamma.damas.rs.error.ExceptionReportFeature;
import solutions.digamma.damas.rs.log.LogFeature;

import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.ws.rs.core.Application;
import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * REST Web application.
 *
 * @author Ahmad Shahwan
 */
@Singleton
public class WebApplication extends Application {

    @Inject
    Instance<ContextResolver> singletons;

    @Override
    public Set<Class<?>> getClasses() {
        final Set<Class<?>> classes = new HashSet<>();
        classes.add(AuthResource.class);
        classes.add(DocumentResource.class);
        classes.add(FolderResource.class);
        classes.add(LogFeature.class);
        classes.add(ExceptionReportFeature.class);
        return classes;
    }

    @Override
    public Set<Object> getSingletons() {
        return singletons.select()
                .stream()
                .filter(x -> x.getClass().isAnnotationPresent(Provider.class))
                .collect(Collectors.toSet());
    }
}
