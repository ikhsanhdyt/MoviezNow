package com.diavolo.movieznow.ui.details.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.diavolo.model.Review
import com.diavolo.movieznow.databinding.ItemReviewsBinding
import com.ms.square.android.expandabletextview.ExpandableTextView

/**
 * Written with passion by Ikhsan Hidayat on 18/08/2023.
 */
class ReviewListAdapter(val context: Context?, var items: List<Review> = ArrayList()) :
    RecyclerView.Adapter<ReviewListAdapter.ViewHolder>() {
    interface OnItemClickListener {
        fun onItemClick(review: Review, container: View)
    }

    private var onItemClickListener: OnItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemReviewsBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun setOnReviewClickListener(onItemClickListener: OnItemClickListener) {
        this.onItemClickListener = onItemClickListener
    }

    fun fillList(items: List<Review>) {
        this.items += items
        notifyDataSetChanged()
    }

    fun clear() {
        this.items = emptyList()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val review = items[position]
        holder.itemReviewTitle.text = review.author
        holder.itemReviewContent.text = review.content
    }

    class ViewHolder(binding: ItemReviewsBinding) : RecyclerView.ViewHolder(binding.root) {
        val itemReviewTitle: TextView = binding.itemReviewTitle
        val itemReviewContent: ExpandableTextView = binding.itemReviewContent
    }
}