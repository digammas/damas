package solutions.digamma.damas.entity;

import java.time.ZonedDateTime;

/**
 * @author Ahmad Shahwan
 */
public interface Modifiable {

    /**
     * Modifier ID.
     *
     * @return
     */
    String getModifiedBy();

    /**
     * Modification timestamp.
     *
     * @return
     */
    ZonedDateTime getModificationDate();
}
