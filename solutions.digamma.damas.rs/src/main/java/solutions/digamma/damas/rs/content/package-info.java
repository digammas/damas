/**
 * @author Ahmad Shahwan
 */
@XmlJavaTypeAdapters({
        @XmlJavaTypeAdapter(value=DocumentAdapter.class, type=Document.class),
        @XmlJavaTypeAdapter(value=FolderAdapter.class, type=Folder.class)
})
package solutions.digamma.damas.rs.content;

import solutions.digamma.damas.content.Document;
import solutions.digamma.damas.content.Folder;

import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapters;