package solutions.digamma.damas.rs.serialization;

import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.namespace.QName;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

/**
 * Non-JAXB-annotated message body writer.
 *
 * @author Ahmad Shahwan
 */
@Provider
@Produces(MediaType.APPLICATION_XML)
public class XmlMessageBodyWriter
        implements MessageBodyWriter<Object> {

    @Override
    public boolean isWriteable(
            Class<?> klass,
            Type type,
            Annotation[] annotations,
            MediaType mediaType) {
        return !klass.isAnnotationPresent(XmlRootElement.class);
    }

    @Override
    public long getSize(
            Object o,
            Class<?> klass,
            Type type,
            Annotation[] annotations,
            MediaType mediaType) {
        return -1;
    }

    @Override
    public void writeTo(
            Object o,
            Class<?> klass,
            Type type,
            Annotation[] annotations,
            MediaType mediaType,
            MultivaluedMap<String, Object> multivaluedMap,
            OutputStream outputStream)
            throws IOException, WebApplicationException {
        try {
            JAXBContext context = JAXBContext.newInstance(klass);
            Marshaller marshaller = context.createMarshaller();
            String name = klass.getSimpleName();
            JAXBElement<?> root = new JAXBElement(new QName(name), klass, o);

            marshaller.marshal(root, outputStream);
        } catch (JAXBException e) {
            throw new IOException("Error writing XML entity.", e);
        }
    }
}
