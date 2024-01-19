package app.usecase

import app.domain.models.Passenger
import app.domain.models.Ride
import app.domain.models.User
import app.domain.models.customenum.Location
import app.domain.models.customenum.RideStatus

interface PassengerUseCase {
    fun bookRide(passenger: Passenger, rideDetails : Pair<Location, Location>): Boolean
    fun getMyRide(passenger: Passenger): Ride?
    fun isPassengerHavaRide(passenger: Passenger): Boolean
    fun cancelRide(passenger: Passenger): Boolean
    fun getMyOtp(passenger: Passenger):Int?
    fun updateRideStatus(user: User, status: RideStatus): Boolean
}