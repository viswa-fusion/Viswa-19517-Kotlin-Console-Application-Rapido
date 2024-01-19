package main

import app.domain.repository.Repository
import app.usecase.UseCase
import app.usecase.UseCaseImplementation
import infrastructure.console.ConsoleApp
import infrastructure.dao.OtpDao
import infrastructure.dao.json.OtpDaoImplement
import infrastructure.dao.sqldatabase.*
import infrastructure.database.DbConnection
import infrastructure.database.MySqlDatabase
import presentationlayer.UiImplementation
import presentationlayer.UserInterface
import utility.UtilityFunction
import java.sql.Connection

fun main() {
    val repository: Repository = createRepository() // Database
    val useCase: UseCase = UseCaseImplementation(repository) // backend
    val userInterface: UserInterface = UiImplementation() // UI

    UtilityFunction.initializeRapidoMap()// initialise rapido map

    val application = ConsoleApp(useCase, userInterface) // Application
    application.run()
}

fun createRepository(): Repository {
    val connection: Connection = DbConnection.connection()

    // Data Access Object for Tables
    val aadhaarDatabase = AadhaarDatabase(connection)
    val licenseDatabase = LicenseDatabase(connection)
    val rcBookDatabase = RcBookDatabase(connection)
    val bikeDatabase = BikeDatabase(rcBookDatabase, connection)
    val passengerDatabase = PassengerDatabase(aadhaarDatabase, connection)
    val driverDatabase = DriverDatabase(licenseDatabase, bikeDatabase, connection)
    val userDatabase = UserDatabase(passengerDatabase, driverDatabase, connection)
    val rideDatabase = RideDatabase(passengerDatabase, driverDatabase, userDatabase, connection)
    val otpJson: OtpDao = OtpDaoImplement()

    // late initialize user Table access to passenger and driver Table
    passengerDatabase.injectUser(userDatabase)
    driverDatabase.injectUser(userDatabase)

    // database object
    return MySqlDatabase(
        userDatabase,
        passengerDatabase,
        driverDatabase,
        rideDatabase,
        aadhaarDatabase,
        licenseDatabase,
        rcBookDatabase,
        bikeDatabase,
        otpJson
    )
}
