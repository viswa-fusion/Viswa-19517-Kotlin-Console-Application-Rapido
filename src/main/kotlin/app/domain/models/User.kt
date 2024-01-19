package app.domain.models


open class User(
    val username: String,
    var password: String,
    val name: String,
    val age: Int,
    val phone: Long,
)
