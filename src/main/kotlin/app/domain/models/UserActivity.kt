package app.domain.models

import infrastructure.database.LogStatus

data class UserActivity(val user: User, val stats: LogStatus, val time: String)