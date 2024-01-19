package infrastructure.dao.sqldatabase


import app.domain.models.response.RepositoryResponse
import infrastructure.dao.BikeDao
import infrastructure.dao.DriverDao
import infrastructure.dao.LicenseDao
import infrastructure.dao.UserDao
import infrastructure.database.DbTables
import app.domain.models.Driver
import java.sql.*


class DriverDatabase(private val licenseDatabase: LicenseDao, private val bikeDatabase: BikeDao, private val connection: Connection):
    DriverDao {
    private lateinit var userDatabase: UserDao
    private lateinit var query: String
    override fun injectUser(userDatabase: UserDao){
        this.userDatabase =userDatabase
    }
    override fun insertDriver(userId: Int, licenseId: Int, bikeId: Int): RepositoryResponse {
        query = "insert into ${DbTables.drivers.name} (`user_id`,`license_id`,`bike_id`) value(?,?,?)"
        connection.prepareStatement(query).use {
            it.setInt(1,userId)
            it.setInt(2,licenseId)
            it.setInt(3,bikeId)
            val result = it.executeUpdate()
            if (result == 0) {
                return RepositoryResponse.OperationUnsuccessful("unable to add driver into $query in insert ride function")
            }
            return RepositoryResponse.SuccessfullyCreated
        }
    }

    override fun getDriver(id: Int): Driver? {
        query = "SELECT * FROM ${DbTables.drivers.name} WHERE id = '$id'"
        val preparedStatement: PreparedStatement = connection.prepareStatement(query)
        val resultSet: ResultSet = preparedStatement.executeQuery()

        if (resultSet.next()) {
            val user = userDatabase.fetchUser(resultSet.getInt("user_id"))
            return Driver(
                user.username,
                user.password,
                user.name,
                user.age,
                user.phone,
                licenseDatabase.getLicense(resultSet.getInt("license_id")),
                bikeDatabase.getBike(resultSet.getInt("bike_id"))
            )
        }
        return null
    }

    override fun getDriverId(userId: Int): Int {
        return DbUtilityFunction.getTablePrimaryKey("SELECT * FROM ${DbTables.drivers.name} WHERE user_id = '${userId}'")
    }
}