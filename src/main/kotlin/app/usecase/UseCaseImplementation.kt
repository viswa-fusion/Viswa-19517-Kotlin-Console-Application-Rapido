package app.usecase

import app.domain.models.*
import app.domain.models.customenum.Location
import app.domain.models.customenum.RideStatus
import app.domain.models.response.AuthenticationResponse
import app.domain.models.response.RepositoryResponse
import app.domain.repository.Repository
import kotlinx.coroutines.runBlocking
import utility.UtilityFunction
import utility.UtilityFunction.Companion.calculateDriverCharge
import kotlin.random.Random

class UseCaseImplementation(private val repository: Repository) : UseCase {

    // domain function's
    //------------------------------------------------------------------------------------------------------------------
    override fun addNewUser(user: User): Boolean {
        val response = when (user) {
            is Passenger -> runBlocking {
                repository.addNewPassenger(user)
            }

            is Driver -> runBlocking {
                repository.addNewDriver(user)
            }

            else -> RepositoryResponse.SignupFailed
        }
        return response is RepositoryResponse.SuccessfullyCreated
    }

    override fun authenticateUser(loginDetails: LoginDetails): AuthenticationResponse {
        return when (val response = repository.isValidCredential(loginDetails)) {
            is AuthenticationResponse.ValidCredential -> repository.findUser(loginDetails.userName)
            else -> response
        }
    }

    override fun isUserNameExist(username: String) = repository.isUserNameExist(username)
    override fun generateOtp(ride: Ride): Boolean{
            return if (ride.status == RideStatus.BOOKED) {
                val passengerId: Int = repository.getUserTypeId(ride.passenger)
                val rideId: Int = repository.getRideId(passengerId)
                if (repository.haveOtp(rideId)) return false
                val randNum: Int = Random.nextInt(100000, 999999)
                val otp = Otp(rideId, randNum)
                val response: RepositoryResponse = repository.storeOtp(otp)
                if (response.getResponse() != 200) return false
                true
            }else false
    }

    // passenger function's
    //------------------------------------------------------------------------------------------------------------------
    override fun bookRide(passenger: Passenger, rideDetails: Pair<Location, Location>): Boolean {
        //calculating total charge for the ride
        val totalCalculatedCharge = Location.calculateTotalCharge(rideDetails.first, rideDetails.second)

        // prepare ride object for book ride
        val ride = Ride(
            passenger = passenger,
            pickup_location = rideDetails.first,
            drop_location = rideDetails.second,
            status = RideStatus.CREATED,
            total_charge = totalCalculatedCharge
        )

        //provide data to database and return the response
        return when (val response = repository.addNewRide(passenger, ride)) {
            is RepositoryResponse.SuccessfullyCreated -> true
            else -> {
                // testing -> print response to console
                println("${response.getResponse()} -> ${response.getResponseMessage()}")
                false
            }
        }
    }

    override fun getMyRide(passenger: Passenger): Ride? {
        val ride: Ride? = repository.getRideByPassenger(passenger)
        return ride?.copy(
            start_time = UtilityFunction.convertToStdTime(ride.start_time?.toLongOrNull()),
            end_time = UtilityFunction.convertToStdTime(ride.end_time?.toLongOrNull())
        )
    }
    override fun isPassengerHavaRide(passenger: Passenger) = repository.isPassengerHavaRide(passenger)
    override fun cancelRide(passenger: Passenger): Boolean {
        if(isPassengerHavaRide(passenger)) {
            val ride: Ride? = getMyRide(passenger)
            if(ride?.status == RideStatus.CREATED || ride?.status ==  RideStatus.BOOKED){
                repository.cancelRide(passenger)
                return true
            }
        }
        return false
    }
    override fun getMyOtp(passenger: Passenger): Int? {
        val result: Boolean = isPassengerHavaRide(passenger)
        if (result) {
            val ride: Ride? = getMyRide(passenger)
            if (ride != null && ride.status == RideStatus.BOOKED) {
                val passengerId: Int = repository.getUserTypeId(passenger)
                val rideId: Int = repository.getRideId(passengerId)
                if (repository.haveOtp(rideId)) return repository.getOtp(rideId)?.otp
            }
        }
        return null
    }


    // Driver function's
    //------------------------------------------------------------------------------------------------------------------
    override fun getMyRide(driver: Driver): Ride? {
        val ride: Ride? = repository.getRideByDriver(driver)
        return ride?.copy(
            start_time = UtilityFunction.convertToStdTime(ride.start_time?.toLongOrNull()),
            end_time = UtilityFunction.convertToStdTime(ride.end_time?.toLongOrNull()),
            total_charge = calculateDriverCharge(ride.total_charge)
        )
    }

    override fun isDriverHavaRide(driver: Driver) = repository.isDriverHaveRide(driver)
    override fun validateOtp(driver: Driver, otp: Int): Boolean {
        val result: Boolean = isDriverHavaRide(driver)
        if (result) {
            val ride: Ride? = getMyRide(driver)
            if (ride != null && ride.status == RideStatus.BOOKED) {
                val passengerId = repository.getUserTypeId(ride.passenger)
                val rideId = repository.getRideId(passengerId)
                if (repository.haveOtp(rideId)) {
                    val fetchedOtp: Otp? = repository.getOtp(rideId)
                    if (fetchedOtp != null && fetchedOtp.otp == otp) {
                        repository.deleteOtp(rideId)
                        return true
                    }
                }
            }
        }
        return false
    }

    override fun getNearByAvailableRide(driverCurrentLocation: Location): List<Ride> {
        val rides = repository.getNearByAvailableRide(driverCurrentLocation)
        rides.forEach {
            it.total_charge = calculateDriverCharge(it.total_charge)
        }
        return rides
    }

    override fun acceptRide(ride: Ride): Boolean {
        return when (val response = repository.acceptRide(ride)) {
            is RepositoryResponse.SuccessfullyUpdated -> {
                generateOtp(ride)
                true
            }
            else -> false
        }

    }

    override fun startRide(driver: Driver): Boolean {
        val ride: Ride? = repository.getRideByDriver(driver)
        val currentTime = UtilityFunction.getCurrentTime()
        val updatedRide: Ride? = ride?.copy(start_time = currentTime.toString(), status = RideStatus.RIDE_START)
        if (updatedRide != null && repository.updateRide(updatedRide) is RepositoryResponse.SuccessfullyUpdated) return true
        return false
    }

    override fun updateRideStatus(user: User, status: RideStatus): Boolean {
        when (user) {
            is Passenger -> {
                val result: Boolean = isPassengerHavaRide(user)
                if (result) {
                    val passengerId = repository.getUserTypeId(user)
                    val rideId = repository.getRideId(passengerId)
                    return when (repository.updateRideStatus(rideId, status)) {
                        RepositoryResponse.SuccessfullyUpdated -> true
                        else -> false
                    }
                }
            }

            is Driver -> {
                val result: Boolean = isDriverHavaRide(user)
                if (result) {
                    val ride = repository.getRideByDriver(user)
                    if (ride != null) {
                        val passengerId: Int = repository.getUserTypeId(ride.passenger)
                        val rideId = repository.getRideId(passengerId)
                        return when (repository.updateRideStatus(rideId, status)) {
                            RepositoryResponse.SuccessfullyUpdated -> true
                            else -> false
                        }
                    }
                }
            }
        }
        return false
    }

}