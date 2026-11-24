package com.bignerdranch.android.vysotskiy_zd3_1

import android.content.Context
import android.net.Uri
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley

object OmdbApi {
    private const val API_KEY = "5dca8348"
    private const val BASE = "https://www.omdbapi.com/"
    private const val PAGES = 3

    private var queue: RequestQueue? = null

    private fun queue(context: Context): RequestQueue =
        queue ?: Volley.newRequestQueue(context.applicationContext).also { queue = it }

    fun searchMovies(
        context: Context,
        keyword: String,
        onSuccess: (List<Movie>) -> Unit,
        onError: (String) -> Unit
    ) {
        loadPage(context, keyword, 1, mutableListOf(), onSuccess, onError)
    }

    private fun loadPage(
        context: Context,
        keyword: String,
        page: Int,
        result: MutableList<Movie>,
        onSuccess: (List<Movie>) -> Unit,
        onError: (String) -> Unit
    ) {
        val url = "$BASE?apikey=$API_KEY&type=movie&page=$page&s=${Uri.encode(keyword)}"

        val request = JsonObjectRequest(Request.Method.GET, url, null,
            { json ->
                if (json.optString("Response") != "True") {
                    if (page == 1) onError(json.optString("Error", "Nothing found"))
                    else onSuccess(result)
                } else {
                    val arr = json.getJSONArray("Search")
                    for (i in 0 until arr.length()) {
                        val o = arr.getJSONObject(i)
                        result.add(
                            Movie(
                                o.optString("imdbID"),
                                o.optString("Title"),
                                o.optString("Year"),
                                o.optString("Type"),
                                o.optString("Poster")
                            )
                        )
                    }
                    if (page < PAGES) loadPage(context, keyword, page + 1, result, onSuccess, onError)
                    else onSuccess(result)
                }
            },
            { error -> onError(error.message ?: "Network error") })

        queue(context).add(request)
    }

    fun getDetails(
        context: Context,
        imdbId: String,
        onSuccess: (MovieDetails) -> Unit,
        onError: (String) -> Unit
    ) {
        val url = "$BASE?apikey=$API_KEY&plot=full&i=${Uri.encode(imdbId)}"

        val request = JsonObjectRequest(Request.Method.GET, url, null,
            { o ->
                if (o.optString("Response") != "True") {
                    onError(o.optString("Error", "Not found"))
                } else {
                    onSuccess(
                        MovieDetails(
                            title = o.optString("Title"),
                            year = o.optString("Year"),
                            rated = o.optString("Rated"),
                            runtime = o.optString("Runtime"),
                            genre = o.optString("Genre"),
                            director = o.optString("Director"),
                            actors = o.optString("Actors"),
                            plot = o.optString("Plot"),
                            poster = o.optString("Poster"),
                            imdbRating = o.optString("imdbRating")
                        )
                    )
                }
            },
            { error -> onError(error.message ?: "Network error") })

        queue(context).add(request)
    }
}