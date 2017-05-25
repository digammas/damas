package solutions.digamma.damas.jcr.providers;

import org.junit.rules.ExternalResource;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Comparator;

/**
 * Repository resource test rule.
 * This rule prepare the terrain for JCR repository, and clean up behind it.
 *
 * @author Ahmad Shahwan
 */
public class RepositoryResource extends ExternalResource {

    @Override
    protected void before() throws IOException {
        Files.createDirectories(Paths.get("repository/cdn/"));
        this.extractResource("/repository/repository.json", "repository/repository.json");
        this.extractResource("/repository/cdn/damas.cdn", "repository/cdn/damas.cdn");
    }

    @Override
    protected void after() {
        try {
            Files.walk(Paths.get("repository"))
                    .sorted(Comparator.reverseOrder())
                    .map(Path::toFile)
                    .forEach(File::delete);
        } catch(IOException e) {
            System.err.println("Repository files couldn't be deleted.");
        }
    }

    private void extractResource(String resourcePath, String distPath)
            throws IOException {
        try (InputStream stream = this
                .getClass()
                .getResourceAsStream(resourcePath)) {
            Files.copy(
                    stream,
                    Paths.get(distPath),
                    StandardCopyOption.REPLACE_EXISTING);
        }
    }
}
