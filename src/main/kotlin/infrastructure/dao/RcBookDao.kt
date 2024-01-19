package infrastructure.dao

import app.domain.models.RcBook

interface RcBookDao {
    fun insertRcBook(rcBook: RcBook): Int
    fun getRcBook(id: Int): RcBook
}