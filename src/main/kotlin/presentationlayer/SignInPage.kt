package presentationlayer

import app.domain.models.LoginDetails
import infrastructure.library.input_handler.ApplicationInput

internal class SignInPage {
    fun displaySignInPage(): LoginDetails {
        val userName = ApplicationInput.username()
        val password = ApplicationInput.password()
        return LoginDetails(userName, password)
    }
}