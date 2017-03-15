package solutions.digamma.damas.jcr.repo;

import java.util.List;

/**
 * Repository job. Repository jobs are low-level specification of content and
 * access control elements. They are used for repository initialization.
 *
 * @author Ahmad Shahwan
 */
public interface RepositoryJob {


    /**
     * List of nodes to be created.
     *
     * @return
     */
    List<Node> getCreations();

    /**
     * Node specification.
     */
    interface Node {

        String getPath();

        String getType();

        List<String> getMixins();
    }
}
