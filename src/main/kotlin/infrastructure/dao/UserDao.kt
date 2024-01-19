package infrastructure.dao

import app.domain.models.LoginDetails
import app.domain.models.User


interface UserDao {
    fun insertUser(user: User) : Int
    fun fetchUserId(username: String): Int?
    fun fetchUser(id: Int): User
    fun fetchUserType(userId : Int): User?
    fun isUserNameExist(username: String): Boolean
    fun checkPassword(loginDetails: LoginDetails): Boolean
    fun fetchUserId(user: User): Int
}