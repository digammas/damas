package solutions.digamma.damas.cdi;

import org.jboss.weld.environment.se.Weld;
import org.jboss.weld.environment.se.WeldContainer;
import solutions.digamma.damas.rs.providers.JerseyProvider;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Comparator;

/**
 * Application entry point.
 *
 * @author Ahmad Shahwan
 */
public class WeldProvider {

    public void setUp() throws IOException {
        Files.createDirectories(Paths.get("repository/cdn/"));
        this.extractResource(
                "/repository/repository.json",
                "repository/repository.json");
        this.extractResource(
                "/repository/cdn/damas.cdn",
                "repository/cdn/damas.cdn");
    }

    public void tearDown() {
        try {
            Files.walk(Paths.get("repository"))
                    .sorted(Comparator.reverseOrder())
                    .map(Path::toFile)
                    .forEach(File::delete);
        } catch (IOException e) {
            System.err.println("IO Exception while cleaning up.");
        }
    }

    public void run() {
        try (WeldContainer weld = new Weld().initialize()) {
            this.setUp();
            if (weld.select(JerseyProvider.class).get() == null) {
                System.err.println("No REST provider found.");
            } else {
                Thread.currentThread().join();
            }
        } catch (InterruptedException e) {
        } catch (IOException e) {
            System.err.println("IO error while preparing environment.");
        } finally {
            this.tearDown();
        }
    }

    public static void main(String[] argv) {
        new WeldProvider().run();
    }

    static private void extractResource(String resourcePath, String distPath)
            throws IOException {
        try (InputStream stream = WeldProvider.class
                .getResourceAsStream(resourcePath)) {
            Files.copy(
                    stream,
                    Paths.get(distPath),
                    StandardCopyOption.REPLACE_EXISTING);
        }
    }
}
