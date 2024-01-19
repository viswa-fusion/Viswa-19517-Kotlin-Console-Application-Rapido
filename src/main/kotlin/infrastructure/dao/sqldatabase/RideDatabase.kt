package infrastructure.dao.sqldatabase

//<<<<<<< Updated upstream:src/main/kotlin/datalayer/sqldatabase/RideDatabase.kt
//import backend.UtilityFunction
//import backend.UtilityFunction.Companion.castToLocation
//import infrastructure.dao.PassengerDao
//import infrastructure.dao.UserDao
//import infrastructure.library.DbResponse
//import infrastructure.datalayer.DbTables
//import app.domain.models.customenum.Location
//import app.domain.models.customenum.RideStatus
//import app.domain.models.Driver
//import app.domain.models.Passenger
//import app.domain.models.Ride
//import app.domain.models.User
//=======

//>>>>>>> Stashed changes:src/main/kotlin/infrastructure/dao/sqldatabase/RideDatabase.kt
import app.domain.models.Driver
import app.domain.models.Passenger
import app.domain.models.Ride
import app.domain.models.User
import app.domain.models.customenum.Location
import app.domain.models.customenum.Location.Companion.castLocation
import app.domain.models.customenum.RideStatus
import app.domain.models.customenum.RideStatus.Companion.castToRideStatus
import app.domain.models.response.RepositoryResponse
import infrastructure.dao.DriverDao
import infrastructure.dao.PassengerDao
import infrastructure.dao.RideDao
import infrastructure.dao.UserDao
import infrastructure.database.DbTables
import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet

class RideDatabase(
    private val passengerDatabase: PassengerDao,
    private val driverDatabase: DriverDao,
    private val userDatabase: UserDao,
    private val connection: Connection
) : RideDao {
    private lateinit var query: String

    override fun insertRide(ride: Ride, passengerId: Int): RepositoryResponse {
        query =
            "insert into ${DbTables.rides.name} (`passenger_id`,`pickup_location`,`drop_location`,`status`,`total_charge`)value(?,?,?,?,?)"
        connection.prepareStatement(query).use {
            it.setInt(1, passengerId)
            it.setString(2, ride.pickup_location.toString())
            it.setString(3, ride.drop_location.toString())
            it.setString(4, ride.status.toString())
            it.setDouble(5, ride.total_charge)
            val result = it.executeUpdate()
            if (result == 0) {
                return RepositoryResponse.OperationUnsuccessful(
                    "unable to add $ride into ${DbTables.users.name} in insert ride function"
                )
            }
            return RepositoryResponse.SuccessfullyCreated
        }
    }


    override fun getRide(id: Int, user: User): Ride? {
        return when (user) {
            is Passenger -> {
                query =
                    "SELECT * FROM ${DbTables.rides} " +
                            "WHERE passenger_id ='$id' " +
                            "AND status NOT IN ('${RideStatus.COMPLETED.name}', '${RideStatus.CANCEL.name}')"

                getMyRide(query)
            }

            is Driver -> {
                query =
                    "SELECT * FROM ${DbTables.rides} " +
                            "WHERE driver_id ='$id' " +
                            "AND status NOT IN ('${RideStatus.COMPLETED.name}', '${RideStatus.CANCEL.name}', '${RideStatus.CREATED}')"
                getMyRide(query)
            }

            else -> null
        }
    }

    private fun getMyRide(query: String): Ride? {


        val preparedStatement: PreparedStatement = connection.prepareStatement(query)
        val resultSet: ResultSet = preparedStatement.executeQuery()
        if (resultSet.next()) {
            return Ride(
                passengerDatabase.getPassenger(resultSet.getInt("passenger_id")),
                driverDatabase.getDriver(resultSet.getInt("driver_id")),
                castLocation(resultSet.getString("pickup_location"))!!,
                castLocation(resultSet.getString("drop_location"))!!,
                resultSet.getString("start_time"),
                resultSet.getString("end_time"),
                castToRideStatus(resultSet.getString("status"))!!,
                resultSet.getDouble("total_charge"),
            )
        }
        return null
    }

    override fun getRideWithPickUpLocation(nearLocation: MutableMap<Location, Int>): List<Ride> {
        val listOfRide = ArrayList<Ride>()

        nearLocation.forEach { (location, _) ->
            query = "SELECT * FROM ${DbTables.rides} " +
                    "WHERE pickup_location ='${location.name}' " +
                    "AND status = '${RideStatus.CREATED.name}'"
            val preparedStatement: PreparedStatement = connection.prepareStatement(query)
            val resultSet: ResultSet = preparedStatement.executeQuery()
            while (resultSet.next()) {
                val ride = Ride(
                    passengerDatabase.getPassenger(resultSet.getInt("passenger_id")),
                    driverDatabase.getDriver(resultSet.getInt("driver_id")),
                    castLocation(resultSet.getString("pickup_location"))!!,
                    castLocation(resultSet.getString("drop_location"))!!,
                    resultSet.getString("start_time"),
                    resultSet.getString("end_time"),
                    castToRideStatus(resultSet.getString("status"))!!,
                    resultSet.getDouble("total_charge")
                )
                listOfRide.add(ride)
            }
        }
        return listOfRide
    }
//
//<<<<<<< Updated upstream:src/main/kotlin/datalayer/sqldatabase/RideDatabase.kt
//    override fun checkUserHaveRide(loggedUserid: Int): Boolean {
//=======
    override fun checkUserHaveRide(loggedUserid: Int): RepositoryResponse {
//>>>>>>> Stashed changes:src/main/kotlin/infrastructure/dao/sqldatabase/RideDatabase.kt
        query =
            "SELECT * FROM ${DbTables.rides} " +
                    "WHERE passenger_id = '$loggedUserid' OR driver_id = '$loggedUserid' " +
                    "AND status NOT IN ('${RideStatus.COMPLETED.name}', '${RideStatus.CANCEL.name}')"

        val preparedStatement: PreparedStatement = connection.prepareStatement(query)
        val resultSet: ResultSet = preparedStatement.executeQuery()
//<<<<<<< Updated upstream:src/main/kotlin/datalayer/sqldatabase/RideDatabase.kt
//        return resultSet.next()
//    }
//
//    override fun updateDriverInRide(rideId: Int, driverId: Int): DbResponse{
//        query = "UPDATE ${DbTables.rides.name} SET driver_id = '${driverId}' WHERE id = '${rideId}'"
//=======

        return if (resultSet.next())
            RepositoryResponse.SuccessfullyCreated
        else
            RepositoryResponse.OperationUnsuccessful("unable to fetch Data")
    }

    override fun updateDriverInRide(rideId: Int, driverId: Int): RepositoryResponse {
        query =
            "UPDATE ${DbTables.rides.name} " +
                    "SET driver_id = '${driverId}', status ='${RideStatus.BOOKED.name}' WHERE id = '${rideId}'"
//>>>>>>> Stashed changes:src/main/kotlin/infrastructure/dao/sqldatabase/RideDatabase.kt
        connection.prepareStatement(query).use {
            val result = it.executeUpdate()
            if (result == 0) {
                return RepositoryResponse.OperationUnsuccessful(
                    "unable to update into ${DbTables.rides.name} in insert ride function"
                )
            }
            return RepositoryResponse.SuccessfullyUpdated
        }
    }

//<<<<<<< Updated upstream:src/main/kotlin/datalayer/sqldatabase/RideDatabase.kt
//    override fun updateRide(ride: Ride): DbResponse {
//        val passengerId: Int? =if(ride.passenger == null) null else passengerDatabase.getPassengerId(userDatabase.getUserId(ride.passenger))
//        val driverId: Int = driverDatabase.getDriverId(userDatabase.getUserId(ride.driver!!))
//        val rideId :Int = getRideId(passengerId!!)
//=======
    override fun updateRideStatus(rideId: Int, status: RideStatus): RepositoryResponse {
        query = "UPDATE ${DbTables.rides.name} SET status ='${status.name}' WHERE id = '${rideId}'"
        connection.prepareStatement(query).use {
            val result = it.executeUpdate()
            if (result == 0) {
                return RepositoryResponse.OperationUnsuccessful(
                    "unable to update into ${DbTables.rides.name} in insert ride function"
                )
            }
            return RepositoryResponse.SuccessfullyUpdated
        }
    }

    override fun cancelRide(rideId: Int): RepositoryResponse {
        query = "UPDATE ${DbTables.rides.name} SET status ='${RideStatus.CANCEL.name}' WHERE id = '${rideId}'"
        connection.prepareStatement(query).use {
            val result = it.executeUpdate()
            if (result == 0) {
                return RepositoryResponse.OperationUnsuccessful(
                    "unable to update into ${DbTables.rides.name} in cancel ride function"
                )
            }
            return RepositoryResponse.SuccessfullyUpdated
        }
    }

    override fun updateRide(ride: Ride): RepositoryResponse {
        if(ride.driver == null) return RepositoryResponse.OperationUnsuccessful("passenger cannot be null")
        val passengerId: Int = passengerDatabase.getPassengerId(userDatabase.fetchUserId(ride.passenger))
        val rideId: Int = getRideId(passengerId)
//>>>>>>> Stashed changes:src/main/kotlin/infrastructure/dao/sqldatabase/RideDatabase.kt
        query =
            "UPDATE ${DbTables.rides.name} SET " +
                    "start_time = ?, " +
                    "end_time = ?, " +
                    "status = ?, " +
                    "total_charge = ? " +
                    "WHERE id = ?"

        connection.prepareStatement(query).use {
//<<<<<<< Updated upstream:src/main/kotlin/datalayer/sqldatabase/RideDatabase.kt
//            it.setInt(1, passengerId)
//            it.setInt(2, driverId)
//            it.setString(3, ride.pickup_location.toString())
//            it.setString(4, ride.drop_location.toString())
//            it.setString(5, ride.status.toString())
//            it.setDouble(6, ride.total_charge)
//            it.setInt(7, rideId)
//=======
            it.setString(1, ride.start_time)
            it.setString(2, ride.end_time)
            it.setString(3, ride.status.encode())
            it.setDouble(4, ride.total_charge)
            it.setInt(5, rideId)
//>>>>>>> Stashed changes:src/main/kotlin/infrastructure/dao/sqldatabase/RideDatabase.kt

            val result = it.executeUpdate()
            if (result == 0) {
                return RepositoryResponse.OperationUnsuccessful(
                    "unable to update $ride into ${DbTables.rides.name} in insert ride function"
                )
            }
            return RepositoryResponse.SuccessfullyUpdated
        }
    }

    override fun getRideId(passengerId: Int): Int {
        query = "SELECT * FROM ${DbTables.rides.name} " +
                "WHERE passenger_id = '${passengerId}' " +
                "AND status NOT IN ('${RideStatus.COMPLETED.name}', '${RideStatus.CANCEL.name}')"
        return DbUtilityFunction.getTablePrimaryKey(query)
    }
}