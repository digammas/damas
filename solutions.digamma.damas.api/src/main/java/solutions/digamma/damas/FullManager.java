package solutions.digamma.damas;

/**
 * Entity full manager.
 *
 * @auther Ahmad Shahwan
 */
public interface FullManager<T extends Entity>
        extends CrudManager<T>, SearchManager<T> {

}
