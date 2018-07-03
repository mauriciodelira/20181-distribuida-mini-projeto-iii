package com.mauriciodelira.models;

public class Movie {
    private String id;
    private String title;
    private String director;
    private String studio;
    private String genre;
    private String launchYear;

    public Movie() {}

    public Movie(String title, String director, String studio, String genre, String launchYear) {
        this.id = null;
        this.title = title;
        this.director = director;
        this.studio = studio;
        this.genre = genre;
        this.launchYear = launchYear;
    }

    public Movie(String id, String title, String director, String studio, String genre, String launchYear) {
        this.id = id;
        this.title = title;
        this.director = director;
        this.studio = studio;
        this.genre = genre;
        this.launchYear = launchYear;
    }

    public final String getId() { return this.id; }

    public final String getTitle() {
        return this.title;
    }

    public final String getDirector() {
        return this.director;
    }

    public final String getStudio() {
        return this.studio;
    }

    public final String getGenre() {
        return this.genre;
    }

    public final String getLaunchYear() {
        return this.launchYear;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public void setStudio(String studio) {
        this.studio = studio;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public void setLaunchYear(String launchYear) {
        this.launchYear = launchYear;
    }

    public String toString() {
        return "Movie(id=" + this.id + ", title=" + this.title + ", director=" + this.director + ", studio=" + this.studio + ", genre=" + this.genre + ", launchYear=" + this.launchYear + ")";
    }
}
