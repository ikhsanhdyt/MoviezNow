package com.diavolo.movieznow.ui.details.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.RecyclerView
import com.diavolo.data.sources.remote.api.ApiClient
import com.diavolo.model.Trailer
import com.diavolo.movieznow.common.glide.load
import com.diavolo.movieznow.databinding.ItemTrailerBinding

/**
 * Written with passion by Ikhsan Hidayat on 18/08/2023.
 */
class TrailerListAdapter(val context: Context?, var items: List<Trailer> = ArrayList()) :
    RecyclerView.Adapter<TrailerListAdapter.ViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(trailer: Trailer, container: View)
    }

    private var onItemClickListener: OnItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemTrailerBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun setOnTrailerClickListener(onItemClickListener: OnItemClickListener) {
        this.onItemClickListener = onItemClickListener
    }

    fun fillList(items: List<Trailer>) {
        this.items += items
        notifyDataSetChanged()
    }

    fun clear() {
        this.items = emptyList()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val trailer = items[position]
        holder.itemVideoTitle.text = trailer.name
        holder.itemVideoCover.load("${ApiClient.YOUTUBE_THUMBNAIL_BASE_URL}${trailer.key}/default.jpg")
    }

    class ViewHolder(binding: ItemTrailerBinding) : RecyclerView.ViewHolder(binding.root) {
        val itemVideoTitle: TextView = binding.itemVideoTitle
        val itemVideoCover: AppCompatImageView = binding.itemVideoCover
    }
}