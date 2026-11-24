package com.bignerdranch.android.vysotskiy_zd3_1

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ChannelFragment : Fragment(R.layout.fragment_channel) {

    private lateinit var adapter: MovieAdapter
    private lateinit var progress: ProgressBar
    private lateinit var empty: TextView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val keyword = requireArguments().getString(ARG_KEYWORD).orEmpty()
        progress = view.findViewById(R.id.progress)
        empty = view.findViewById(R.id.empty)

        val rec: RecyclerView = view.findViewById(R.id.recyclers)
        rec.layoutManager = GridLayoutManager(requireContext(), 3)
        adapter = MovieAdapter(requireContext()) { movie -> openDescription(movie) }
        rec.adapter = adapter

        view.findViewById<EditText>(R.id.search).addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                adapter.filter(s?.toString().orEmpty())
                updateEmpty("Nothing found")
            }
        })

        loadMovies(keyword)
    }

    private fun loadMovies(keyword: String) {
        val cached = cache[keyword]
        if (cached != null) {
            progress.visibility = View.GONE
            adapter.setItems(cached)
            updateEmpty("Nothing found")
            return
        }

        progress.visibility = View.VISIBLE
        empty.visibility = View.GONE

        OmdbApi.searchMovies(requireContext(), keyword,
            onSuccess = { list ->
                if (isAdded) {
                    cache[keyword] = list
                    progress.visibility = View.GONE
                    adapter.setItems(list)
                    updateEmpty("Nothing found")
                }
            },
            onError = { message ->
                if (isAdded) {
                    progress.visibility = View.GONE
                    adapter.setItems(emptyList())
                    updateEmpty(message)
                }
            })
    }

    private fun updateEmpty(text: String) {
        empty.text = text
        empty.visibility =
            if (adapter.itemCount == 0 && progress.visibility != View.VISIBLE) View.VISIBLE
            else View.GONE
    }

    private fun openDescription(movie: Movie) {
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, MovieFragment.newInstance(movie.imdbId))
            .addToBackStack(null)
            .commit()
    }

    companion object {
        private const val ARG_KEYWORD = "keyword"

        private val cache = HashMap<String, List<Movie>>()

        fun newInstance(keyword: String) = ChannelFragment().apply {
            arguments = Bundle().apply { putString(ARG_KEYWORD, keyword) }
        }
    }
}