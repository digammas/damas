package solutions.digamma.damas.jcr.test;

import org.modeshape.common.collection.SimpleProblems;
import org.modeshape.jcr.CndImporter;
import org.modeshape.jcr.ExecutionContext;
import org.modeshape.jcr.ModeShapeEngine;
import org.modeshape.jcr.RepositoryConfiguration;
import solutions.digamma.damas.logging.Logbook;

import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.jcr.Repository;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.nodetype.NodeTypeDefinition;
import javax.jcr.nodetype.NodeTypeManager;
import java.io.IOException;
import java.util.List;

/**
 * ModeShape JCR in-mem repository provider.
 *
 * @author Ahmad Shahwan
 */
public class RepositoryProvider {

    @Inject
    private Logbook logger;

    /**
     * Returns a JCR repository.
     *
     * @return
     */
    @Singleton @Produces
    public Repository getRepository() throws RepositoryException, IOException {
        this.logger.info("Starting ModeShape.");
        ModeShapeEngine engine = new ModeShapeEngine();
        engine.start();
        RepositoryConfiguration conf = RepositoryConfiguration.read(
                this.getClass().getResource("/repository/in_mem_repo.json"));
        Repository repository = engine.deploy(conf);
        CndImporter cnd = new CndImporter(ExecutionContext.DEFAULT_CONTEXT);
        SimpleProblems problems = new SimpleProblems();
        cnd.importFrom(
                this.getClass().getResourceAsStream("repository/cdn/damas.cdn"),
                problems, null);
        List<NodeTypeDefinition> types = cnd.getNodeTypeDefinitions();
        Session session = repository.login();
        NodeTypeManager manager = session.getWorkspace().getNodeTypeManager();
        manager.registerNodeTypes(
                types.toArray(new NodeTypeDefinition[0]), true);
        session.logout();
        return repository;
    }
}
