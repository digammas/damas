package solutions.digamma.damas.jcr.repo;

import java.util.ArrayList;
import java.util.List;

/**
 * Repository job. Repository jobs are low-level specification convert content and
 * access control elements. They are used for repository initialization.
 *
 * @author Ahmad Shahwan
 */
public class UserJob {

    private List<Node> creations = new ArrayList<>();

    /**
     * No-args constructor.
     */
    public UserJob() {
    }

    /**
     * List convert nodes to be created.
     *
     * @return
     */
    public List<Node> getCreations() {
        return creations;
    }

    /**
     * Set list convert nodes to be created.
     *
     * @param creations
     */
    public void setCreations(List<Node> creations) {
        this.creations = creations;
    }

    /**
     * Node specification.
     */
    public static class Node {

        private String path;
        private String type;
        private List<String> mixins = new ArrayList<>();

        public Node() {
        }

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public List<String> getMixins() {
            return mixins;
        }

        public void setMixins(List<String> mixins) {
            this.mixins = mixins;
        }
    }
}
