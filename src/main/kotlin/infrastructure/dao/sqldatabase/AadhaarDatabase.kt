package infrastructure.dao.sqldatabase

import app.domain.models.Aadhaar
import infrastructure.dao.AadhaarDao
import infrastructure.database.DbTables
import java.sql.*

class AadhaarDatabase(private val connection: Connection ): AadhaarDao {
    private lateinit var query: String

    override fun insertAadhaar(aadhaar: Aadhaar) : Int{
        query = "insert into ${DbTables.AADHAAR.name} (`aadhaar_no`,`name`,`renewal_date`) value(?,?,?)"
        connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS).use{
            it.setString(1,aadhaar.aadhaarNo)
            it.setString(2,aadhaar.name)
            it.setString(3,aadhaar.renewalDate)
            val result = it.executeUpdate()
            if (result == 0) {
                throw SQLException("Insert aadhaar failed, no rows affected.")
            }
            val generatedKeys = it.generatedKeys
            if (generatedKeys.next()) {
                return generatedKeys.getInt(1)
            } else {
                throw SQLException("Insert aadhaar failed, no ID obtained.")
            }
        }
    }

    override fun getAadhaar(aadhaarId: Int): Aadhaar {
        query = "SELECT * FROM ${DbTables.AADHAAR.name} WHERE id = '$aadhaarId'"
        try {
            val preparedStatement: PreparedStatement = connection.prepareStatement(query)
            val resultSet: ResultSet = preparedStatement.executeQuery()
            lateinit var aadhaar: Aadhaar
            if (resultSet.next()) {
                aadhaar = Aadhaar(
                    resultSet.getString("aadhaar_no"),
                    resultSet.getString("name"),
                    resultSet.getString("renewal_date")
                )
            }
            return aadhaar
        } catch (e: SQLException) {
            throw SQLException("aadhaar not found")
        } catch (e: NullPointerException) {
            throw NullPointerException("aadhaar not found")
        }
    }
}