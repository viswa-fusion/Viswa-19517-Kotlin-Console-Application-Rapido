package infrastructure.dao.sqldatabase

import app.domain.models.Bike
import infrastructure.dao.BikeDao
import infrastructure.dao.RcBookDao
import infrastructure.database.DbTables
import java.sql.*

class BikeDatabase(private val rcBookDatabase: RcBookDao, private val connection: Connection) :BikeDao {
    private lateinit var query: String

    override fun insertBike(bike: Bike): Int {
        query = "insert into ${DbTables.BIKE.name} (`rc_book_id`,`used_year`)value(?,?)"
        connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS).use {
            val rcBookId: Int= rcBookDatabase.insertRcBook(bike.rcBook)
            it.setInt(1,rcBookId)
            it.setInt(2,bike.usedYear)
            val result = it.executeUpdate()
            if (result == 0) {
                throw SQLException("Insert Bike failed, no rows affected.")
            }
            val generatedKeys = it.generatedKeys
            if (generatedKeys.next()) {
                return generatedKeys.getInt(1)
            } else {
                throw SQLException("Insert Bike failed, no ID obtained.")
            }
        }
    }
    override fun getBike(id: Int): Bike {
        query = "SELECT * FROM ${DbTables.BIKE.name} WHERE id = '$id'"
        val preparedStatement: PreparedStatement = connection.prepareStatement(query)
        val resultSet: ResultSet = preparedStatement.executeQuery()
        if (resultSet.next()) {
            return Bike(
                rcBookDatabase.getRcBook(resultSet.getInt("rc_book_id")),
                resultSet.getInt("used_year")
            )
        }
        throw throw Exception("0 row matched in $query")
    }

}