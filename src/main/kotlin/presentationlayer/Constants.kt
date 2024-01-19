package presentationlayer

import infrastructure.library.TextColor

object Constants {
    private const val COLON_SIGN = ":"
    private const val APPLICATION_DATE_FORMAT = "format(DD/MM/YYYY)"

    const val PROMPT_ENTER_INPUT = "enter Input here"
    const val PROMPT_ENTER_CHOICE = "enter your choice"
    const val PROMPT_ENTER_STRING_INPUT = "enter string input"
    const val PROMPT_ENTER_STRING_INCLUDE_NULL = "enter string input null Allowed"
    const val PROMPT_ENTER_CURRENT_LOCATION = "enter your current location"
    const val PROMPT_FULL_NAME = "enter full name"
    const val PROMPT_ENTER_USERNAME = "enter username"
    const val PROMPT_PASSWORD = "enter password"
    const val PROMPT_PHONE_NUMBER = "enter your phone number"
    const val PROMPT_AGE = "enter your Age"
    const val PROMPT_PASSENGER_AGE = "$PROMPT_AGE (15 - 70 only eligible)"
    const val PROMPT_DRIVER_AGE = "$PROMPT_AGE (21 - 40 only eligible)"
    const val PROMPT_BIKE_TYPE = "type(SCOOTER/CLASSIC/SPORTS)"
    const val PROMPT_LICENSE_NO = "enter License number"
    const val PROMPT_LICENSED_DATE = "enter licensed date $APPLICATION_DATE_FORMAT"
    const val PROMPT_LICENSE_RENEWAL_DATE = "enter license renewal date $APPLICATION_DATE_FORMAT"
    const val PROMPT_AADHAAR_NUMBER = "enter your aadhaar number format('XXXX-XXXX-XXXX')"
    const val PROMPT_AADHAAR_NAME = "enter name as in your Aadhaar"
    const val PROMPT_AADHAAR_RENEWAL_DATE = "enter Aadhaar renewal date $APPLICATION_DATE_FORMAT"
    const val PROMPT_LICENSE_TYPE = "enter license type(gear/non-gear vehicle)"
    const val PROMPT_VEHICLE_USED_YEAR = "enter your vehicle used year"
    const val PROMPT_RC_OWNER_NAME = "enter RC-Book owner name"
    const val PROMPT_MODEL_NAME = "enter bike model name"
    const val PROMPT_BIKE_COLOR = "choose your Vehicle color"
    const val PROMPT_RC_BOOK_REG_DATE = "enter registration date of RC-Book $APPLICATION_DATE_FORMAT"
    const val PROMPT_RC_RENEWAL_DATE = "enter RC-Book renewal date $APPLICATION_DATE_FORMAT"
    const val PROMPT_VEHICLE_REG_NO = "choose your Vehicle registration number"
    const val PROMPT_LOCATION_PICK_POINT = "enter pick location (eg:GU)"
    const val PROMPT_LOCATION_DROP_POINT = "enter drop location (eg:PE)"
    const val PROMPT_ENTER_OTP = "enter OTP here :"

    const val DEFAULT_WARNING_GO_BACK = "Back option not allowed.🚫"
    const val DEFAULT_WARNING_REGEX = "not match with the regex pattern."
    const val DEFAULT_WARNING_NULL_OR_EMPTY = "Input cannot be null or empty."
    const val DEFAULT_WARNING_LIST_VALUE_NOT_MATCH = "Input must be one of the allowed values."
    const val DEFAULT_WARNING_TRY_AGAIN = "Please try again."
    const val DEFAULT_WARNING_NOT_IN_RANGE = "input not in the specified size range"
    const val DEFAULT_WARNING_USERNAME_ALREADY_EXIST = "User name already exist"
    const val DEFAULT_WARNING_BOTH_LOCATION_ARE_SAME = "both pickup and drop location are same..⚠️"
    const val DEFAULT_ERROR_INVALID_INPUT = "invalid input try again..!"

    val RANGE_USERNAME = 3..12
    val RANGE_PASSWORD = 8..20
    val RANGE_PASSENGER_AGE = 15..70
    val RANGE_DRIVER_AGE = 21..40
    val RANGE_VEHICLE_USED_YEAR = 1..12
    val RANGE_OTP_VALIDATION = 100000..999999

    val OPTION_FOR_DRIVER_MAIN_MENU = 1..4
    val OPTION_FOR_PASSENGER_MAIN_MENU = 1..4
    val OPTION_FOR_APPLICATION_MAIN_MENU = 1..4
    val OPTION_FOR_USER_TYPE_MENU = 1..2

    const val ZERO = 0
    const val ZERO_STRING = "0"
    const val MINUS_ONE = -1
    const val MINUS_ONE_STRING = "-1"

    const val APPLICATION_WELCOME = "Welcome"
    const val APPLICATION_THANK_MESSAGE = "thanks for choosing rapido 😁"

    const val APPLICATION_SUCCESS_ACCOUNT_CREATE = "Account Created Successfully"
    const val APPLICATION_SUCCESS_LOGGED_OUT = "successfully Logged out"
    const val APPLICATION_SUCCESS_LOGGED_IN = "successfully Logged In"
    const val APPLICATION_SUCCESS_RIDE_CREATED = "your ride successfully created wait for Driver, -1 to go back"
    const val APPLICATION_SUCCESS_DRIVER_ASSIGN = "Driver assigned. Enjoy your ride!"
    const val APPLICATION_SUCCESS_RIDE_CANCEL = "ride cancel successfully"
    const val APPLICATION_SUCCESS_DRIVER_ACCEPT_RIDE = "Successfully Taken The Ride 🥳"
    const val APPLICATION_SUCCESS_VALID_OTP = "OTP Successful"

    const val APPLICATION_WARNING_RIDE_NOT_AVAILABLE = "no Ride Available..❕"
    const val APPLICATION_WARNING_HAVE_ANOTHER_RIDE = "you have another ride currently. please check MyRide Menu"
    const val APPLICATION_WARNING_DRIVER_UNABLE_TO_TAKE_RIDE = "unable to Taken the ride"
    const val APPLICATION_WARNING_NO_AVAILABLE_RIDE_IN_LOCATION = "currently no ride available in the location"
    const val APPLICATION_WARNING_INVALID_OTP = "Invalid OTP"
    const val APPLICATION_WARNING_OTP_NOT_AVAILABLE = "currently don't have otp"
    const val APPLICATION_WARNING_OTP_VALIDATED = "already OTP validated"
    const val APPLICATION_WARNING_ALREADY_HAVE_RIDE =
        "your already accept another ride so kindly complete it, check MyRide menu to know more"

    const val APPLICATION_ERROR_ACCOUNT_CREATE = "Can't create account now."
    const val APPLICATION_ERROR_USER_NOT_FOUND = "User Not Found"
    const val APPLICATION_ERROR_CANT_BOOK_RIDE = "unfortunately can't book ride now"
    const val APPLICATION_ERROR_RIDE_CANCEL = "ride cancel Unsuccessfully"
    const val APPLICATION_ERROR_INVALID_USER_TYPE = "Invalid user type Selected..‼️"

    const val APPLICATION_FEATURE_UNDER_DEVELOPMENT = "feature under development"
    const val APPLICATION_SELECT_FROM_AVAIL_RIDE = "enter id number to Accept Ride or( 0 to refresh) or (-1 to go back)"
    const val APPLICATION_PROMPT_CONTINUE_LOGIN = "enter 0 to login or press any key to exit"
    const val APPLICATION_PROMPT_CONFIRM_TO_LOGOUT = "enter 0 to confirm logout or press any key to cancel"
    const val APPLICATION_WAITING_DRIVER_ASSIGN = "Loading"
    const val APPLICATION_WAITING_PASSENGER_RESPONSE = "Waiting For Passenger Confirmation"


    val APPLICATION_MAIN_MENU = """1 -> Sign_Up
                |2 -> Sign_In
                |3 -> About
                |4 -> Exit""".trimMargin("|")
    val APPLICATION_USER_TYPE_SELECT_MENU = """1 -> Passenger
                |2 -> Driver""".trimMargin("|")
    val APPLICATION_PASSENGER_MAIN_MENU = """1 -> Book Ride
                |2 -> My Ride
                |3 -> View Profile
                |4 -> Logout
            """.trimMargin("|")
    val APPLICATION_DRIVER_MAIN_MENU = """1 -> Check Available Ride
                |2 -> My Ride
                |3 -> View Profile
                |4 -> Logout
            """.trimMargin("|")
    private const val CANCEL_RIDE_MENU =" 2  -> Cancel Ride"
    private val APPLICATION_RIDE_MENU = """ 0  -> Refresh
                |-1  -> go Back
            """.trimMargin("|")

    val APPLICATION_PASSENGER_MY_RIDE_CREATED = """$CANCEL_RIDE_MENU
                |$APPLICATION_RIDE_MENU
            """.trimMargin("|")
    val APPLICATION_PASSENGER_MY_RIDE_BOOKED = """ 1  -> Show Otp
                |$CANCEL_RIDE_MENU
                |$APPLICATION_RIDE_MENU
            """.trimMargin("|")
    val APPLICATION_PASSENGER_MY_RIDE_START = APPLICATION_RIDE_MENU

    private val APPLICATION_DRIVER_MY_RIDE = """ 
                |$CANCEL_RIDE_MENU
                |$APPLICATION_RIDE_MENU
            """.trimMargin("|")
    val APPLICATION_DRIVER_MY_RIDE_MENU_BEFORE_START = """ 1  -> Start Ride
                |$APPLICATION_DRIVER_MY_RIDE
            """.trimMargin("|")
    val APPLICATION_DRIVER_MY_RIDE_MENU_AFTER_START = """ 1  -> End Ride
                |$APPLICATION_DRIVER_MY_RIDE
            """.trimMargin("|")

    val WELCOME_MESSAGE = """
                                                                                              |-|
       __        __   _                              _________        ____         _ __ (_)   | |
       \ \      / /__| | ___ ___  _ __ ___   __     |___   ___|      |  _ \  __ _ | '_ \ _  __. | ___
        \ \ /\ / / _ \ |/ __/ _ \| '_ ` _ \ / _ \       | |/ _ \     | |_) |/ _` || |_) | |/ _  |/ _ \
         \ V  V /  __/ | (_| (_) | | | | | |  __/       | | (_) |    |  _ <| (_| || .__/| | (_| | (_) |
          \_/\_/ \___|_|\___\___/|_| |_| |_|\___|       |_|\___/     |_| \_\\__,_|| |   |_|\__'_|\___/ 
                                                                                  |_| 
    """.trimIndent()


    val MAP2 = """
        Rapido Map :
                                  |<--------> Mudichor(MU) <------>|                                   |<-----------> Pammal(PM)
        Guduvancheri(GU) <--> Vandalur(VA)                    Tambaram(TM) <--> Chrompet(CH) <--> Pallavaram(PA) <--> InternationalAirport(CIA)
                                  |<------> Perungalathur(PE) <--->|
                                         
            """.trimIndent()

    val MAPOLD = """
    RAPIDO MAP :
                                 ╭───── Mudichor(MU) ─────╮
                                 │                        │
                 ╭───⟶ Vandalur(VA)             Tambaram(TM) ⟵⟶ Chrompet(CH) ⟵⟶ Pallavaram(PA) ──────╮
                 │               │                        │               │                          │
   Guduvancheri(GU)              ╰─── Perungalathur(PE) ──╯               │                          │   
                 │                                                        │   InternationalAirport(CIA)
   Chengalpattu(CHE)        Thiruvanmiyur(TMY) ⟵⟶ Thoraipakkam(TH) ───────╯                          │
                                            │                                                        │
                                     Adyar(AD) ⟵───────⟶ Guindy(GI) ⟵─────⟶ Alandur(AL) ─────────────╯
                                                                 │                   │
       Egmore(EG) ⟵⟶ Vadapalani(VP) ⟵⟶ T. Nagar(TN) ⟵⟶ Saidapet(SA)    Ekktuthangal(EK)
               │                 ╰───────────────────────────────╮                   │
               ╰──── Chetpet(CPT) ⟵⟶ AnnaNagar(AN) ⟵⟶ Koyambedu(KO) ─────────────────╯
               
    """.trimIndent()

    private val y = TextColor.BLUE
    private val r = TextColor.RESET
    private val c = TextColor.CYAN
    private val m = TextColor.MAGENTA
    val MAP = """
        ${y}RAPIDO MAP :${r}
                                     ${c}╭───── ${m}Mudichor(MU)${c} ─────╮
                                     ${c}│                        │
                     ${c}╭───⟶ ${m}Vandalur(VA)${c}             ${m}Tambaram(TM)${c} ⟵⟶ ${m}Chrompet(CH)${c} ⟵⟶ ${m}Pallavaram(PA)${c} ──────╮${c}
                     ${c}│               │                        │               │                          │${c}
       ${m}Guduvancheri(GU)${c}              ╰─── ${m}Perungalathur(PE)${c} ──╯               │                          │${c}   
                     ${c}│                                                        │   ${m}InternationalAirport(CIA)${c}
       ${m}Chengalpattu(CHE)${c}        ${m}Thiruvanmiyur(TMY)${c} ⟵⟶ ${m}Thoraipakkam(TH)${c} ───────╯                          │${c}
                                                ${c}│                                                        │${c}
                                         ${m}Adyar(AD)${c} ⟵───────⟶ ${m}Guindy(GI)${c} ⟵─────⟶ ${m}Alandur(AL)${c} ─────────────╯${c}
                                                                 ${c}│                   │${r}
           ${m}Egmore(EG)${c} ⟵⟶ ${m}Vadapalani(VP)${c} ⟵⟶ ${m}T. Nagar(TN)${c} ⟵⟶ ${m}Saidapet(SA)${c}    ${m}Ekktuthangal(EK)${c}
                   ${c}│                 ╰───────────────────────────────╮                   │${c}
                   ${c}╰──── ${m}Chetpet(CPT)${c} ⟵⟶ ${m}AnnaNagar(AN)${c} ⟵⟶ ${m}Koyambedu(KO)${c} ─────────────────╯${r}
                   
    """.trimIndent()

    val AVAILABLE_PASSENGER_HEAD_TEMP = """
        ╭─────────────────────────────────╮
        │          Available Ride         │
        │────────────────────────────────────────────────────────────────────────────────────────────╮
        │ ID │ Passenger Name             │ Pickup Location      │ Drop Location        │ Price      │
        +────+────────────────────────────+──────────────────────+──────────────────────+────────────+
        """.trimIndent()

    val AVAILABLE_PASSENGER_LIST_TEMP = """
        │ %-2s │ %-26s │ %-20s │ %-20s │ ₹%-9.2f │                                                   
        ╰────────────────────────────────────────────────────────────────────────────────────────────╯
        """.trimIndent()

    val RIDE_DISPLAY_TEMPLATE = """
        ╭────────────────────────────────────────────────────────────────────────────────────────────────╮
        │       passenger = %-20s │                driver = %-30s│
        │────────────────────────────────────────+───────────────────────────────────────────────────────│
        │ Ride =   %-5s ->    %-17s │ RideStatus = %-10s         │ price = %-12s│
        │────────────────────────────────────────+───────────────────────────────────────────────────────│
        │ Start Time: %-26s │ End Time: %-44s│ 
        ╰────────────────────────────────────────+───────────────────────────────────────────────────────╯
        """.trimIndent()
}