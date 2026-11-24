package com.bignerdranch.android.vysotskiy_zd3_1

data class Movie (
    val imdbId: String,
    val title: String,
    val year: String,
    val type: String,
    val poster: String)

data class MovieDetails(
    val title: String,
    val year: String,
    val rated: String,
    val runtime: String,
    val genre: String,
    val director: String,
    val actors: String,
    val plot: String,
    val poster: String,
    val imdbRating: String
)