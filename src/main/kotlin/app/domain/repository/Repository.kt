package app.domain.repository

import app.domain.models.*
import app.domain.models.customenum.Location
import app.domain.models.customenum.RideStatus
import app.domain.models.response.AuthenticationResponse
import app.domain.models.response.RepositoryResponse

interface Repository {
    suspend fun addUser(user: User): Int
    fun findUser(userName: String): AuthenticationResponse
    fun getUserId(userId: Int): User
    fun getUser(userId: Int): User?
    suspend fun addNewPassenger(passenger: Passenger): RepositoryResponse
    fun addLicense(license: License): Int
    fun getLicense(id: Int): License
    fun addBike(bike: Bike): Int
    suspend fun addNewDriver(driver: Driver): RepositoryResponse

    //-----------------------------------------------------------------------------------------------------
    fun addNewRide(user : User, ride: Ride): RepositoryResponse
    fun getRideByPassenger(passenger: Passenger):Ride?
    fun getRideByDriver(driver: Driver):Ride?
    fun isPassengerHavaRide(passenger: Passenger): Boolean
    fun isDriverHaveRide(driver: Driver): Boolean
    fun cancelRide(passenger: Passenger): Boolean
    fun getRideId(passengerId: Int): Int
    fun updateRide(ride: Ride): RepositoryResponse

    //-----------------------------------------------------------------------------------------------------
    suspend fun addAadhaar(aadhaar: Aadhaar): Int
    fun getUserTypeId(user: User): Int
    fun isUserNameExist(userName: String): Boolean
    fun isValidCredential(loginDetails: LoginDetails): AuthenticationResponse
    fun getNearByAvailableRide(currentLocation: Location): List<Ride>
    fun getAadhaar(id: Int): Aadhaar
    fun getDriver(id: Int): Driver?
    fun getBike(id: Int): Bike
    fun getRcBook(id: Int): RcBook
    fun getPassenger(id: Int): Passenger
    fun addRcBook(rcBook: RcBook): Int
    fun acceptRide(ride: Ride): RepositoryResponse

    //-----------------------------------------------------------------------------------------------------
    fun storeOtp(otp: Otp): RepositoryResponse
    fun haveOtp(rideId: Int): Boolean
    fun deleteOtp(rideId: Int): Boolean
    fun getOtp(rideId: Int): Otp?
    fun updateRideStatus(rideId: Int, rideStatus: RideStatus): RepositoryResponse
}