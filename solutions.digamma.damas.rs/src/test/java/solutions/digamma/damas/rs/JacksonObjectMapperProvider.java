package solutions.digamma.damas.rs;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector;
import org.mockito.internal.creation.bytebuddy.MockAccess;

import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;

/**
 * Ignore Mockito-added properties when serializing into JSON.
 *
 * @author Ahmad Shahwan
 */
@Provider
public class JacksonObjectMapperProvider
        implements ContextResolver<ObjectMapper> {

    final ObjectMapper mapper;

    public JacksonObjectMapperProvider() {
        mapper = new ObjectMapper();
        mapper.setAnnotationIntrospector(new JacksonAnnotationIntrospector() {

            @Override
            public boolean hasIgnoreMarker(final AnnotatedMember m) {
                return MockAccess.class
                        .equals(m.getDeclaringClass()) ||
                        super.hasIgnoreMarker(m);
            }
        });
    }

    @Override
    public ObjectMapper getContext(Class<?> type) {
        return mapper;
    }
}
