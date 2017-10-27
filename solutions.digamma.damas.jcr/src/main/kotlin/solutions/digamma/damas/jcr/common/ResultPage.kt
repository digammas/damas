package solutions.digamma.damas.jcr.common

import solutions.digamma.damas.entity.Entity
import solutions.digamma.damas.entity.Page

/**
 * Generic content page.
 *
 * @param content       content of the result page
 * @param total         total size of the result page
 *
 * @author Ahmad Shahwan
 */
class ResultPage<T : Entity> (
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
