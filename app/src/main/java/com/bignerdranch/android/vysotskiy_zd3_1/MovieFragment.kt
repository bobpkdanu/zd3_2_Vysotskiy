package com.bignerdranch.android.vysotskiy_zd3_1

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.squareup.picasso.Picasso

class MovieFragment : Fragment(R.layout.fragment_description) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val content: View = view.findViewById(R.id.content)
        val progress: ProgressBar = view.findViewById(R.id.progress)
        val error: TextView = view.findViewById(R.id.error)
        val poster: ImageView = view.findViewById(R.id.detailPoster)
        val back: Button = view.findViewById(R.id.backButton)

        back.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }

        OmdbApi.getDetails(requireContext(), requireArguments().getString(ARG_ID).orEmpty(),
            onSuccess = { d ->
                if (isAdded) {
                    progress.visibility = View.GONE
                    content.visibility = View.VISIBLE

                    view.findViewById<TextView>(R.id.detailTitle).text = d.title
                    view.findViewById<TextView>(R.id.detailYear).text =
                        "${d.year} · ${d.runtime} · IMDb ${d.imdbRating}"
                    view.findViewById<TextView>(R.id.detailGenre).text = d.genre
                    view.findViewById<TextView>(R.id.detailPlot).text = d.plot
                    view.findViewById<TextView>(R.id.detailDirector).text = "Director: ${d.director}"
                    view.findViewById<TextView>(R.id.detailActors).text = "Actors: ${d.actors}"

                    if (d.poster != "N/A" && d.poster.isNotEmpty()) {
                        Picasso.get().load(d.poster).into(poster)
                    }
                    back.requestFocus()
                }
            },
            onError = { message ->
                if (isAdded) {
                    progress.visibility = View.GONE
                    error.text = message
                    error.visibility = View.VISIBLE
                }
            })
    }

    companion object {
        private const val ARG_ID = "imdb_id"

        fun newInstance(imdbId: String) = MovieFragment().apply {
            arguments = Bundle().apply { putString(ARG_ID, imdbId) }
        }
    }
}