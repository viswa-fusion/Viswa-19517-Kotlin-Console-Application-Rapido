package infrastructure.dao.sqldatabase


import app.domain.models.Aadhaar
import app.domain.models.Passenger
import app.domain.models.customenum.BikeType
import app.domain.models.customenum.BikeType.Companion.castBikeType
import infrastructure.dao.AadhaarDao
import infrastructure.dao.PassengerDao
import infrastructure.dao.UserDao
import infrastructure.database.DbTables
import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.SQLException

class PassengerDatabase(private val aadhaarDatabase: AadhaarDao, private val connection: Connection) : PassengerDao {
    private lateinit var userDatabase: UserDao
    private lateinit var query: String
    override fun injectUser(userDatabase: UserDao){
        this.userDatabase = userDatabase
    }
    override fun insertPassenger(userId: Int, aadhaarId: Int, preferredVehicleType: BikeType) {
        query = "insert into ${DbTables.passengers.name} (`user_id`,`aadhaar_id`,`preferred_vehicle_type`)value(?,?,?)"
        connection.prepareStatement(query).use {
            it.setInt(1, userId)
            it.setInt(2, aadhaarId)
            it.setString(3, preferredVehicleType.toString())
            val result = it.executeUpdate()
            if (result == 0) {
                throw SQLException("insert passenger failed, no rows affected.")
            }
        }
    }

    override fun getPassenger(id: Int): Passenger {
        query = "SELECT * FROM ${DbTables.passengers.name} WHERE id = '$id'"
        val preparedStatement: PreparedStatement = connection.prepareStatement(query)
        val resultSet: ResultSet = preparedStatement.executeQuery()

        if (resultSet.next()) {
            val aadhaar: Aadhaar = aadhaarDatabase.getAadhaar(resultSet.getInt("aadhaar_id"))
            val user = userDatabase.fetchUser(resultSet.getInt("user_id"))
            return Passenger(
                user.username,
                user.password,
                user.name,
                user.age,
                user.phone,
                aadhaar,
                castBikeType(resultSet.getString("preferred_vehicle_type"))!!
            )
        }
        throw throw Exception("0 row matched in $query")
    }

    override fun getPassengerId(userId: Int): Int {
        return DbUtilityFunction.getTablePrimaryKey("SELECT * FROM ${DbTables.passengers.name} WHERE user_id = '${userId}'")
    }

}