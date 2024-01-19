package infrastructure.dao.json

import app.domain.models.Otp
import app.domain.models.response.RepositoryResponse
import com.google.gson.Gson
import infrastructure.dao.OtpDao
import java.io.File

private const val fileName ="src/main/kotlin/infrastructure/dao/json/OtpFile.json"
private val gson = Gson()
class OtpDaoImplement : OtpDao{
    override fun storeOtp(otp: Otp): RepositoryResponse {
        if(haveOtp(otp.rideId)) return RepositoryResponse.HaveAnotherOtp
        val gson = Gson()
        val exist:MutableList<Otp> = getData()?.toMutableList()?: mutableListOf()
        exist.add(otp)
        val json = gson.toJson(exist)
        return if(json != null){
            File(fileName).writeText(json)
            RepositoryResponse.SuccessfullyCreated
        }else
            RepositoryResponse.OperationUnsuccessful("unable to save in file")
    }
    private fun getData(): List<Otp>?{
        return try {
            val json = File(fileName).readText()
            gson.fromJson(json, Array<Otp>::class.java).toList()
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
    override fun haveOtp(rideId:Int):Boolean{
        val data: List<Otp>? = getData()
        data?.forEach {
            if(it.rideId == rideId) return true
        }
        return false
    }

    override fun getOtp(rideId: Int): Otp?{
        if(haveOtp(rideId)){
            val data: List<Otp>? = getData()
            data?.forEach {
                if(it.rideId == rideId) return it
            }
        }
        return null
    }
    override fun deleteOtp(rideId: Int):Boolean{
        if(haveOtp(rideId)){
            val data: MutableList<Otp>? = getData()?.toMutableList()
            if (data != null) {
                for(i in 0..data.size){
                    if(rideId == data[i].rideId) {
                        data.removeAt(i)
                        break
                    }
                }
            }
            val json = gson.toJson(data)
            File(fileName).writeText(json)
            return true
        }
        return false
    }
}