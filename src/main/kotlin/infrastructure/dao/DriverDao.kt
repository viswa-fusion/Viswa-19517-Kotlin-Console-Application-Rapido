package infrastructure.dao

import app.domain.models.response.RepositoryResponse
import app.domain.models.Driver

interface DriverDao {
    fun insertDriver(userId: Int, licenseId: Int, bikeId: Int): RepositoryResponse
    fun getDriver(id: Int): Driver?
    fun getDriverId(userId: Int): Int
    fun injectUser(userDatabase: UserDao)
}