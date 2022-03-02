package com.gdscandroid.loginproject.Volunteer

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.gdscandroid.loginproject.Donator.DonorData
import com.gdscandroid.loginproject.R
import kotlinx.android.synthetic.main.doantor_item.view.*
import kotlinx.android.synthetic.main.volunteer_restaur_item.view.*


class VolunteerAPllyAdapter(val restaurData:ArrayList<AvailableRestauData>): RecyclerView.Adapter<VolunteerAPllyAdapter.RVViewHolder>() {
    class RVViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RVViewHolder {
        val itemView= LayoutInflater.from(parent.context).inflate(
            R.layout.volunteer_restaur_item,parent,false
        )
        return RVViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: RVViewHolder, position: Int) {
        holder.itemView.restau_name_vri.text=restaurData[position].name
        holder.itemView.nomeals_vri.text=restaurData[position].meals
        holder.itemView.apply_vri.setOnClickListener {

        }
    }

    override fun getItemCount(): Int = restaurData.size
}