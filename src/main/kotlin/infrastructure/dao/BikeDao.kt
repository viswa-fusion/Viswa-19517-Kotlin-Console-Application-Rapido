package infrastructure.dao

import app.domain.models.Bike

interface BikeDao {
    fun insertBike(bike: Bike): Int
    fun getBike(id: Int): Bike
}