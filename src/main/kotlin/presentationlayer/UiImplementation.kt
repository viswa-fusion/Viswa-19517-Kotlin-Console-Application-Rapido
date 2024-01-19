package presentationlayer

import app.domain.models.Ride
import app.domain.models.User
import app.domain.models.customenum.Location
import infrastructure.library.OutputHandler
import infrastructure.library.PrintType
import infrastructure.library.input_handler.ApplicationInput
import presentationlayer.Constants.APPLICATION_DRIVER_MAIN_MENU
import presentationlayer.Constants.APPLICATION_DRIVER_MY_RIDE_MENU_AFTER_START
import presentationlayer.Constants.APPLICATION_DRIVER_MY_RIDE_MENU_BEFORE_START
import presentationlayer.Constants.APPLICATION_MAIN_MENU
import presentationlayer.Constants.APPLICATION_PASSENGER_MAIN_MENU
import presentationlayer.Constants.APPLICATION_USER_TYPE_SELECT_MENU
import presentationlayer.Constants.AVAILABLE_PASSENGER_LIST_TEMP
import presentationlayer.Constants.MAP
import presentationlayer.Constants.RIDE_DISPLAY_TEMPLATE

class UiImplementation : UserInterface {

    // objects for related pages
    private val signUpPage = SignUpPage()
    private val signInPage = SignInPage()

    override fun gatherRideDataFromPassenger(): Pair<Location, Location> {
        displayMap(MAP)
        var pickupLocation: Location
        var dropLocation: Location
        do {
            pickupLocation = ApplicationInput.gatherPickLocation()
            dropLocation = ApplicationInput.gatherDropLocation()
        } while ((pickupLocation == dropLocation).also {
                if (it) OutputHandler.displayWarning(Constants.DEFAULT_WARNING_BOTH_LOCATION_ARE_SAME)
            })
        return Pair(pickupLocation, dropLocation)
    }

    override fun displayRideRotes() = displayMap(MAP)
    override fun displayWelcomeMessage() = disPlayPlain(Constants.WELCOME_MESSAGE)
    override fun displayMainMenu() = displayMenu(APPLICATION_MAIN_MENU)
    override fun displayUserTypeSelectMenu() = displayMenu(APPLICATION_USER_TYPE_SELECT_MENU)
    override fun displayPassengerMainMenu() = displayMenu(APPLICATION_PASSENGER_MAIN_MENU)
    override fun displayDriverMainMenu() = displayMenu(APPLICATION_DRIVER_MAIN_MENU)
    override fun displayDriverMyRideMenuBeforeStart() = displayMenu(APPLICATION_DRIVER_MY_RIDE_MENU_BEFORE_START)
    override fun displayDriverMyRideMenuAfterStart() = displayMenu(APPLICATION_DRIVER_MY_RIDE_MENU_AFTER_START)
    override fun displayPassengerMyRideCreate() = displayMenu(Constants.APPLICATION_PASSENGER_MY_RIDE_CREATED)
    override fun displayPassengerMyRideBooked() = displayMenu(Constants.APPLICATION_PASSENGER_MY_RIDE_BOOKED)
    override fun displayPassengerMyRideStart() = displayMenu(Constants.APPLICATION_PASSENGER_MY_RIDE_START)

    override fun gatherPassengerDetails(isUserNameExist: (String) -> Boolean): User =
        signUpPage.displayPassengerSignUp(isUserNameExist)

    override fun gatherDriverDetails(isUserNameExist: (String) -> Boolean): User =
        signUpPage.displayDriverSignUp(isUserNameExist)

    override fun gatherLoginDetails() = signInPage.displaySignInPage()
    override fun gatherLocation() = ApplicationInput.gatherCurrentLocation()
    override fun displaySuccess(message: String, printType: PrintType) =
        OutputHandler.displaySuccess(message, printType)

    override fun displayWarning(message: String, printType: PrintType) =
        OutputHandler.displayWarning(message, printType)

    override fun displayError(message: String, printType: PrintType) = OutputHandler.displayError(message, printType)
    override fun displayMenu(message: String, printType: PrintType) = OutputHandler.displayMenu(message, printType)
    override fun displayMap(message: String, printType: PrintType) = OutputHandler.displayMap(message, printType)
    override fun disPlayPlain(message: String, printType: PrintType) = OutputHandler.disPlayPlain(message, printType)
    override fun displayHeader(message: String, printType: PrintType) = OutputHandler.displayHeader(message, printType)
    override fun displayPrompt(message: String, printType: PrintType) = OutputHandler.displayPrompt(message, printType)


    override fun displayAvailableRides(rides: List<Ride>): Int {
        var id = 1
        displayMenu(Constants.AVAILABLE_PASSENGER_HEAD_TEMP)
        rides.forEach {
            displayMenu(
                AVAILABLE_PASSENGER_LIST_TEMP.format(
                    id++,
                    it.passenger.username,
                    it.pickup_location,
                    it.drop_location,
                    it.total_charge
                )
            )
        }
        return id - 1
    }

    override fun displayMyRide(ride: Ride) {
        displayMenu(
            RIDE_DISPLAY_TEMPLATE.format(
                ride.passenger.username,
                ride.driver?.username ?: "not assigned",
                ride.pickup_location,
                ride.drop_location,
                ride.status,
                ride.total_charge,
                ride.start_time?:"-",
                ride.end_time?:"-"
            )
        )
    }
}