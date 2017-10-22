package solutions.digamma.damas.jcr.common

import solutions.digamma.damas.entity.Entity
import solutions.digamma.damas.entity.Page

/**
 * Generic content page.
 *
 * @author Ahmad Shahwan
 */
class ResultPage<T : Entity>
/**
 * Constructor.
 *
 * @param content
 * @param total
 */
(
        private val content: List<T>,
        private val offset: Int, private val total: Int) : Page<T> {

    override fun getTotal(): Int {
        return this.total
    }

    override fun getSize(): Int {
        return this.content.size
    }

    override fun getOffset(): Int {
        return this.offset
    }

    override fun getObjects(): List<T> {
        return this.content
    }
}
