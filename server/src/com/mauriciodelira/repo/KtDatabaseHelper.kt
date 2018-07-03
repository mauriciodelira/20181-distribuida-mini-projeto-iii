package com.mauriciodelira.repo

import java.sql.Connection
import java.sql.DriverManager

 object KtDatabaseHelper {

  fun getConnection(serverName: String = "localhost",
                    dbName: String = "distribuida",
                    dbUser: String = "distribuida",
                    dbPassword: String = "senhagenerica123"): Connection? {
    val connUrl = "jdbc:mysql://$serverName/$dbName"

    return try {
      val tempConn = DriverManager.getConnection(connUrl, dbUser, dbPassword)

      if (tempConn != null && tempConn.isValid(10))
        tempConn
      else null

    } catch (e: Exception) {
      println("Exceção lançada: ${e.message}")
      println("Conexão mal-sucedida para a URL: [ $connUrl ]")
      null
    }
  }

  fun insertInto(conn: Connection, tableName: String, fieldValueMap: Map<String, String>): Int {
    val fields = fieldValueMap.keys.joinToString(", ", "(", ")")
    val values = fieldValueMap.values.joinToString(", ", "(", ")")

    return conn.prepareStatement("INSERT INTO $tableName $fields VALUES $values")
            .executeUpdate()
  }
}