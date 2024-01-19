package app.domain.models.customenum

enum class Location(val place: String) {
//    GU, VA, MU, PE, TM, CH, PA, PM, CIA;
    GU("Guduvancheri"),
    VA("Vandalur"),
    TM("Tambaram"),
    CH("Chrompet"),
    PA("Pallavaram"),
    PE("Perungalathur"),
    MU("Mudichor"),
    TH("Thoraipakkam"),
    AD("Adyar"),
    GI("Guindy"),
    AL("Alandur"),
    SA("Saidapet"),
    EK("Ekktuthangal"),
    VP("Vadapalani"),
    TN("T.Nagar"),
    AN("AnnaNagar"),
    KO("Koyambedu"),
    EG("Egmore"),
    CPT("Chetpet"),
    CHE("Chengalpattu"),
    CIA("International Airport"),
    TMY("Thiruvanmiyur");

    var map = mutableMapOf<Location, Int>()
        private set

    fun addNearByDestination(nearByLocation: Location, distanceBetween: Int) {
        map[nearByLocation] = distanceBetween
        nearByLocation.map[this] = distanceBetween
    }

    fun getShortestRoute(destination: Location): List<Location>{
        val result = this.getShortestDistance(destination)
        return result.first
    }
    fun getDistanceBetween(destination: Location): Int{
        val result = this.getShortestDistance(destination)
        return result.second
    }
    private fun getShortestDistance(destination: Location): Pair<List<Location>, Int> {
        val currentLocation = mutableListOf(this)
        val checkedLocation = mutableListOf<Location>()
        val measuredLocation = mutableMapOf<Location, Int>()
        val previousLocation = mutableMapOf<Location, Location>()

        measuredLocation[this] = 0

        while (currentLocation.isNotEmpty()) {
            val current = currentLocation.removeAt(0)

            if (current == destination) {
                val route = mutableListOf<Location>()
                var backtrace = destination
                while (backtrace != this) {
                    route.add(backtrace)
                    backtrace = previousLocation[backtrace]!!
                }
                route.add(this)
                return Pair(route.reversed(), measuredLocation[current] ?: 0)
            }
            if (checkedLocation.contains(current)) continue

            checkedLocation.add(current)

            for ((nearLocation, distance) in current.map) {
                val newMeasuredDistance = measuredLocation[current]!! + distance
                if (newMeasuredDistance < measuredLocation.getOrDefault(nearLocation, Int.MAX_VALUE)) {
                    measuredLocation[nearLocation] = newMeasuredDistance
                    previousLocation[nearLocation] = current
                    currentLocation.add(nearLocation)
                }
            }
        }
        return Pair(emptyList(), 0)
    }
    fun castLocation(): String {
        return this.toString()
    }

    companion object {
        fun calculateTotalCharge(fromLocation : Location, toLocation: Location): Double {
            val costPerKilometer = 7.0
            val kilometer = fromLocation.getDistanceBetween(toLocation)
            return kilometer.times(costPerKilometer)
        }
        fun castLocation(location: String): Location? {
            return try {
                Location.valueOf(location.uppercase())
            }catch(e: IllegalArgumentException){
                null
            }
        }
    }
}

