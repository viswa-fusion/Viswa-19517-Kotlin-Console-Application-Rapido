package infrastructure.dao

import app.domain.models.License

interface LicenseDao {
    fun insertLicense(license: License): Int
    fun getLicense(id: Int): License
}