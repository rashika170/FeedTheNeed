package com.gdscandroid.loginproject.feeds

import android.R.attr.bitmap
import android.content.Intent
import android.location.Location
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.gdscandroid.loginproject.R
import com.gdscandroid.loginproject.Utility
import kotlinx.android.synthetic.main.feed_item.view.*
import kotlin.math.roundToInt


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
        val loca=feeds[position].Location.toString().split(",")
        hi.location.text=loca[loca.size-4]+", "+loca[loca.size-3]
        hi.rewardsPoints.text=feeds[position].Points+" Points"
        val time=feeds[position].currentTime.toString().split(" ")
        hi.feedTime.text=time[0]
        hi.time.text=time[1]
        hi.role.text=feeds[position].role
        hi.info.text=feeds[position].description

        hi.share.setOnClickListener {
            val myUri:Uri = Uri.parse(feeds[position].feedImage.toString())
            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "text/plain"

            // adding text to share

            // adding text to share
            intent.putExtra(Intent.EXTRA_TEXT, feeds[position].description + " $myUri")

            // Add subject Here


            // setting type to image

            // setting type to image


            // calling startactivity() to share

            // calling startactivity() to share

            holder.itemView.context.startActivity(Intent.createChooser(intent, "Share Via"))
        }


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

        var distance: Double = startPoint.distanceTo(endPoint)/1000.0
        distance=(distance * 100.0).roundToInt() / 100.0
        hi.distance.text=distance.toString()+" KM away"

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