package infrastructure.database

import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException

object DbConnection {
    private const val url = "jdbc:mysql://localhost:3306/rapido"
    private const val user = "root"
    private const val password = "Hicet@123"
    fun connection(): Connection {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver")
        } catch (e: ClassNotFoundException) {
            e.printStackTrace()
        }

        try {
            return  DriverManager.getConnection(url, user, password)
        } catch (e: SQLException) {
            throw SQLException("database connection issue")
        }
    }
}
