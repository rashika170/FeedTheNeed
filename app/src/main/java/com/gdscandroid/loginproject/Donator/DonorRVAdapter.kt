package com.gdscandroid.loginproject.Donator

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.gdscandroid.loginproject.R
import kotlinx.android.synthetic.main.doantor_item.view.*



class DonorRVAdapter(val donorData:ArrayList<DonorData>): RecyclerView.Adapter<DonorRVAdapter.RVViewHolder>() {
    class RVViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RVViewHolder {
        val itemView= LayoutInflater.from(parent.context).inflate(
            R.layout.doantor_item,parent,false
        )
        return RVViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: RVViewHolder, position: Int) {
        holder.itemView.itemTitle.text=donorData[position].desc
        holder.itemView.itemLocation.text=donorData[position].location
        val imageUri=donorData[position].image.toString()
        if (imageUri !== null) Glide.with(holder.itemView)
            .load(imageUri)
            .into(holder.itemView.feedImage)
        else holder.itemView.feedImage.setImageResource(R.drawable.ic_launcher_background)
        holder.itemView.itemPickedBy.text=donorData[position].pickedBy
        holder.itemView.itemPickedTime.text=donorData[position].pickedTime
        holder.itemView.itemStatus.text=donorData[position].status
        holder.itemView.itemVerification.text=donorData[position].verificationLink
    }

    override fun getItemCount(): Int = donorData.size
}