package com.gdscandroid.loginproject.Volunteer

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.gdscandroid.loginproject.Donator.DonorData
import com.gdscandroid.loginproject.R
import com.gdscandroid.loginproject.Utility
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.vol_things_item.view.*

class VolThingsRVAdapter(val volThingsData:ArrayList<DonorData>): RecyclerView.Adapter<VolThingsRVAdapter.RVViewHolder>(){
    class RVViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RVViewHolder {
        val itemView= LayoutInflater.from(parent.context).inflate(
            R.layout.vol_things_item,parent,false
        )
        return RVViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: RVViewHolder, position: Int) {
        holder.itemView.itemTitleVol.text=volThingsData[position].desc
        holder.itemView.itemLocationVol.text=volThingsData[position].location
        val imageUri=volThingsData[position].image.toString()
        if (imageUri !== null) Glide.with(holder.itemView)
            .load(imageUri)
            .into(holder.itemView.feedImageVol)
        else holder.itemView.feedImageVol.setImageResource(R.drawable.ic_launcher_background)
        holder.itemView.itemPickedTimeVol.text=volThingsData[position].pickedTime
        holder.itemView.itemStatusVol.text=volThingsData[position].status
        holder.itemView.donatorPhn.text=volThingsData[position].donatorPhone
        Glide.with(holder.itemView.context).load(volThingsData[position].donatorPic).into(holder.itemView.doantorImage)
        if(volThingsData[position].status.toString().equals("Booked")){
            holder.itemView.itemBookingVol.visibility=View.GONE
        }else{
            holder.itemView.itemBookingVol.visibility=View.VISIBLE
        }
       holder.itemView.itemBookingVol.setOnClickListener {
           val db=Firebase.database.reference
           val bookRef=db.child("DonatorPost").child(volThingsData[position].bookId.toString())
           bookRef.child("pickedBy").setValue(volThingsData[position].name.toString())
           bookRef.child("status").setValue("Booked")
           bookRef.child("volPic").setValue(Utility.getProfileContext(holder.itemView.context))
           bookRef.child("volPhoneNumber").setValue(Utility.getMobileContext(holder.itemView.context))
       }


    }

    override fun getItemCount(): Int =volThingsData.size
}