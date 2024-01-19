package infrastructure.database

import app.domain.models.*
import app.domain.models.customenum.Location
import app.domain.models.customenum.RideStatus
import app.domain.models.response.AuthenticationResponse
import app.domain.models.response.RepositoryResponse
import app.domain.repository.Repository
import infrastructure.dao.*
import infrastructure.dao.DriverDao
import infrastructure.dao.RideDao
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import utility.UtilityFunction
import java.sql.SQLException

class MySqlDatabase(
    private val userDatabase: UserDao,
    private val passengerDatabase: PassengerDao,
    private val driverDatabase: DriverDao,
    private val rideDatabase: RideDao,
    private val aadhaarDatabase: AadhaarDao,
    private val licenseDatabase: LicenseDao,
    private val rcBookDatabase: RcBookDao,
    private val bikeDatabase: BikeDao,
    private val otpDatabase: OtpDao
) : Repository {

    //User DAO
    //<-------------------------------------------------------------------------------------------------------->
    override suspend fun addUser(user: User) = userDatabase.insertUser(user)
    override fun findUser(userName: String): AuthenticationResponse{
        return when(val userId: Int? = userDatabase.fetchUserId(userName)){
            null -> AuthenticationResponse.UserNotFound
            else -> {
                val user: User? =getUser(userId)
                if(user!=null){ AuthenticationResponse.UserFound(user) }
                else AuthenticationResponse.UserNotFound
            }
        }
    }
    override fun getUserId(userId: Int)= userDatabase.fetchUser(userId)
    private fun getUserId(user: User) = userDatabase.fetchUserId(user)
    override fun getUser(userId : Int) = userDatabase.fetchUserType(userId)
    override fun isUserNameExist(userName: String) = userDatabase.isUserNameExist(userName)
    override fun getUserTypeId(user: User): Int {
        val userId = getUserId(user)
        return when (user) {
            is Passenger -> getPassengerId(userId)
            is Driver -> getDriverId(userId)
            else -> throw SQLException("not a valid user")
        }
    }
    override fun isValidCredential(loginDetails: LoginDetails): AuthenticationResponse {
        if (isUserNameExist(loginDetails.userName))
            return if (userDatabase.checkPassword(loginDetails)) AuthenticationResponse.ValidCredential
            else AuthenticationResponse.InvalidPassword
        return AuthenticationResponse.InvalidUsername
    }


    //Passenger DAO
    //<-------------------------------------------------------------------------------------------------------->
    override suspend fun addNewPassenger(passenger: Passenger): RepositoryResponse {
        return try {
            coroutineScope {
                val user: User = with(passenger){User(username,password,name,age,phone)}
                val fetchingUserId = async { addUser(user) }
                val fetchingAadhaarId = async { addAadhaar(passenger.aadhaar) }

                val userId :Int = fetchingUserId.await()
                val aadhaarId :Int = fetchingAadhaarId.await()

                passengerDatabase.insertPassenger(userId, aadhaarId, passenger.preferredVehicleType)
            }
            RepositoryResponse.SuccessfullyCreated
        } catch (e: SQLException) {
            RepositoryResponse.OperationUnsuccessful(e.message)
        }
    }

    override fun getPassenger(id: Int) = passengerDatabase.getPassenger(id)
    private fun getPassengerId(userId: Int) = passengerDatabase.getPassengerId(userId)


    //Driver DAO
    //<-------------------------------------------------------------------------------------------------------->
    override suspend fun addNewDriver(driver: Driver): RepositoryResponse {
        return try {

            coroutineScope {
                val user: User = with(driver){ User(username,password,name,age,phone) }
                val fetchingUserId: Deferred<Int> = async { addUser(user) }
                val fetchingLicenseId: Deferred<Int> = async { addLicense(driver.license) }
                val fetchingBikeId: Deferred<Int> = async { addBike(driver.bike) }

                val userId: Int = fetchingUserId.await()
                val licenseId: Int = fetchingLicenseId.await()
                val bikeId: Int = fetchingBikeId.await()
                driverDatabase.insertDriver(userId, licenseId, bikeId)
            }
            RepositoryResponse.SuccessfullyCreated
        } catch (e: SQLException) {
            RepositoryResponse.OperationUnsuccessful(e.message)
        }
    }
    override fun getDriver(id: Int) = driverDatabase.getDriver(id)
    private fun getDriverId(userId: Int) = driverDatabase.getDriverId(userId)


    //Ride DAO
    //<-------------------------------------------------------------------------------------------------------->
    override fun addNewRide(user: User, ride: Ride): RepositoryResponse {
        val passengerId = getUserTypeId(user)
        return rideDatabase.insertRide(ride, passengerId)
    }

    override fun getRideByPassenger(passenger: Passenger): Ride? {
        val passengerId = getUserTypeId(passenger)
        return rideDatabase.getRide(passengerId, passenger)
    }

    override fun getRideByDriver(driver: Driver): Ride? {
        val driverId = getUserTypeId(driver)
        return rideDatabase.getRide(driverId, driver)
    }

    override fun isPassengerHavaRide(passenger: Passenger): Boolean {
        val passengerId = getUserTypeId(passenger)
        return when (rideDatabase.checkUserHaveRide(passengerId)) {
            RepositoryResponse.SuccessfullyCreated -> true
            else -> false
        }
    }

    override fun getNearByAvailableRide(currentLocation: Location): List<Ride> {
        val nearLocation = currentLocation.map
        nearLocation[currentLocation] = 0
        val rideList = rideDatabase.getRideWithPickUpLocation(nearLocation)

        rideList.forEach {
            it.total_charge = UtilityFunction.calculateDriverCharge(it.total_charge)
        }
        return rideList
    }
//
//    override fun convertToCommissionRate(totalCharge: Double): Double {
//        val commissionPercentage = 20.0
//        return totalCharge - (totalCharge * (commissionPercentage / 100.0))
//    }
//
//    override fun getAadhaar(id: Int): Aadhaar {
//        return aadhaarDatabase.getAadhaar(id)
//    }
//
//    override fun getDriver(id: Int): Driver? {
//        return driverDatabase.getDriver(id)
//    }
//
//    override fun getBike(id: Int): Bike {
//        return bikeDatabase.getBike(id)
//    }
//
//    override fun getRcBook(id: Int): RcBook {
//        return rcBookDatabase.getRcBook(id)
//    }
//
//    override fun getPassenger(id: Int): Passenger {
//        return  passengerDatabase.getPassenger(id)
//    }
//
//    override fun addRcBook(rcBook: RcBook): Int {
//        return rcBookDatabase.insertRcBook(rcBook)
//    }
//
//    override fun isLoggedUserHavaAnotherRide(loggedUserid: Int): Boolean {
//        return rideDatabase.checkUserHaveRide(loggedUserid)
//    }
//
//    override fun acceptRide(ride: Ride): DbResponse {
//        val passengerUserId =userDatabase.getUserId(ride.passenger!!)
//        val passengerId = passengerDatabase.getPassengerId(passengerUserId)
//        val driverUserId = userDatabase.getUserId(ride.driver!!)
//        val driverId = driverDatabase.getDriverId(driverUserId)
//=======
//        val nearLocation = currentLocation.getMyNeighbourLocation()
//        return rideDatabase.getRideWithPickUpLocation(nearLocation)
//    }

    override fun isDriverHaveRide(driver: Driver): Boolean {
        val driverId = getUserTypeId(driver)
        return when (rideDatabase.checkUserHaveRide(driverId)) {
            RepositoryResponse.SuccessfullyCreated -> true
            else -> false
        }
    }

    override fun acceptRide(ride: Ride): RepositoryResponse {
        val passengerUserId = getUserId(ride.passenger)
        val passengerId = getPassengerId(passengerUserId)
        val driverUserId = getUserId(ride.driver!!)
        val driverId = getDriverId(driverUserId)
        val rideId = rideDatabase.getRideId(passengerId)
        return rideDatabase.updateDriverInRide(rideId, driverId)
    }

    override fun cancelRide(passenger: Passenger): Boolean {
        val passengerUserId = getUserId(passenger)
        val passengerId = getPassengerId(passengerUserId)
        val rideId = rideDatabase.getRideId(passengerId)
        return when (rideDatabase.cancelRide(rideId)) {
            RepositoryResponse.SuccessfullyUpdated -> true
            else -> false
        }
    }

    override fun getRideId(passengerId: Int) :Int = rideDatabase.getRideId(passengerId)
    override fun updateRide(ride: Ride): RepositoryResponse = rideDatabase.updateRide(ride)

    override fun updateRideStatus(rideId: Int, rideStatus: RideStatus) = rideDatabase.updateRideStatus(rideId,rideStatus)


    //License DAO
    //<-------------------------------------------------------------------------------------------------------->
    override fun addLicense(license: License) = licenseDatabase.insertLicense(license)
    override fun getLicense(id: Int) = licenseDatabase.getLicense(id)


    //Aadhaar DAO
    //<-------------------------------------------------------------------------------------------------------->
    override suspend fun addAadhaar(aadhaar: Aadhaar) = aadhaarDatabase.insertAadhaar(aadhaar)
    override fun getAadhaar(id: Int) = aadhaarDatabase.getAadhaar(id)


    //RcBook DAO
    //<-------------------------------------------------------------------------------------------------------->
    override fun getRcBook(id: Int) = rcBookDatabase.getRcBook(id)
    override fun addRcBook(rcBook: RcBook) = rcBookDatabase.insertRcBook(rcBook)


    //Bike DAO
    //<-------------------------------------------------------------------------------------------------------->
    override fun addBike(bike: Bike) = bikeDatabase.insertBike(bike)
    override fun getBike(id: Int) = bikeDatabase.getBike(id)


    //Otp DAO
    //<-------------------------------------------------------------------------------------------------------->
    override fun storeOtp(otp: Otp): RepositoryResponse = otpDatabase.storeOtp(otp)
    override fun haveOtp(rideId: Int): Boolean = otpDatabase.haveOtp(rideId)
    override fun deleteOtp(rideId: Int): Boolean = otpDatabase.deleteOtp(rideId)
    override fun getOtp(rideId: Int): Otp? = otpDatabase.getOtp(rideId)

}