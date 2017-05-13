package solutions.digamma.damas.rs;

import solutions.digamma.damas.rs.auth.AuthResource;
import solutions.digamma.damas.rs.content.DocumentResource;
import solutions.digamma.damas.rs.content.FolderResource;
import solutions.digamma.damas.rs.error.ExceptionReportFeature;
import solutions.digamma.damas.rs.error.GenericExceptionMapper;
import solutions.digamma.damas.rs.log.LogFeature;
import solutions.digamma.damas.rs.log.LogResponseFilter;
import solutions.digamma.damas.rs.serialization.XmlMessageBodyFeature;

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
        classes.add(AuthResource.class);
        classes.add(DocumentResource.class);
        classes.add(FolderResource.class);
        classes.add(LogFeature.class);
        classes.add(ExceptionReportFeature.class);
        classes.add(XmlMessageBodyFeature.class);
        return classes;
    }
}
