package com.gdscandroid.loginproject.feeds

import android.content.Context
import android.location.Location
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.gdscandroid.loginproject.R
import com.gdscandroid.loginproject.Utility
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
        val hi=holder.itemView
        hi.name.text=feeds[position].name
        hi.location.text=feeds[position].Location
        hi.rewardsPoints.text=feeds[position].Points
        hi.feedTime.text=feeds[position].currentTime
        hi.role.text=feeds[position].role
        hi.info.text=feeds[position].description
        Glide.with(hi.context).load(feeds[position].feedImage).into(hi.feedImage)
        Glide.with(hi.context).load(feeds[position].photo).into(hi.image)
        val lati=feeds[position].Latitude
        val longi=feeds[position].Longitude

        val startPoint = Location("locationA")
        if (lati != null) {
            startPoint.setLatitude(lati.toDouble())
        }
        if(longi!=null){
            startPoint.setLongitude(longi.toDouble())
        }

        val endPoint = Location("locationA")
        endPoint.setLatitude(Utility.getLatitudeContext(hi.context)!!.toDouble())
        endPoint.setLongitude(Utility.getLongitudeContext(hi.context)!!.toDouble())

        val distance: Double = startPoint.distanceTo(endPoint)/1000.0
        hi.distance.text=distance.toString()

//        holder.itemView.volunteerName.text=feeds[position].name
//        val imageUri=feeds[position].image.toString()
//        if (imageUri !== null) Glide.with(holder.itemView)
//            .load(imageUri)
//            .into(holder.itemView.feedImage)
//        else holder.itemView.feedImage.setImageResource(R.drawable.ic_launcher_background)
//        holder.itemView.volunteerLocation.text=feeds[position].location
//        holder.itemView.volunteerFeedTime.text=feeds[position].time
    }

    override fun getItemCount(): Int = feeds.size
}