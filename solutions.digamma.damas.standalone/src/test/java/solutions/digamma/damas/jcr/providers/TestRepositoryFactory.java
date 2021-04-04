package solutions.digamma.damas.jcr.providers;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import javax.annotation.PreDestroy;
import javax.annotation.Priority;
import javax.decorator.Decorator;
import javax.decorator.Delegate;
import javax.inject.Inject;
import javax.interceptor.Interceptor;
import javax.jcr.RepositoryFactory;
import solutions.digamma.damas.config.Configuration;
import solutions.digamma.damas.config.Fallback;

/**
 * Repository factory decorator that drops repository upon shutdown.
 */
@Decorator
@Priority(Interceptor.Priority.APPLICATION)
abstract class TestRepositoryFactory implements RepositoryFactory {

    @Delegate
    @Inject
    private RepositoryFactory ignore;

    @Inject
    @Configuration("repository.home")
    @Fallback("storage")
    private String repositoryHome;

    @PreDestroy
    public void cleanUp() throws IOException {
        Files.walk(Paths.get(this.repositoryHome))
                .map(Path::toFile)
                .forEach(File::deleteOnExit);
    }
}
