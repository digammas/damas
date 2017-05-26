package solutions.digamma.damas.jcr.providers;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.decorator.Decorator;
import javax.decorator.Delegate;
import javax.inject.Inject;
import javax.jcr.RepositoryFactory;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Comparator;

/**
 * @author Ahmad Shahwan
 */
@Decorator
abstract public class OneTimeRepositoryFactory implements RepositoryFactory {

    @Delegate @Inject
    private RepositoryFactory factory;

    @PostConstruct
    private void deploy() throws IOException {
        Files.createDirectories(Paths.get("repository/cdn/"));
        extractResource(
                "/repository/repository.json",
                "repository/repository.json");
        extractResource(
                "/repository/cdn/damas.cdn",
                "repository/cdn/damas.cdn");
    }

    @PreDestroy
    private void clean() {
        try {
            Files.walk(Paths.get("repository"))
                    .sorted(Comparator.reverseOrder())
                    .map(Path::toFile)
                    .forEach(File::delete);
        } catch(IOException e) {
            System.err.println("Repository files couldn't be deleted.");
        }
    }

    private static void extractResource(String resourcePath, String distPath)
            throws IOException {
        try (InputStream stream = OneTimeRepositoryFactory.class
                .getResourceAsStream(resourcePath)) {
            Files.copy(
                    stream,
                    Paths.get(distPath),
                    StandardCopyOption.REPLACE_EXISTING);
        }
    }
}
