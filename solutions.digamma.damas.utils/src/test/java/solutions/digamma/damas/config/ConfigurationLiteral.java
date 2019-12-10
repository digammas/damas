package solutions.digamma.damas.config;

import javax.enterprise.util.AnnotationLiteral;

public class ConfigurationLiteral
        extends AnnotationLiteral<Configuration>
    implements Configuration {

    private String[] value;
    private boolean optional;

    public ConfigurationLiteral() {
        this(false);
    }

    public ConfigurationLiteral(String... value) {
        this(false, value);
    }

    public ConfigurationLiteral(boolean optional, String... value) {
        this.value = value;
        this.optional = optional;
    }

    @Override
    public String[] value() {
        return this.value;
    }

    @Override
    public boolean optional() {
        return this.optional;
    }
}
