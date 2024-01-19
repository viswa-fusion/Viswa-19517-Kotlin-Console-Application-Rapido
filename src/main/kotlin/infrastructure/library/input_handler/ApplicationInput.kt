package infrastructure.library.input_handler

import app.domain.models.customenum.BikeType
import app.domain.models.customenum.Location
import infrastructure.library.LocalRegex
import presentationlayer.Constants.PROMPT_AADHAAR_NAME
import presentationlayer.Constants.PROMPT_AADHAAR_NUMBER
import presentationlayer.Constants.PROMPT_AADHAAR_RENEWAL_DATE
import presentationlayer.Constants.PROMPT_BIKE_COLOR
import presentationlayer.Constants.PROMPT_BIKE_TYPE
import presentationlayer.Constants.PROMPT_DRIVER_AGE
import presentationlayer.Constants.PROMPT_ENTER_CHOICE
import presentationlayer.Constants.PROMPT_ENTER_CURRENT_LOCATION
import presentationlayer.Constants.PROMPT_ENTER_USERNAME
import presentationlayer.Constants.PROMPT_FULL_NAME
import presentationlayer.Constants.PROMPT_LICENSED_DATE
import presentationlayer.Constants.PROMPT_LICENSE_NO
import presentationlayer.Constants.PROMPT_LICENSE_RENEWAL_DATE
import presentationlayer.Constants.PROMPT_LICENSE_TYPE
import presentationlayer.Constants.PROMPT_LOCATION_DROP_POINT
import presentationlayer.Constants.PROMPT_LOCATION_PICK_POINT
import presentationlayer.Constants.PROMPT_MODEL_NAME
import presentationlayer.Constants.PROMPT_PASSENGER_AGE
import presentationlayer.Constants.PROMPT_PASSWORD
import presentationlayer.Constants.PROMPT_PHONE_NUMBER
import presentationlayer.Constants.PROMPT_RC_BOOK_REG_DATE
import presentationlayer.Constants.PROMPT_RC_OWNER_NAME
import presentationlayer.Constants.PROMPT_RC_RENEWAL_DATE
import presentationlayer.Constants.PROMPT_VEHICLE_REG_NO
import presentationlayer.Constants.PROMPT_VEHICLE_USED_YEAR
import presentationlayer.Constants.RANGE_DRIVER_AGE
import presentationlayer.Constants.RANGE_PASSENGER_AGE
import presentationlayer.Constants.RANGE_PASSWORD
import presentationlayer.Constants.RANGE_USERNAME
import presentationlayer.Constants.RANGE_VEHICLE_USED_YEAR


object ApplicationInput {
    val name = InputHandler(regexPattern = LocalRegex.NAME_PATTERN)
    val date = InputHandler(regexPattern = LocalRegex.DATE_PATTERN)
    val location = InputHandler(allowedValues = enumValues<Location>().map { it.name }, caseSensitive = false)
    val input = InputHandler(prompt = PROMPT_ENTER_CHOICE)
    val inputWithMinusOne = InputHandler(allowMinusOne = true)
    val inputWithEmpty = InputHandler(allowEmpty = true)
    val inputWithMinusOneAndEmpty = InputHandler(allowMinusOne = true, allowEmpty = true)

    fun password() = InputHandler(
        prompt = PROMPT_PASSWORD,
        sizeRange = RANGE_PASSWORD,
        regexPattern = LocalRegex.PASSWORD_PATTERN
    ).getString()

    fun username() = InputHandler(
        prompt = PROMPT_ENTER_USERNAME,
        sizeRange = RANGE_USERNAME,
        regexPattern = LocalRegex.USERNAME_PATTERN
    ).getString()

    fun fullName() = name.copy(prompt = PROMPT_FULL_NAME).getString()

    fun phoneNumber() = InputHandler(prompt = PROMPT_PHONE_NUMBER, regexPattern = LocalRegex.PHONE_PATTERN).getLong()

    fun passengerAge() = InputHandler(prompt = PROMPT_PASSENGER_AGE).getInt(RANGE_PASSENGER_AGE)

    fun driverAge() = InputHandler(prompt = PROMPT_DRIVER_AGE).getInt(RANGE_DRIVER_AGE)

    fun bikeType(): BikeType =
        BikeType.castBikeType(
            InputHandler(
                prompt = PROMPT_BIKE_TYPE,
                allowedValues = enumValues<BikeType>().map { it.name }, caseSensitive = false
            ).getString()
        )!!

    fun licenseNo() = InputHandler(prompt = PROMPT_LICENSE_NO, regexPattern = LocalRegex.LICENSE_PATTERN).getString()

    fun licenseValidFrom() = date.copy(prompt = PROMPT_LICENSED_DATE).getString()

    fun licenseValidTill() = date.copy(prompt = PROMPT_LICENSE_RENEWAL_DATE).getString()

    fun aadhaarNumber() =
        InputHandler(prompt = PROMPT_AADHAAR_NUMBER, regexPattern = LocalRegex.AADHAAR_NUMBER_PATTERN).getString()

    fun aadhaarName() = name.copy(prompt = PROMPT_AADHAAR_NAME).getString()

    fun aadhaarValidTill() = date.copy(prompt = PROMPT_AADHAAR_RENEWAL_DATE).getString()

    fun licenseType() =
        InputHandler(prompt = PROMPT_LICENSE_TYPE, allowedValues = listOf("gear,non-gear"), caseSensitive = false).getString()

    fun vehicleUsedYear() = InputHandler(prompt = PROMPT_VEHICLE_USED_YEAR).getInt(RANGE_VEHICLE_USED_YEAR)

    fun rcOwnerName() = name.copy(prompt = PROMPT_RC_OWNER_NAME).getString()

    fun bikeModel() = InputHandler(prompt = PROMPT_MODEL_NAME).getString()

    fun bikeColor() = InputHandler(prompt = PROMPT_BIKE_COLOR).getString()

    fun rcValidFrom() = date.copy(prompt = PROMPT_RC_BOOK_REG_DATE).getString()

    fun rcValidTill() = date.copy(prompt = PROMPT_RC_RENEWAL_DATE).getString()

    fun vehicleRegNo() = InputHandler(prompt = PROMPT_VEHICLE_REG_NO, regexPattern = LocalRegex.VEHICLE_REG_NO_PATTERN).getString()

    fun gatherCurrentLocation() =
        Location.castLocation(location.copy(prompt = PROMPT_ENTER_CURRENT_LOCATION, allowMinusOne = true).getString())

    fun gatherPickLocation() = Location.castLocation(location.copy(prompt = PROMPT_LOCATION_PICK_POINT).getString())!!

    fun gatherDropLocation() = Location.castLocation(location.copy(prompt = PROMPT_LOCATION_DROP_POINT).getString())!!

}