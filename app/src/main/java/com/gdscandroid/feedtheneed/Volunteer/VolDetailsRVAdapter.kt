package com.gdscandroid.feedtheneed.Volunteer

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.gdscandroid.feedtheneed.R
import kotlinx.android.synthetic.main.vol_details_data.view.*

class VolDetailsRVAdapter(val volDetailsData: ArrayList<VolDetailsData>) : RecyclerView.Adapter<VolDetailsRVAdapter.RVViewHolder>(){
    class RVViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RVViewHolder {
        val itemView= LayoutInflater.from(parent.context).inflate(
            R.layout.vol_details_data,parent,false
        )
        return RVViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: RVViewHolder, position: Int) {
        val hi=holder.itemView
        hi.nameVol.text=volDetailsData[position].name
        hi.phoneVol.text=volDetailsData[position].Phone
        hi.cmtVol.text=volDetailsData[position].cmt
        Glide.with(hi.context).load(volDetailsData[position].Pic).into(hi.volPic)
    }

    override fun getItemCount(): Int =volDetailsData.size
}