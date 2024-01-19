package app.domain.models

import app.domain.models.customenum.Location
import app.domain.models.customenum.RideStatus

data class Ride(
    val passenger: Passenger,
    var driver: Driver? = null,
    val pickup_location: Location,
    val drop_location: Location,
    val start_time: String?=null,
    val end_time: String? = null,
    var status: RideStatus,
    var total_charge: Double = 0.0
)