package com.mauriciodelira.repo

import com.mauriciodelira.models.KtMovie
import java.sql.Connection
import java.sql.ResultSet
import com.mauriciodelira.KtTypeExtensions.getOrElse

object KtMoviesDatabaseHelper {
  val TITLE = "title"
  val DIRECTOR = "director"
  val STUDIO = "studio"
  val GENRE = "genre"
  val LAUNCH_YEAR = "launch_year"
  val allColumns = listOf(TITLE, DIRECTOR, STUDIO, GENRE, LAUNCH_YEAR)
  val tableName = "movies"

  fun insertInto(ktMovie: KtMovie): String {
    val connection: Connection? = KtDatabaseHelper.getConnection()
    val status = if (connection != null) {
      if (KtDatabaseHelper.insertInto(connection,
              tableName,
              allColumns.zip(
                      listOf(ktMovie.title,
                              ktMovie.director,
                              ktMovie.studio,
                              ktMovie.genre,
                              ktMovie.launchYear.toString()
                      )).toMap()) > 0) {
        connection.close()
        "Filme cadastrado com sucesso."
      } else {
        connection.close()
        "Não foi possível cadastrar o filme."
      }
    } else {
      "Conexão com o banco malsucedida."
    }
    return status
  }

  fun findByGlobal(search: String): List<KtMovie> {
    val connection: Connection? = KtDatabaseHelper.getConnection()
    return if(connection != null) {
      findBy("title", search, connection).getOrElse {
        findBy("director", search, connection).getOrElse {
          findBy("genre", search, connection).getOrElse {
            findBy("launch_year", search, connection)
          }
        }
      }
      /*
      val byTitle = findBy(TITLE, search, connection)
      if(byTitle.isEmpty()) {
        val byDirector = findBy(DIRECTOR, search, connection)
        if(byDirector.isEmpty()) {
          val byGenre = findBy(GENRE, search, connection)
          if(byGenre.isEmpty()) {
            findBy(LAUNCH_YEAR, search, connection)
          } else
            byGenre
        } else
          byDirector
      } else byTitle
      */
    } else return listOf()
  }

  fun updateMovieTitle(title: String): KtMovie? {
    val connection: Connection? = KtDatabaseHelper.getConnection()

    return if(connection != null) {
      val match = findBy(TITLE, title, connection).firstOrNull()

      match
    } else {
      null
    }
  }

  private fun findBy(field: String, value: String, connection: Connection): List<KtMovie> {
    val sql = connection.prepareStatement("select * from $tableName where $field = ?")
    sql.setString(1, value)
    return iterateOverResultSet(sql.executeQuery())
  }

  private fun iterateOverResultSet(rs: ResultSet): List<KtMovie> {
    val movies = mutableListOf<KtMovie>()
    while(rs.next()) {
      movies.add(
        KtMovie(
          title = rs.getString(TITLE),
          director = rs.getString(DIRECTOR),
          studio = rs.getString(STUDIO),
          genre = rs.getString(GENRE),
          launchYear = rs.getInt(LAUNCH_YEAR)
        )
      )
    }
    return movies.toList()
  }
}