package app.usecase

import app.domain.models.LoginDetails
import app.domain.models.Ride
import app.domain.models.User
import app.domain.models.response.AuthenticationResponse

interface UseCase: PassengerUseCase, DriverUseCase {
    fun addNewUser(user: User) : Boolean
    fun authenticateUser(loginDetails: LoginDetails): AuthenticationResponse
    fun isUserNameExist(username: String): Boolean
    fun generateOtp(ride: Ride): Boolean
}