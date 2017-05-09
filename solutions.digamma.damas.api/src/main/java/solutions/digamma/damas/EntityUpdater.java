package solutions.digamma.damas;

/**
 * Entity updater.
 *
 * @author Ahmad Shahwan
 */
public interface EntityUpdater<T extends Entity> {

    /**
     * Update file with file information.
     *
     * @param entity
     * @throws DocumentException
     */
    void update(Entity entity) throws DocumentException;
}
