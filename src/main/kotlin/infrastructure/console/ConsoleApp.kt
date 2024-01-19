package infrastructure.console

import app.domain.models.*
import app.domain.models.customenum.Location
import app.domain.models.customenum.RideStatus
import app.domain.models.response.AuthenticationResponse
import app.usecase.DriverUseCase
import app.usecase.PassengerUseCase
import app.usecase.UseCase
import infrastructure.library.PrintType
import infrastructure.library.input_handler.ApplicationInput
import kotlinx.coroutines.*
import presentationlayer.Constants
import presentationlayer.UserInterface
import utility.UtilityFunction
import java.util.*

class ConsoleApp(private val useCase: UseCase, private val userInterface: UserInterface) {
    fun run() {
        userInterface.displayWelcomeMessage()
        while (true) {
            userInterface.displayMainMenu()
            when (ApplicationInput.input.getInt(Constants.OPTION_FOR_APPLICATION_MAIN_MENU)) {
                1 -> signUp()
                2 -> signIn()
                3 -> UtilityFunction.aboutApplication()
                4 -> break
            }
        }
    }

    private fun signUp() {
        userInterface.displayUserTypeSelectMenu()
        val user: User? = when (ApplicationInput.input.getInt(Constants.OPTION_FOR_USER_TYPE_MENU)) {
            1 -> userInterface.gatherPassengerDetails(useCase::isUserNameExist)
            2 -> userInterface.gatherDriverDetails(useCase::isUserNameExist)
            else -> null
        }
        if (user != null) {
            val response = useCase.addNewUser(user)
            if (response) {
                userInterface.displaySuccess(
                    "${Constants.APPLICATION_SUCCESS_ACCOUNT_CREATE}, ${Constants.APPLICATION_THANK_MESSAGE}"
                )
                userInterface.disPlayPlain(Constants.APPLICATION_PROMPT_CONTINUE_LOGIN)
                if (ApplicationInput.inputWithEmpty.getString() == Constants.ZERO_STRING) signIn()
            } else userInterface.displayError(
                "${Constants.APPLICATION_ERROR_ACCOUNT_CREATE}, ${Constants.DEFAULT_WARNING_TRY_AGAIN}"
            )
        } else userInterface.displayError(Constants.APPLICATION_ERROR_INVALID_USER_TYPE)
    }

    private fun signIn() {
        while (true) {
            val loginDetails: LoginDetails = userInterface.gatherLoginDetails()

            // Authenticate login credential and get logged user
            when (val response: AuthenticationResponse = useCase.authenticateUser(loginDetails)) {
                is AuthenticationResponse.UserFound -> {
                    userInterface.displaySuccess(Constants.APPLICATION_SUCCESS_LOGGED_IN)
                    loggedIn(response.user)
                    break
                }

                is AuthenticationResponse.UserNotFound -> {
                    userInterface.displayError(Constants.APPLICATION_ERROR_USER_NOT_FOUND)
                }

                else -> userInterface.displayWarning(response.getResponseMessage())
            }
        }
    }

    private fun loggedIn(loggedUser: User) {
        when (loggedUser) {
            is Passenger -> loggedWithPassenger(loggedUser, useCase)
            is Driver -> loggedWithDriver(loggedUser, useCase)
        }
        userInterface.displaySuccess(Constants.APPLICATION_SUCCESS_LOGGED_OUT)
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun loggedWithPassenger(passenger: Passenger, passengerUseCase: PassengerUseCase) {
        // printing of welcome message
        userInterface.displayHeader("${Constants.APPLICATION_WELCOME} ${passenger.name} 🙂")

        while (true) {

            // Menu : Book Ride, My Ride, View Profile, Logout
            userInterface.displayPassengerMainMenu()

            when (ApplicationInput.input.getInt(Constants.OPTION_FOR_PASSENGER_MAIN_MENU)) {
                1 -> {
                    // check user currently have incomplete ride
                    if (!passengerUseCase.isPassengerHavaRide(passenger)) {
                        // get pickup and drop location from user
                        val rideData: Pair<Location, Location> = userInterface.gatherRideDataFromPassenger()

                        // booking ride for user
                        if (passengerUseCase.bookRide(passenger, rideData)) {
                            // get short Route
                            val rideRoute: List<Location> = rideData.first.getShortestRoute(rideData.second)

                            userInterface.displayHeader(
                                rideRoute.joinToString(separator = "->", prefix = "Ride Route : ") { it.place }
                            )
                            userInterface.displaySuccess(Constants.APPLICATION_SUCCESS_RIDE_CREATED)

                            // start coroutine to focus on driver Assigned or break with -1
                            val waitingForDriver = GlobalScope.launch {
                                val dots = listOf(".", "..", "...")
                                while (isActive) {
                                    for (i in dots.indices) {
                                        val dotIndex = i % dots.size
                                        userInterface.disPlayPlain(
                                            "\r${Constants.APPLICATION_WAITING_DRIVER_ASSIGN}${dots[dotIndex]}",
                                            PrintType.PRINT
                                        )
                                        delay(500)
                                        userInterface.disPlayPlain("\r ", PrintType.PRINT)
                                    }
                                    val ride: Ride? = passengerUseCase.getMyRide(passenger)

                                    if (ride?.driver != null) {
                                        userInterface.displaySuccess(Constants.APPLICATION_SUCCESS_DRIVER_ASSIGN)
                                        userInterface.displayMyRide(ride)
                                        cancel()
                                    }
                                }
                            }
                            println()
                            if (ApplicationInput.inputWithMinusOne.getInt(Constants.MINUS_ONE..Constants.MINUS_ONE) == Constants.MINUS_ONE) {
                                if (waitingForDriver.isActive) waitingForDriver.cancel()
                            }
                        } else userInterface.displayError(Constants.APPLICATION_ERROR_CANT_BOOK_RIDE)
                    } else userInterface.displayWarning(Constants.APPLICATION_WARNING_HAVE_ANOTHER_RIDE)
                }

                2 -> {
                    outer@ while (true) {
                        val ride: Ride? = passengerUseCase.getMyRide(passenger)
                        if (ride != null) {
                            userInterface.displayMyRide(ride)
                            var menuRange: IntRange = -1..0
                            when(ride.status){
                                RideStatus.CREATED -> {
                                    userInterface.displayPassengerMyRideCreate()
                                    menuRange = -1..1
                                }
                                RideStatus.BOOKED -> {
                                    userInterface.displayPassengerMyRideBooked()
                                    menuRange = -1..2
                                }
                                RideStatus.RIDE_START -> userInterface.displayPassengerMyRideStart()
                                else -> userInterface.displayWarning(Constants.DEFAULT_ERROR_INVALID_INPUT)
                            }

                            while (true) {
                                when (ApplicationInput.inputWithMinusOne.getInt(menuRange)) {
                                    Constants.MINUS_ONE -> break@outer
                                    1 -> {
                                        val otp: Int? = passengerUseCase.getMyOtp(passenger)
                                        if (otp != null) userInterface.displaySuccess("Your OTP: $otp")
                                        else userInterface.displayWarning(Constants.APPLICATION_WARNING_OTP_NOT_AVAILABLE)
                                    }

                                    2 -> {
                                        val cancelResult = passengerUseCase.cancelRide(passenger)
                                        if (cancelResult) {
                                            userInterface.displaySuccess(Constants.APPLICATION_SUCCESS_RIDE_CANCEL)
                                            break@outer
                                        } else userInterface.displayError(Constants.APPLICATION_ERROR_RIDE_CANCEL)
                                    }

                                    0 -> break
                                    else -> userInterface.disPlayPlain(Constants.APPLICATION_FEATURE_UNDER_DEVELOPMENT)
                                }
                            }
                        } else {
                            userInterface.displayWarning(Constants.APPLICATION_WARNING_RIDE_NOT_AVAILABLE)
                            break
                        }
                    }
                }

                3 -> userInterface.disPlayPlain(Constants.APPLICATION_FEATURE_UNDER_DEVELOPMENT)

                4 -> {
                    userInterface.disPlayPlain(Constants.APPLICATION_PROMPT_CONFIRM_TO_LOGOUT)
                    if (ApplicationInput.inputWithMinusOneAndEmpty.getString() == Constants.ZERO_STRING) break
                }
            }
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun loggedWithDriver(driver: Driver, driverUseCase: DriverUseCase) {
        // printing of welcome message
        userInterface.displayHeader("${Constants.APPLICATION_WELCOME} ${driver.name} 🙂")

        while (true) {
            userInterface.displayDriverMainMenu()
            when (ApplicationInput.input.getInt(Constants.OPTION_FOR_DRIVER_MAIN_MENU)) {
                1 -> {
                    outer@ while (true) {
                        if (driverUseCase.isDriverHavaRide(driver)) {
                            userInterface.displayWarning(Constants.APPLICATION_WARNING_ALREADY_HAVE_RIDE)
                            break
                        }
                        userInterface.displayMap(Constants.MAP)

                        while (true) {
                            val driverCurrentLocation = userInterface.gatherLocation() ?: break
                            val result = driverUseCase.getNearByAvailableRide(driverCurrentLocation)
                            if (result.isNotEmpty()) {
                                val count = userInterface.displayAvailableRides(result)
                                userInterface.displayMenu(Constants.APPLICATION_SELECT_FROM_AVAIL_RIDE)
                                when (val input =
                                    ApplicationInput.inputWithMinusOne.getInt(Constants.MINUS_ONE..count)) {
                                    Constants.MINUS_ONE -> break@outer
                                    in 1..count -> {
                                        var ride = result[input - 1]
                                        ride = ride.copy(driver = driver, status = RideStatus.BOOKED)
                                        val acceptRideResult = driverUseCase.acceptRide(ride)
                                        if (acceptRideResult) {
                                            userInterface.displaySuccess(Constants.APPLICATION_SUCCESS_DRIVER_ACCEPT_RIDE)
                                            break@outer
                                        } else userInterface.displayError(Constants.APPLICATION_WARNING_DRIVER_UNABLE_TO_TAKE_RIDE)
                                    }
                                }
                            } else userInterface.displayWarning(Constants.APPLICATION_WARNING_NO_AVAILABLE_RIDE_IN_LOCATION)
                        }
                    }
                }

                2 -> {
                    outer@ while (true) {
                        val ride: Ride? = driverUseCase.getMyRide(driver)
                        if (ride != null) {
                            userInterface.displayMyRide(ride)
                            when (ride.status) {
                                RideStatus.BOOKED -> userInterface.displayDriverMyRideMenuBeforeStart()
                                RideStatus.RIDE_START -> userInterface.displayDriverMyRideMenuAfterStart()
                                else -> userInterface.displayWarning(Constants.DEFAULT_ERROR_INVALID_INPUT)
                            }
                            while (true) {
                                when (ApplicationInput.inputWithMinusOne.getInt(Constants.MINUS_ONE..2)) {
                                    Constants.MINUS_ONE -> break@outer
                                    1 -> {
                                        when (ride.status) {
                                            RideStatus.BOOKED -> {
                                                while (true) {
                                                    userInterface.displayPrompt(Constants.PROMPT_ENTER_OTP)
                                                    val input: Int =
                                                        ApplicationInput.inputWithMinusOne.getInt(Constants.RANGE_OTP_VALIDATION)
                                                    if (input == Constants.MINUS_ONE) break@outer
                                                    if (driverUseCase.validateOtp(
                                                            driver,
                                                            input
                                                        ) && driverUseCase.startRide(driver)
                                                    ) {
                                                        userInterface.displaySuccess(Constants.APPLICATION_SUCCESS_VALID_OTP)
                                                        break@outer
                                                    } else userInterface.displayWarning(Constants.APPLICATION_WARNING_INVALID_OTP)
                                                }
                                            }

                                            RideStatus.RIDE_START -> {
                                                val waitingForPassengerConformation = GlobalScope.launch {
                                                    val dots = listOf(".", "..", "...")
                                                    while (isActive) {
                                                        for (i in dots.indices) {
                                                            val dotIndex = i % dots.size
                                                            userInterface.disPlayPlain(
                                                                "\r${Constants.APPLICATION_WAITING_PASSENGER_RESPONSE}${dots[dotIndex]}",
                                                                PrintType.PRINT
                                                            )
                                                            delay(500)
                                                            userInterface.disPlayPlain("\r ", PrintType.PRINT)
                                                        }
                                                    }
                                                }
                                            }

                                            else -> userInterface.displayWarning(Constants.APPLICATION_WARNING_OTP_VALIDATED)
                                        }
                                    }

                                    2 -> userInterface.disPlayPlain(Constants.APPLICATION_FEATURE_UNDER_DEVELOPMENT)

                                    0 -> break
                                }
                            }
                        } else {
                            userInterface.displayWarning(Constants.APPLICATION_WARNING_RIDE_NOT_AVAILABLE)
                            break
                        }
                    }
                }

                3 -> userInterface.disPlayPlain(Constants.APPLICATION_FEATURE_UNDER_DEVELOPMENT)

                4 -> {
                    userInterface.disPlayPlain(Constants.APPLICATION_PROMPT_CONFIRM_TO_LOGOUT)
                    if (ApplicationInput.inputWithMinusOneAndEmpty.getString() == Constants.ZERO_STRING) break
                }
            }
        }
    }
}