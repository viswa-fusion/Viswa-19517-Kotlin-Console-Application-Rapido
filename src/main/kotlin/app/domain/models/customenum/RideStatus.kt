package app.domain.models.customenum

enum class RideStatus {
    CREATED,
    BOOKED,
    RIDE_START,
    RIDE_END,
    PAYMENT_PENDING,
    COMPLETED,
    CANCEL;
    fun encode(): String{
        return this.toString()
    }
    companion object{
        fun castToRideStatus(status: String): RideStatus? {
            return try{
                valueOf(status)
            }catch (e: IllegalArgumentException) {
                null
            }
        }
    }
}
