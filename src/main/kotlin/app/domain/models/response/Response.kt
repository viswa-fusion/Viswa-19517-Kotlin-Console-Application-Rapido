package app.domain.models.response

import app.domain.models.User

interface Response {
    fun getResponse(): Int
    fun getResponseMessage(): String
}

sealed class RepositoryResponse(private val responseCode: Int, private val responseMessage: String) : Response {

    object SuccessfullyCreated : RepositoryResponse(200, "INSERT successfully")
    object SuccessfullyUpdated : RepositoryResponse(200, "UPDATE successfully")
    object SuccessfullyDeleted : RepositoryResponse(200, "DELETE successfully")
    data class OperationUnsuccessful(val message: String?) : RepositoryResponse(
        401,
        "Operation Failed"
    )
    object ConnectionFailed : RepositoryResponse(404, "Database connection failed")
    object SignupFailed : RepositoryResponse(404, "unable to signup now try again later")
    object HaveAnotherOtp : RepositoryResponse(404, "have another otp")

    override fun getResponse(): Int {
        return responseCode
    }

    override fun getResponseMessage(): String {
        return responseMessage
    }
}

sealed class AuthenticationResponse(private val responseCode: Int, private val responseMessage: String) : Response {

    data class UserFound(val user: User) : AuthenticationResponse(
        200,
        "Successfully logged in"
    )
    object ValidCredential : AuthenticationResponse(200, "user available")
    object UserNotFound : AuthenticationResponse(404, "User not found")
    object InvalidUsername : AuthenticationResponse(401, "Invalid Username")
    object InvalidPassword : AuthenticationResponse(401, "Invalid Password")

    override fun getResponse(): Int {
        return responseCode
    }

    override fun getResponseMessage(): String {
        return responseMessage
    }
}

