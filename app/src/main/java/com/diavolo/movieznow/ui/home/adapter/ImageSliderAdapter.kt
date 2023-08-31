package com.diavolo.movieznow.ui.home.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.diavolo.data.sources.remote.api.ApiClient
import com.diavolo.model.Movie
import com.diavolo.movieznow.common.glide.load
import com.diavolo.movieznow.common.utils.dp
import com.diavolo.movieznow.databinding.ItemSlideBinding

/**
 * Written with passion by Ikhsan Hidayat on 31/08/2023.
 */
class ImageSliderAdapter(val context: Context?, var items: List<Movie> = ArrayList()) :
    RecyclerView.Adapter<ImageSliderViewHolder>() {


    interface OnItemClickListener {
        fun onItemClick(movie: Movie, container: View) // pass ImageView to make shared transition
    }

    private var onItemClickListener: OnItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageSliderViewHolder {
        return ImageSliderViewHolder(
            ItemSlideBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ImageSliderViewHolder, position: Int) {
        val movie = items[position]


        holder.ivSlider.load(
            url = ApiClient.POSTER_BASE_URL + movie.poster_path,
        )
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun setOnSliderClickListener(onItemClickListener: OnItemClickListener) {
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


class ImageSliderViewHolder(binding: ItemSlideBinding) : RecyclerView.ViewHolder(binding.root) {
    val ivSlider: ImageView = binding.ivSlider

}

