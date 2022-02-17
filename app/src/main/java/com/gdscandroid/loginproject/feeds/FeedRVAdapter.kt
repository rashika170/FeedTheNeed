package com.gdscandroid.loginproject.feeds

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.gdscandroid.loginproject.R
import kotlinx.android.synthetic.main.feed_item.view.*

class FeedRVAdapter(val feeds:ArrayList<FeedData>) : RecyclerView.Adapter<FeedRVAdapter.RVViewHolder>() {
    class RVViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RVViewHolder {
        val itemView= LayoutInflater.from(parent.context).inflate(
            R.layout.feed_item,parent,false
        )
        return RVViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: RVViewHolder, position: Int) {
        holder.itemView.volunteerName.text=feeds[position].name
        val imageUri=feeds[position].image.toString()
        if (imageUri !== null) Glide.with(holder.itemView)
            .load(imageUri)
            .into(holder.itemView.feedImage)
        else holder.itemView.feedImage.setImageResource(R.drawable.ic_launcher_background)
        holder.itemView.volunteerLocation.text=feeds[position].location
        holder.itemView.volunteerFeedTime.text=feeds[position].time
    }

    override fun getItemCount(): Int = feeds.size
}