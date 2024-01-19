package infrastructure.dao

import app.domain.models.Ride
import app.domain.models.User
import app.domain.models.customenum.Location
import app.domain.models.customenum.RideStatus
import app.domain.models.response.RepositoryResponse

interface RideDao {
    fun insertRide(ride: Ride, passengerId: Int): RepositoryResponse
    fun getRide(id: Int, user : User): Ride?
    fun getRideWithPickUpLocation(nearLocation: MutableMap<Location, Int>): List<Ride>
    fun checkUserHaveRide(loggedUserid: Int): RepositoryResponse
    fun updateRide(ride: Ride): RepositoryResponse
    fun getRideId(passengerId: Int): Int
    fun updateDriverInRide(rideId: Int, driverId: Int): RepositoryResponse
    fun updateRideStatus(rideId: Int, status: RideStatus): RepositoryResponse
    fun cancelRide(rideId: Int): RepositoryResponse
}