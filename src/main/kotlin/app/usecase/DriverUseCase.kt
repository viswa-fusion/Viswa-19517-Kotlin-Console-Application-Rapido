package app.usecase

import app.domain.models.Driver
import app.domain.models.Ride
import app.domain.models.User
import app.domain.models.customenum.Location
import app.domain.models.customenum.RideStatus

interface DriverUseCase {
    fun getNearByAvailableRide(driverCurrentLocation: Location): List<Ride>
    fun getMyRide(driver: Driver): Ride?
    fun acceptRide(ride: Ride): Boolean
    fun isDriverHavaRide(driver: Driver): Boolean
    fun validateOtp(driver: Driver, otp: Int): Boolean
    fun startRide(driver: Driver): Boolean
    fun updateRideStatus(user: User, status: RideStatus): Boolean
}