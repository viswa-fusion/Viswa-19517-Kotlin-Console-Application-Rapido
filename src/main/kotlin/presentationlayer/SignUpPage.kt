package presentationlayer

import app.domain.models.*
import infrastructure.library.OutputHandler.displayWarning
import infrastructure.library.input_handler.ApplicationInput
import presentationlayer.Constants.DEFAULT_WARNING_USERNAME_ALREADY_EXIST

internal class SignUpPage() {

    private var age: Int? = null
    lateinit var user: User
    lateinit var aadhaar: Aadhaar

    private fun displayUserSignup(isUserNameExist: (String) -> Boolean): User {
        lateinit var username: String
        var response: Boolean
        while (true) {
            username = ApplicationInput.username()
            response = isUserNameExist(username) // check userName already exist
            if (response) displayWarning(DEFAULT_WARNING_USERNAME_ALREADY_EXIST) else break
        }
        val password: String = ApplicationInput.password()
        val name: String = ApplicationInput.fullName()
        val phone: Long = ApplicationInput.phoneNumber()
        return User(username, password, name, age!!, phone)
    }

    fun displayPassengerSignUp(isUserNameExist: (String) -> Boolean): User {
        age = ApplicationInput.passengerAge()
        user = displayUserSignup(isUserNameExist)
        aadhaar = gatherAadhaarData()
        val preferredVehicleType = ApplicationInput.bikeType()
        return Passenger(user.username, user.password, user.name, user.age, user.phone, aadhaar, preferredVehicleType)
    }


    fun displayDriverSignUp(isUserNameExist: (String) -> Boolean): User {
        age = ApplicationInput.driverAge()
        user = displayUserSignup(isUserNameExist)
        val license: License = gatherLicenseData()
        val bike = gatherBikeData()
        return Driver(user.username, user.password, user.name, user.age, user.phone, license, bike)
    }

    private fun gatherLicenseData(): License {
        val licenseNo = ApplicationInput.licenseNo().uppercase()
        val validFrom = ApplicationInput.licenseValidFrom()
        val validTill = ApplicationInput.licenseValidTill()
        val type = ApplicationInput.licenseType()

        return License(licenseNo, validFrom, validTill, type)
    }

    private fun gatherAadhaarData(): Aadhaar {
        val aadhaarNo = ApplicationInput.aadhaarNumber()
        val name: String = ApplicationInput.aadhaarName()
        val renewalDate = ApplicationInput.aadhaarValidTill()

        return Aadhaar(aadhaarNo, name, renewalDate)
    }

    private fun gatherBikeData(): Bike {
        val rcBook = gatherRcBookData()
        val usedYear = ApplicationInput.vehicleUsedYear()
        return Bike(rcBook, usedYear)
    }

    private fun gatherRcBookData(): RcBook {
        val ownerName = ApplicationInput.rcOwnerName()
        val model = ApplicationInput.bikeModel()
        val bikeType = ApplicationInput.bikeType()
        val bikeColor = ApplicationInput.bikeColor()
        val validFrom = ApplicationInput.rcValidFrom()
        val validTill = ApplicationInput.rcValidTill()
        val regNo = ApplicationInput.vehicleRegNo()

        return RcBook(ownerName, model, bikeType, bikeColor, validFrom, validTill, regNo)
    }
}