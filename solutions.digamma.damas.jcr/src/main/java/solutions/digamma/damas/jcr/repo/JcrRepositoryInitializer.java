package solutions.digamma.damas.jcr.repo;

import javax.annotation.PostConstruct;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Repository initializer.
 * A best effort is applied when initializing repository. That means that errors
 * are ignored, and unless the error is fatal, the rest of the current job, as
 * well as the rest of the jobs, are still attempted.
 *
 * @author Ahmad Shahwan
 */
@Singleton
public class JcrRepositoryInitializer implements RepositoryInitializer {

    /**
     * Lock used for thread safety.
     * @see "http://stackoverflow.com/questions/1040818/python-event-equivalent-in-java"
     */
    private Lock lock = new ReentrantLock();
    private Condition condition = lock.newCondition();
    private boolean isReady = false;
    private List<RepositoryJob> jobs;

    @Inject
    private JcrRepository repository;

    @Inject
    private Logger logger;

    @Inject
    private Event<RepositoryReadyEvent> repositoryReadyEventEvent;

    /**
     * No-args constructor.
     */
    public JcrRepositoryInitializer() {
    }

    /**
     * Prepare repository for use.
     */
    @PostConstruct
    public void initialize() {
        this.logger.info("Initializing JCR repository.");
        this.collectJobs();
        this.logger.info(() -> String.format(
                "%d jobs collected", this.jobs.size()));
        Session superuser = null;
        try {
            superuser = this.repository.getSuperuserSession();
            for (RepositoryJob job : this.jobs) {
                for (RepositoryJob.Node node : job.getCreations()) {
                    try {
                        Node jcrRoot = superuser.getRootNode();
                        String path = URI
                                .create(jcrRoot.getPath())
                                .relativize(URI.create(node.getPath()))
                                .getPath();
                        Node jcrNode;
                        if (jcrRoot.hasNode(path)) {
                            jcrNode = jcrRoot.getNode(path);
                            this.logger.info(() -> String.format(
                                "Node %s already exists.", node.getPath()));
                        } else {
                            jcrNode = jcrRoot.addNode(
                                    path, node.getType());
                            String jcrPath = jcrNode.getPath();
                            this.logger.info(() -> String.format(
                                "Node %s added.", jcrPath));
                        }
                        for (String mixin : node.getMixins()) {
                            try {
                                jcrNode.addMixin(mixin);
                            } catch (RepositoryException e) {
                                this.logger.warning(() -> String.format(
                                    "Repo job unable to add mixin %s.", mixin));
                            }
                        }
                    } catch (RepositoryException e) {
                        this.logger.log(Level.SEVERE, e, () -> String.format(
                            "Repo job error adding node %s.", node.getPath()));
                    }
                }
            }
        } catch (RepositoryException e) {
            this.logger.log(
                Level.SEVERE, "Repo job unable to connect as admin.", e);
        } finally {
            try {
                if (superuser != null) {
                    superuser.save();
                }
            } catch (RepositoryException e) {
                this.logger.log(
                    Level.SEVERE, "Repo job unable to save session.", e);
            }
        }
        this.lock.lock();
        try {
            this.isReady = true;
            condition.signalAll();
        }  finally {
            this.lock.unlock();
        }

        this.repositoryReadyEventEvent.fire(new RepositoryReadyEvent());
    }

    private List<RepositoryJob> collectJobs() {
        this.jobs = new ArrayList<>(1);
        this.jobs.add(BuiltInJob.INSTANCE);
        return this.jobs;
    }

    @Override
    public void waitUntilDone() throws InterruptedException {
        this.lock.lock();
        try {
            while (!this.isReady) {
                condition.await();
            }
        } finally {
            this.lock.unlock();
        }
    }

    @Override
    public boolean waitUntilDone(long timeout) throws InterruptedException {
        this.lock.lock();
        try {
            if (!this.isReady) {
                condition.await(timeout, TimeUnit.SECONDS);
            }
            return this.isReady;
        } finally {
            this.lock.unlock();
        }
    }
}
