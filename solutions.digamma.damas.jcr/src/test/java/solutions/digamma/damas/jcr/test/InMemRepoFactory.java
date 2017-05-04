package solutions.digamma.damas.jcr.test;

import org.modeshape.common.collection.SimpleProblems;
import org.modeshape.jcr.CndImporter;
import org.modeshape.jcr.ExecutionContext;
import solutions.digamma.damas.logging.Logbook;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.jcr.Repository;
import javax.jcr.RepositoryException;
import javax.jcr.RepositoryFactory;
import javax.jcr.Session;
import javax.jcr.nodetype.NodeTypeDefinition;
import javax.jcr.nodetype.NodeTypeManager;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.ServiceLoader;

/**
 * In-memory repository factory.
 * 
 * @author Ahmad Shahwan
 */
@Singleton
public class InMemRepoFactory implements RepositoryFactory {

    private static final String JCR_URL = "org.modeshape.jcr.URL";

    @Inject
    private Logbook logger;

    @Override
    public Repository getRepository(Map params) throws RepositoryException {
        this.logger.info("Acquiring JCR repository from Java service loader.");
        Repository repository;
        for (RepositoryFactory factory :
                ServiceLoader.load(RepositoryFactory.class)) {
            this.logger.info("JCR repository factory service found.");
            repository = factory.getRepository(this.getParameters(params));
            if (repository == null) {
                continue;
            }
            this.logger.info("JCR repository implementation found.");
            CndImporter cnd = new CndImporter(ExecutionContext.DEFAULT_CONTEXT);
            SimpleProblems problems = new SimpleProblems();
            try {
                InputStream url = this.getClass()
                    .getResourceAsStream("repository/cdn/damas.cdn");
                cnd.importFrom(url, problems, null);
            } catch (IOException e) {
                throw new RepositoryException("CND file not found.", e);
            }
            List<NodeTypeDefinition> types = cnd.getNodeTypeDefinitions();
            Session session = repository.login();
            NodeTypeManager manager = session.getWorkspace()
                    .getNodeTypeManager();
            manager.registerNodeTypes(
                    types.toArray(new NodeTypeDefinition[0]), true);
            session.logout();
            return repository;
        }
        this.logger.severe("No JCR implementation found.");
        throw new RepositoryException("No repository factory service found.");
    }

    /**
     * Prepare repository parameters.
     *
     * @return
     */
    private Map<String, Object> getParameters(Map params) {
        String url = this.getClass().getResource("/repository/in_mem_repo.json")
                .toString();
        this.logger.info("JCR repository home URL is set to %s.", url);
        params.put(JCR_URL, url);
        return params;
    }
}
