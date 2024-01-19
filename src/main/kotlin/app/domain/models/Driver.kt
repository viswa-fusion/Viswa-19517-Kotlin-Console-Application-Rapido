package app.domain.models

class Driver(
    username: String,
    password: String,
    name: String,
    age: Int,
    phone: Long,
    val license: License,
    val bike: Bike
    ): User(username, password, name, age, phone)
