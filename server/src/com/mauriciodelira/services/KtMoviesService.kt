package com.mauriciodelira.services

import com.mauriciodelira.models.KtMovie
import com.mauriciodelira.repo.KtMoviesDatabaseHelper

import javax.jws.WebMethod
import javax.jws.WebParam
import javax.jws.WebService

//@WebService(serviceName = "KotlinFilmesService")
open class KtMoviesService {

//  @WebMethod(operationName = "ktCadastrarFilme")
  open fun createMovie(
//          @WebParam(name = "titulo")
          titulo: String,
//          @WebParam(name = "diretor")
          diretor: String,
//          @WebParam(name = "estudio")
          estudio: String,
//          @WebParam(name = "genero")
          genero: String,
//          @WebParam(name = "anoLancamento")
          anoLancamento: Int
  ): String {
    return KtMoviesDatabaseHelper.insertInto(KtMovie(titulo, diretor, estudio, genero, anoLancamento))
  }

//  @WebMethod(operationName = "ktBuscarFilme")
  open fun searchMovie(
//          @WebParam(name = "searchText")
          busca: String): List<KtMovie> {
    return KtMoviesDatabaseHelper.findByGlobal(busca)
  }

//  @WebMethod(operationName = "ktAtualizarFilme")
  open fun updateMovie(
//          @WebParam(name = "titulo")
          titulo: String ): KtMovie? {
    return KtMoviesDatabaseHelper.updateMovieTitle(titulo)
  }
}
