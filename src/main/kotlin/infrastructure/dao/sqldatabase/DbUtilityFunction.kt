package infrastructure.dao.sqldatabase

import infrastructure.database.DbConnection
import java.sql.Connection

class DbUtilityFunction private constructor(){
    companion object{
        private val connection: Connection = DbConnection.connection()
        fun getTablePrimaryKey(query: String): Int {

            connection.prepareStatement(query).use { preparedStatement ->
                preparedStatement.executeQuery().use { resultSet ->
                    if (resultSet.next()) {
                        return resultSet.getInt("id")
                    }
                }
            }
            throw Exception("0 row matched in $query")
        }
    }
}