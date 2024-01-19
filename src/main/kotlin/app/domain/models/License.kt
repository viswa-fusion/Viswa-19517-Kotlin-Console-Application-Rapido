package app.domain.models

data class License(val licenseNo: String, val validFrom: String, var validTill: String, var type: String)