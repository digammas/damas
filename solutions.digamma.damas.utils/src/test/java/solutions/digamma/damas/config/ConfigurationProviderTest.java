package solutions.digamma.damas.config;

import java.lang.annotation.Annotation;
import javax.enterprise.inject.Instance;
import javax.enterprise.inject.UnsatisfiedResolutionException;
import javax.inject.Inject;
import javax.inject.Singleton;
import org.junit.Test;
import org.junit.runner.RunWith;
import solutions.digamma.damas.cdi.ContainerRunner;

@RunWith(ContainerRunner.class)
@Singleton
public class ConfigurationProviderTest {

    @Inject
    private Instance<String> strings;

    @Inject
    @Configuration(Configurations.HOME)
    private String home;

    @Inject
    @Configuration(value = "optional", optional = true)
    private String optional;

    @Inject
    @Configuration("fallback")
    @Fallback("hello")
    private String fallback;

    @Test
    public void getString() {
        assert this.home != null;
    }

    @Test
    public void getOptionalString() {
        assert this.optional == null;
    }

    @Test
    public void getUnsatisfiedString() {
        try {
            strings.select(String.class, new Configuration() {
                @Override
                public Class<? extends Annotation> annotationType() {
                    return Configuration.class;
                }

                @Override
                public String value() {
                    return "missing";
                }

                @Override
                public boolean optional() {
                    return false;
                }
            }).get();
            assert false : "Excepting exception";
        } catch (UnsatisfiedResolutionException ignore) {
            assert true : "Excepting UnsatisfiedResolutionException";
        }
    }

    @Test
    public void getStringFallback() {
        assert "hello".equals(this.fallback);
    }

    @Test
    public void getInteger() {
    }

    @Test
    public void getOptionalInteger() {
    }

    @Test
    public void getIntegerOrFallback() {
    }

    @Test
    public void getConfigurations() {
    }
}