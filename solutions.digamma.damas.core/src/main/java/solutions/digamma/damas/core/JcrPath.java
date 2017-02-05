package solutions.digamma.damas.core;

import solutions.digamma.damas.api.inspection.Nonnull;

/**
 * @author Ahmad Shahwan
 */
public class JcrPath {

    /**
     * Path separator string.
     */
    public static final String SEPARATOR = "/";

    private String path;

    /**
     * Create a path from a path string.
     *
     * @param path
     */
    public JcrPath(final @Nonnull String path) {
        this.path = path;
    }

    /**
     * Append a new element to the path.
     * This method takes care of removing duplicate splashes if needed.
     *
     * @param element   Path element's name.
     * @return          JCR path with element appended at the end.
     */
    public JcrPath append(String element) {
        this.path = append(this.path, element);
        return this;
    }

    /**
     * Attempts to trim an element from the beginning of a path.
     * If first element is different from {@code element}, object will remain
     * unchanged.
     *
     * @param element
     * @return
     */
    public JcrPath trim(String element) {
        this.path = trim(this.path, element);
        return this;
    }

    /**
     * Append two path strings.
     *
     * @param path
     * @param element
     * @return
     */
    public static @Nonnull String append(
            @Nonnull String path,
            @Nonnull String element) {
        return new StringBuilder()
                .append(normalize(path))
                .append(SEPARATOR)
                .append(element)
                .toString();

    }

    /**
     * Attempts to trim an element from the beginning of a path.
     * If first element is different from {@code element}, {@code path} is
     * returned unchanged.
     *
     * @param path
     * @param element
     * @return
     */
    public static @Nonnull String trim(
            @Nonnull String path,
            @Nonnull String element) {
        String normalized = normalize(element);
        if (path.startsWith(String.format("%s%s", normalized, SEPARATOR))) {
            return path.substring(
                    normalized.length(),
                    path.length() - normalized.length()
            );
        }
        return path;
    }

    @Override
    public String toString() {
        return this.path;
    }

    private static @Nonnull String normalize(@Nonnull String path) {
        if (!path.endsWith(SEPARATOR)) {
            return path;
        } else {
            return path.substring(0, path.length() - 1);
        }
    }
}
