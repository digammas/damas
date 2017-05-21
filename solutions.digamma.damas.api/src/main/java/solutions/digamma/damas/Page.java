package solutions.digamma.damas;

import solutions.digamma.damas.inspection.NotNull;

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

    @NotNull
    List<T> getObjects();
}
