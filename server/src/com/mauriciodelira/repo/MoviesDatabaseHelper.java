package com.mauriciodelira.repo;

import com.mauriciodelira.exceptions.SoapException;
import com.mauriciodelira.models.Movie;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class MoviesDatabaseHelper {
    private static MoviesDatabaseHelper ourInstance = new MoviesDatabaseHelper();
    private static final String ID = "id";
    private static final String TITLE = "title";
    private static final String DIRECTOR = "director";
    private static final String STUDIO = "studio";
    private static final String GENRE = "genre";
    private static final String LAUNCH_YEAR = "launch_year";
    static List<String> insertColumns = new ArrayList<>(Arrays.asList(TITLE, DIRECTOR, STUDIO, GENRE, LAUNCH_YEAR));
    static String tableName = "movies";

    private MoviesDatabaseHelper() {
    }

    public static MoviesDatabaseHelper getInstance() {
        return ourInstance;
    }

    public final Movie insertInto(Movie movie) throws SoapException {
        Connection connection = DatabaseHelper.getConnection("", "", "", "");

        Movie movieResult;

        if (connection != null) {
            movieResult = findByEquals(connection, TITLE, movie.getTitle());
            if (movieResult != null) {
                DatabaseHelper.closeConnection(connection);
                throw new SoapException("Filme \"" + movie.getTitle() + "\" já cadastrado.");
            } else {
                List<String> values = new ArrayList<>(Arrays.asList(movie.getTitle(), movie.getDirector(), movie.getStudio(), movie.getGenre(), movie.getLaunchYear()));
                Map<String, String> keyVal = new LinkedHashMap<>();

                for (int i = 0; i < insertColumns.size(); i++) {
                    keyVal.put(insertColumns.get(i), values.get(i));
                }

                int res = DatabaseHelper.insertInto(connection, tableName, keyVal);

                if (res > 0) {
                    movieResult = findByEquals(connection, TITLE, movie.getTitle());
                } else {
                    DatabaseHelper.closeConnection(connection);
                    throw new SoapException("Não foi possível cadastrar o filme \"" + movie.getTitle() + "\".");
                }
            }
            DatabaseHelper.closeConnection(connection);
        } else throw new SoapException("Não foi possível conectar-se ao banco de dados.");
        return movieResult;
    }

    public final Movie updateMovieTitle(String id, String newTitle) throws SoapException {
        Connection connection = DatabaseHelper.getConnection("", "", "", "");
        Movie var10000;

        if (connection != null) {

            Movie match = this.findByEquals(connection, ID, id);

            if (match == null) {
                DatabaseHelper.closeConnection(connection);
                throw new SoapException("Filme de ID: \"" + id + "\" não encontrado.");
            } else {
                Map<String, String> fieldsToUpdate = new LinkedHashMap<>();
                Map<String, String> whereClause = new LinkedHashMap<>();

                fieldsToUpdate.put(TITLE, newTitle);
                whereClause.put(ID, match.getId());

                int res = DatabaseHelper.update(connection, tableName, fieldsToUpdate, whereClause);

                if (res > 0) {
                    var10000 = findByEquals(connection, ID, match.getId());
                } else {
                    DatabaseHelper.closeConnection(connection);
                    throw new SoapException("Ocorreu um erro ao atualizar o filme de ID: \"" + id + "\"");
                }

            }

            DatabaseHelper.closeConnection(connection);
        } else throw new SoapException("Não foi possível conectar-se ao banco de dados.");

        return var10000;
    }

    public final Movie deleteMovie(String id) throws SoapException {
        Connection connection = DatabaseHelper.getConnection("", "", "", "");
        Movie var10000;

        if (connection != null) {
            Movie match = this.findByEquals(connection, ID, id);

            if (match == null) {
                DatabaseHelper.closeConnection(connection);
                throw new SoapException("Filme \"" + id + "\" não encontrado.");
            } else {
                Map<String, String> whereCondition = new LinkedHashMap<>();

                whereCondition.put(ID, match.getId());

                int res = DatabaseHelper.delete(connection, tableName, whereCondition);

                if (res > 0) {
                    var10000 = match;
                } else {
                    DatabaseHelper.closeConnection(connection);
                    throw new SoapException("Ocorreu um erro ao apagar o filme \"" + id + "\"");
                }
            }

            DatabaseHelper.closeConnection(connection);
        } else throw new SoapException("Não foi possível conectar-se ao banco de dados.");

        return var10000;
    }

    private Movie findByEquals(Connection connection, String column, String value) {
        try {
            PreparedStatement sql;
            sql = connection.prepareStatement("select * from " + tableName + " where " + column + " = ?");
            sql.setString(1, value.toUpperCase());
            ResultSet var10001 = sql.executeQuery();

            ArrayList<Movie> moviesFound = this.iterateOverResultSet(var10001);

            return moviesFound.size() > 0 ? moviesFound.get(0) : null;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }

    }

    private ArrayList<Movie> findByLike(Connection connection, String column, String value) {
        try {
            PreparedStatement sql;
            sql = connection.prepareStatement("select * from " + tableName + " where " + column + " like ?");
            sql.setString(1, "%" + value.toUpperCase() + "%");
            ResultSet var10001 = sql.executeQuery();
            return this.iterateOverResultSet(var10001);
        } catch (SQLException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }

    }

    public final ArrayList<Movie> listAll() throws SoapException {
        Connection connection = DatabaseHelper.getConnection("", "", "", "");

        if (connection == null) {
            throw new SoapException("Não foi possível conectar-se ao banco de dados.");
        } else {
            try {
                PreparedStatement sql = connection.prepareStatement("SELECT * FROM " + tableName);
                ResultSet var10001 = sql.executeQuery();
                return this.iterateOverResultSet(var10001);
            } catch (SQLException e) {
                e.printStackTrace();
                return new ArrayList<>();
            }
        }
    }

    public final ArrayList<Movie> findByGlobal(String search) throws SoapException {
        Connection connection = DatabaseHelper.getConnection("", "", "", "");

        if (connection != null) {
            Movie byId = this.findByEquals(connection, ID, search);
            ArrayList<Movie> var10000 = new ArrayList<>();
            if (byId == null) {
                ArrayList<Movie> byTitle = this.findByLike(connection, TITLE, search);
                if (byTitle.isEmpty()) {
                    ArrayList<Movie> byDirector = this.findByLike(connection, DIRECTOR, search);
                    if (byDirector.isEmpty()) {
                        ArrayList<Movie> byGenre = this.findByLike(connection, GENRE, search);
                        var10000 = byGenre.isEmpty() ? this.findByLike(connection, LAUNCH_YEAR, search) : byGenre;
                    } else {
                        var10000 = byDirector;
                    }
                } else {
                    var10000 = byTitle;
                }
            } else {
                var10000.add(byId);
            }

            DatabaseHelper.closeConnection(connection);

            return var10000;
        } else {
            throw new SoapException("Não foi possível conectar-se ao banco de dados.");
        }
    }

    private ArrayList<Movie> iterateOverResultSet(ResultSet rs) {
        ArrayList<Movie> movies = new ArrayList<>();
        try {
            while (rs.next()) {
                String var10002 = rs.getString(ID);
                String var10003 = rs.getString(TITLE);
                String var10004 = rs.getString(DIRECTOR);
                String var10005 = rs.getString(STUDIO);
                String var10006 = rs.getString(GENRE);
                String var10007 = rs.getString(LAUNCH_YEAR);
                movies.add(new Movie(var10002, var10003, var10004, var10005, var10006, var10007));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return movies;
    }

}