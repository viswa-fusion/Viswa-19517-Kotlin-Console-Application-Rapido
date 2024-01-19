package infrastructure.dao

import app.domain.models.Aadhaar

interface AadhaarDao {
    fun insertAadhaar(aadhaar: Aadhaar) : Int
    fun getAadhaar(aadhaarId: Int): Aadhaar
}