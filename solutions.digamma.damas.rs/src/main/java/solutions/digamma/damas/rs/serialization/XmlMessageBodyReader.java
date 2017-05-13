package solutions.digamma.damas.rs.serialization;

import javax.ws.rs.Consumes;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.Provider;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.transform.stream.StreamSource;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;

/**
 * Non-JAXB-annotated message body reader.
 *
 * @author Ahmad Shahwan
 */
@Provider
@Consumes(MediaType.APPLICATION_XML)
public class XmlMessageBodyReader
        implements MessageBodyReader<Object> {

    @Override
    public boolean isReadable(
            Class<?> klass,
            Type type,
            Annotation[] annotations,
            MediaType mediaType) {
        return !klass.isAnnotationPresent(XmlRootElement.class);
    }

    @Override
    public Object readFrom(
            Class<Object> klass,
            Type type,
            Annotation[] annotations,
            MediaType mediaType,
            MultivaluedMap<String, String> multivaluedMap,
            InputStream inputStream)
            throws IOException, WebApplicationException {
        try (InputStreamReader streamReader = new InputStreamReader(
                inputStream, StandardCharsets.UTF_8)) {

            JAXBContext context = JAXBContext.newInstance(klass);

            Unmarshaller unmarshaller = context.createUnmarshaller();
            JAXBElement<?> root = unmarshaller.unmarshal(
                    new StreamSource(streamReader), klass);
            return root.getValue();

        } catch (JAXBException e) {
            throw new IOException("Error reading XML entity.", e);
        }
    }
}
