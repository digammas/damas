package solutions.digamma.damas.rs.serialization;

import javax.ws.rs.core.Feature;
import javax.ws.rs.core.FeatureContext;
import javax.ws.rs.ext.Provider;

/**
 * Non-JABX-annotated serialization feature.
 *
 * @author Ahmad Shahwan
 */
@Provider
public class XmlMessageBodyFeature implements Feature {

    @Override
    public boolean configure(FeatureContext featureContext) {
        featureContext.register(XmlMessageBodyReader.class);
        featureContext.register(XmlMessageBodyWriter.class);
        return true;
    }
}
