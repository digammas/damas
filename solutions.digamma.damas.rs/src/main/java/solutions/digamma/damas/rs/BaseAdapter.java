package solutions.digamma.damas.rs;

import javax.xml.bind.annotation.adapters.XmlAdapter;

/**
 * @author Ahmad Shahwan
 */
abstract public class BaseAdapter <S extends E, E> extends XmlAdapter<S, E> {

    @Override
    public E unmarshal(S value) throws Exception {
        return value;
    }
}
