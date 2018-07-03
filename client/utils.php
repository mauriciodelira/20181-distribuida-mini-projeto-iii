<?php
include_once "MovieService.php";
$movieService = new MovieService();
$base_url_location = "/sopa-de-filmes/simple";

$listOfMovies = $movieService->listMovies();