package infrastructure.dao

import app.domain.models.Otp
import app.domain.models.response.RepositoryResponse

interface OtpDao {
    fun storeOtp(otp: Otp): RepositoryResponse
    fun getOtp(rideId: Int): Otp?
    fun deleteOtp(rideId: Int):Boolean
    fun haveOtp(rideId:Int):Boolean
}