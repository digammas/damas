package solutions.digamma.damas.entity;

import solutions.digamma.damas.common.WorkspaceException;
import solutions.digamma.damas.inspection.NotNull;

import java.util.List;

/**
 * Page of objects, with an offset and a size.
 * The page also contains information about the total size of search.
 *
 * @auther Ahmad Shahwan
 */
public interface Page<T extends Entity> {

    int getTotal();

    int getSize();

    int getOffset();

    @NotNull
    List<T> getObjects() throws WorkspaceException;
}
