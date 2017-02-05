package solutions.digamma.damas.api;

import solutions.digamma.damas.api.inspection.Nonnull;

import java.util.List;

/**
 * Page of objects, with an offset and a size.
 * The page also contains information about the total size of search.
 *
 * @auther Ahmad Shahwan
 */
public interface Page<T> {

    int getTotal();

    int getSize();

    int getOffset();

    @Nonnull List<T> getObjects();
}
