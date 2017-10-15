package solutions.digamma.damas.jcr.repo;

import solutions.digamma.damas.jcr.names.TypeNamespace;
import solutions.digamma.damas.jcr.content.JcrFile;

import java.util.Arrays;
import java.util.List;

/**
 * Built-in repository job.
 *
 * @author Ahmad Shahwan
 */
public final class BuiltInJob implements RepositoryJob {

    public static final BuiltInJob INSTANCE = new BuiltInJob();

    private final static Node[] CREATIONS = {
            new Node(
                    JcrFile.ROOT_PATH,
                    TypeNamespace.FOLDER,
                    new String[0]
            )
    };

    /**
     * Constructor.
     */
    private BuiltInJob() {
    }

    /**
     * List convert nodes to be created.
     *
     * @return
     */
    public List<RepositoryJob.Node> getCreations() {
        return Arrays.asList(CREATIONS);
    }

    /**
     * Node specification.
     */
    private static class Node implements RepositoryJob.Node {

        private final String path;
        private final String type;
        private final String[] mixins;

        public Node(String path, String type, String[] mixins) {
            this.path = path;
            this.type = type;
            this.mixins = mixins;
        }

        @Override
        public String getPath() {
            return this.path;
        }

        @Override
        public String getType() {
            return this.type;
        }

        @Override
        public List<String> getMixins() {
            return Arrays.asList(this.mixins);
        }
    }
}
