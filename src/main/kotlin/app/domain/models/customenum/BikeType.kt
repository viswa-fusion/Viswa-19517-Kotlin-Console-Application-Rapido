package app.domain.models.customenum


enum class BikeType {
    SCOOTER,CLASSIC,SPORTS;

    fun castBikeType():String{
        return this.toString()
    }
    companion object{
        fun castBikeType(type: String): BikeType?{
            return try{
                BikeType.valueOf(type)
            }catch (e: IllegalArgumentException){
                null
            }
        }
    }
}
