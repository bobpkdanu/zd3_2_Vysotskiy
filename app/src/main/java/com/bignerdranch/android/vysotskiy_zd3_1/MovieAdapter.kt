package com.bignerdranch.android.vysotskiy_zd3_1

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

class MovieAdapter(
    val context: Context,
    private val onClick: (Movie) -> Unit
) : RecyclerView.Adapter<MovieAdapter.MyVH>() {

    private val all = ArrayList<Movie>()
    private val shown = ArrayList<Movie>()

    fun setItems(items: List<Movie>) {
        all.clear()
        all.addAll(items)
        shown.clear()
        shown.addAll(items)
        notifyDataSetChanged()
    }

    fun filter(query: String) {
        val q = query.trim().lowercase()
        shown.clear()
        shown.addAll(all.filter { q.isEmpty() || it.title.lowercase().contains(q) })
        notifyDataSetChanged()
    }

    class MyVH(val itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.image_quests)
        val title: TextView = itemView.findViewById(R.id.title_quests)
        val descr: TextView = itemView.findViewById(R.id.desc)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyVH {
        val root = LayoutInflater.from(context).inflate(R.layout.activity_recycle, parent, false)
        return MyVH(root)
    }

    override fun onBindViewHolder(holder: MyVH, position: Int) {
        val movie = shown[position]
        holder.title.text = movie.title
        holder.descr.text = "${movie.year} · ${movie.type}"

        if (movie.poster == "N/A" || movie.poster.isEmpty()) {
            holder.imageView.setImageResource(android.R.drawable.ic_menu_gallery)
        } else {
            Picasso.get()
                .load(movie.poster)
                .placeholder(android.R.drawable.ic_menu_gallery)
                .error(android.R.drawable.ic_menu_gallery)
                .into(holder.imageView)
        }

        holder.itemView.setOnClickListener { onClick(movie) }
    }

    override fun getItemCount(): Int = shown.size
}