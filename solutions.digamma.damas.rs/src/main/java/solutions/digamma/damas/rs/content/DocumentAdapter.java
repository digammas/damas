package solutions.digamma.damas.rs.content;

import solutions.digamma.damas.content.Document;
import solutions.digamma.damas.rs.BaseAdapter;

/**
 * @author Ahmad Shahwan
 */
public class DocumentAdapter
        extends BaseAdapter<DocumentSerialization, Document> {

    @Override
    public DocumentSerialization marshal(Document value) throws Exception {
        return new DocumentSerialization(value);
    }
}
