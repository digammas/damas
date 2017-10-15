package solutions.digamma.damas.jcr.common;

import solutions.digamma.damas.entity.Entity;
import solutions.digamma.damas.entity.Page;

import java.util.List;

/**
 * Generic content page.
 *
 * @author Ahmad Shahwan
 */
public class ResultPage<T extends Entity> implements Page<T> {

    private final List<T> content;
    private final int offset;
    private final int total;

    /**
     * Constructor.
     *
     * @param content
     * @param total
     */
    public ResultPage(List<T> content, int offset, int total) {
        this.content = content;
        this.offset = offset;
        this.total = total;
    }

    @Override
    public int getTotal() {
        return this.total;
    }

    @Override
    public int getSize() {
        return this.content.size();
    }

    @Override
    public int getOffset() {
        return this.offset;
    }

    @Override
    public List<T> getObjects() {
        return this.content;
    }
}
