package presentationlayer

import app.domain.models.LoginDetails
import app.domain.models.Ride
import app.domain.models.User
import app.domain.models.customenum.Location
import infrastructure.library.PrintType

interface UserInterface {
    fun displayMainMenu()
    fun displayUserTypeSelectMenu()
    fun displayPassengerMainMenu()
    fun displayDriverMainMenu()
    fun displayRideRotes()
    fun displayWelcomeMessage()
    fun displaySuccess(message: String, printType: PrintType = PrintType.PRINTLN)
    fun displayWarning(message: String, printType: PrintType = PrintType.PRINTLN)
    fun displayError(message: String, printType: PrintType = PrintType.PRINTLN)
    fun displayMenu(message: String, printType: PrintType = PrintType.PRINTLN)
    fun displayMap(message: String, printType: PrintType = PrintType.PRINTLN)
    fun disPlayPlain(message: String, printType: PrintType = PrintType.PRINTLN)
    fun displayHeader(message: String, printType: PrintType = PrintType.PRINTLN)
    fun displayPrompt(message: String, printType: PrintType = PrintType.PRINT)
    fun displayAvailableRides(rides: List<Ride>): Int
    fun displayMyRide(ride: Ride)
    fun displayDriverMyRideMenuBeforeStart()
    fun displayDriverMyRideMenuAfterStart()
    fun gatherPassengerDetails(isUserNameExist: (String) -> Boolean): User
    fun gatherDriverDetails(isUserNameExist: (String) -> Boolean): User
    fun gatherLoginDetails(): LoginDetails
    fun gatherLocation(): Location?
    fun gatherRideDataFromPassenger(): Pair<Location, Location>
    fun displayPassengerMyRideBooked()
    fun displayPassengerMyRideCreate()
    fun displayPassengerMyRideStart()
}