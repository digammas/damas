package solutions.digamma.damas.entity;

import java.time.ZonedDateTime;

/**
 * @author Ahmad Shahwan
 */
public interface Created {

    /**
     * Creator ID.
     *
     * @return
     */
    String getCreatedBy();

    /**
     * Creation timestamp.
     *
     * @return
     */
    ZonedDateTime getCreationDate();
}

