package solutions.digamma.damas.text;

import java.util.IllegalFormatException;
import java.util.function.Supplier;

/**
 * Lazily formatted string supplier.
 *
 * @author Ahmad Shahwan
 */
public class LazyString implements Supplier<String> {

    private String format;
    private Object[] args;

    public LazyString(String format, Object... args) {
        this.format = format;
        this.args = args;
    }

    @Override
    public String get() {
        try {
            return String.format(this.format, this.args);
        } catch (IllegalFormatException e) {
            return this.format;
        }
    }
}
