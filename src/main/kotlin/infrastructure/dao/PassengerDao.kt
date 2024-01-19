package infrastructure.dao

import app.domain.models.Passenger
import app.domain.models.customenum.BikeType

interface PassengerDao {
    fun insertPassenger(userId: Int, aadhaarId: Int, preferredVehicleType: BikeType)
    fun getPassenger(id: Int): Passenger
    fun getPassengerId(userId: Int): Int
    fun injectUser(userDatabase: UserDao)
}