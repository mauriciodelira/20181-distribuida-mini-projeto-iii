package com.mauriciodelira.services;

import com.mauriciodelira.exceptions.SoapException;
import com.mauriciodelira.models.Movie;
import com.mauriciodelira.repo.MoviesDatabaseHelper;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import java.util.ArrayList;
import java.util.List;

@WebService(serviceName = "FilmesService")
public class MoviesService {
    private MoviesDatabaseHelper db = MoviesDatabaseHelper.getInstance();

    @WebMethod(operationName = "cadastrarFilme")
    public Movie createMovie(
            @WebParam(name = "titulo") String title,
            @WebParam(name = "diretor") String director,
            @WebParam(name = "estudio") String studio,
            @WebParam(name = "genero") String genre,
            @WebParam(name = "anoLancamento") String launchYear) throws SoapException {
        return db.insertInto(new Movie(title, director, studio, genre, launchYear));
    }

    @WebMethod(operationName = "listarTodos")
    public Movie[] listAll() throws SoapException {
        ArrayList<Movie> movies = db.listAll();
        return movies.toArray(new Movie[movies.size()]);
    }

    @WebMethod(operationName = "buscarFilmes")
    public Movie[] searchMovie(@WebParam(name = "buscarPor") String searchText) throws SoapException {
        ArrayList<Movie> movies = db.findByGlobal(searchText);
        return movies.toArray(new Movie[movies.size()]);
    }

    @WebMethod(operationName = "atualizarFilme")
    @WebResult(name = "filme")
    public Movie updateMovie(
            @WebParam(name = "id") String id,
            @WebParam(name = "novoTitulo") String newTitle) throws SoapException {
        return db.updateMovieTitle(id, newTitle);
    }

    @WebMethod(operationName = "apagarFilme")
    public Movie deleteMovie(@WebParam(name = "id") String id) throws SoapException {
        return db.deleteMovie(id);
    }
}