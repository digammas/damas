package solutions.digamma.damas.config;

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

    static  {
        System.setProperty("greeting", "hello");
        System.setProperty("salutation", "bonjour");
        System.setProperty("bool", "true");
        System.setProperty("integer", "42");
    }

    @Inject
    private Instance<String> strings;

    @Inject
    private Instance<Boolean> booleans;

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

    @Inject
    @Configuration({"greeting", "salutation"})
    private String hello;

    @Inject
    @Configuration({"salutation", "greeting"})
    private String bonjour;

    @Inject
    @Configuration({"missing", "greeting"})
    private String helloToo;

    @Inject
    @Configuration("bool")
    private Boolean bool;

    @Inject
    @Configuration(value = "optionalBool", optional = true)
    private Boolean optionalBool;

    @Inject
    @Configuration(value = "trueBool")
    @Fallback(Configurations.TRUE)
    private Boolean trueBool;

    @Inject
    @Configuration("fallbackBool")
    @Fallback("true")
    private Boolean fallbackBool;

    @Inject
    @Configuration("integer")
    private Integer integer;

    @Inject
    @Configuration(value = "optionalInteger", optional = true)
    private Integer optionalInteger;

    @Inject
    @Configuration("fallbackInteger")
    @Fallback("314")
    private Integer fallbackInteger;

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
            strings
                    .select(String.class, new ConfigurationLiteral("missing"))
                    .get();
            assert false : "Excepting exception";
        } catch (UnsatisfiedResolutionException ignore) {
            assert true : "Excepting UnsatisfiedResolutionException";
        }
    }

    @Test
    public void getMultipleConfiguration() {
        assert "hello".equals(this.hello) :
                "Error acquiring multiple config in order";
        assert "bonjour".equals(this.bonjour) :
                "Error acquiring multiple config in order";
        assert "hello".equals(this.helloToo) :
                "Error acquiring multiple config";
    }

    @Test
    public void getStringFallback() {
        assert "hello".equals(this.fallback);
    }

    @Test
    public void getInteger() {
        assert this.integer == 42: "Unable to read integer configuration";
    }

    @Test
    public void getOptionalInteger() {
        assert this.optionalInteger == null :
                "Unable to read integer configuration";
    }

    @Test
    public void getIntegerFallback() {
        assert this.fallbackInteger == 314 :
                "Unable to read fallback integer configuration";
    }

    @Test
    public void getConfigurations() {
    }

    @Test
    public void getBoolean() {
        assert this.bool : "Unable to read boolean configuration";
    }

    @Test
    public void getUnsatisfiedBoolean() {
        try {
            booleans
                    .select(Boolean.class, new ConfigurationLiteral("missing"))
                    .get();
            assert false : "Excepting exception";
        } catch (UnsatisfiedResolutionException ignore) {
            assert true : "Excepting UnsatisfiedResolutionException";
        }
        assert this.bool : "Unable to read boolean configuration";
    }

    @Test
    public void getOptionalBoolean() {
        assert this.optionalBool == null :
                "Unable to read optional boolean configuration";
    }

    @Test
    public void getTrueBoolean() {
        assert this.trueBool :
                "Unable to read default true boolean configuration";
    }

    @Test
    public void getBooleanFallback() {
        assert this.fallbackBool:
                "Unable to read fallback boolean configuration";
    }
}
