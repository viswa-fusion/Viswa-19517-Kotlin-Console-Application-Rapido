package utility

import app.domain.models.customenum.Location
import infrastructure.library.OutputHandler
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

class UtilityFunction private constructor() {
    companion object {
        private const val commissionRate = 5
        fun calculateDriverCharge(amount: Double): Double {
            return amount - (amount * commissionRate / 100)
        }

        fun getCurrentTime(): Long {
            return System.currentTimeMillis()
        }

        fun convertToStdTime(millis: Long?): String? {
            return if(millis != null){
                val localDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(millis), ZoneOffset.systemDefault())
                val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm:ss a")
                localDateTime.format(formatter)
            }else null
        }

        fun initializeRapidoMap() {
            Location.CHE.addNearByDestination(Location.GU, 15)
            Location.GU.addNearByDestination(Location.VA, 7)
            Location.VA.addNearByDestination(Location.PE, 5)
            Location.VA.addNearByDestination(Location.MU, 8)
            Location.PE.addNearByDestination(Location.TM, 6)
            Location.MU.addNearByDestination(Location.TM, 4)
            Location.TM.addNearByDestination(Location.CH, 3)
            Location.CH.addNearByDestination(Location.PA, 4)
            Location.CH.addNearByDestination(Location.TH, 14)
            Location.TH.addNearByDestination(Location.TMY, 9)
            Location.TMY.addNearByDestination(Location.AD, 7)
            Location.PA.addNearByDestination(Location.CIA, 5)
            Location.CIA.addNearByDestination(Location.AL, 8)
            Location.AL.addNearByDestination(Location.GI, 6)
            Location.AD.addNearByDestination(Location.GI, 12)
            Location.AL.addNearByDestination(Location.EK, 6)
            Location.EK.addNearByDestination(Location.KO, 5)
            Location.KO.addNearByDestination(Location.AN, 10)
            Location.AN.addNearByDestination(Location.CPT, 3)
            Location.CPT.addNearByDestination(Location.EG, 5)
            Location.EG.addNearByDestination(Location.VP, 7)
            Location.VP.addNearByDestination(Location.KO, 9)
            Location.VP.addNearByDestination(Location.TN, 4)
            Location.TN.addNearByDestination(Location.SA, 6)
            Location.SA.addNearByDestination(Location.GI, 8)
        }

        fun aboutApplication() {
            OutputHandler.displayHeader(
                """
                Welcome to Rapido Bike Taxi Application!
                ===============================================
                About Rapido Bike Taxi:
                Rapido is a convenient and efficient bike taxi service that allows users to easily navigate through busy city streets.
                Our application provides a simplified interface to access Rapido's features.
                
                Key Features:
                1. Book a Ride: Quickly book a bike taxi for your immediate or scheduled travel needs.
                2. Real-time Tracking: Stay informed with live tracking of your ride for enhanced safety and convenience.
                3. Fare Estimation: Get an estimate of the ride fare before confirming your booking.
                5. Seamless Payments: Enjoy hassle-free payments with various payment options available.
                
                Get Started:
                To begin, simply enter the corresponding option number when prompted.
                
                Thank you for choosing Rapido Bike Taxi! Safe travels!""".trimIndent()
            )
        }
    }
}
