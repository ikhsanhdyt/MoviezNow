package com.diavolo.movieznow.ui.home.adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.diavolo.data.sources.remote.api.ApiClient
import com.diavolo.model.Movie
import com.diavolo.movieznow.R
import com.diavolo.movieznow.common.glide.load
import com.diavolo.movieznow.common.utils.dp
import com.diavolo.movieznow.databinding.ItemMovieListBinding

class MovieListAdapter(val context: Context?, var items: List<Movie> = ArrayList()) :
    RecyclerView.Adapter<ViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(movie: Movie, container: View) // pass ImageView to make shared transition
    }

    private var onItemClickListener: OnItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemMovieListBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val movie = items[position]

        holder.tvMovieTitle.text = movie.title

        holder.ivMoviePoster.setImageResource(R.drawable.poster_placeholder)
        holder.ivMoviePoster.transitionName = movie.id.toString()
        holder.llMovieTextContainer.setBackgroundColor(Color.DKGRAY)

        holder.ivMoviePoster.load(
            url = ApiClient.POSTER_BASE_URL + movie.poster_path,
            crossFade = true, width = 160.dp, height = 160.dp
        ) { color ->

            holder.llMovieTextContainer.setBackgroundColor(color)
        }

        holder.cvMovieContainer.setOnClickListener {
            onItemClickListener?.onItemClick(movie, holder.ivMoviePoster)
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun setOnMovieClickListener(onItemClickListener: OnItemClickListener) {
        this.onItemClickListener = onItemClickListener
    }

    fun fillList(items: List<Movie>) {
        this.items += items
        notifyDataSetChanged()
    }

    fun clear() {
        this.items = emptyList()
    }
}

class ViewHolder(binding: ItemMovieListBinding) : RecyclerView.ViewHolder(binding.root) {
    val cvMovieContainer: CardView = binding.cvMovieContainer
    val llMovieTextContainer: LinearLayout = binding.llTextContainer
    val tvMovieTitle: TextView = binding.tvMovieTitle
    val tvMovieDescription: TextView = binding.tvMovieDescription
    val ivMoviePoster: ImageView = binding.ivMoviePoster
}