package com.gdscandroid.loginproject.Restaurant.dataClass

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.gdscandroid.loginproject.R
import kotlinx.android.synthetic.main.volunteer_meals_details_item.view.*

class CompletedOrderRVAdapter(private val volunteerMealsDetailsData: ArrayList< VolunteerMealsDetailsData>) :RecyclerView.Adapter<CompletedOrderRVAdapter.RVViewHolder>(){
    class RVViewHolder(itemView: View):RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RVViewHolder {
        val itemView= LayoutInflater.from(parent.context).inflate(
            R.layout.volunteer_meals_details_item,parent,false
        )
        return RVViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: RVViewHolder, position: Int) {
        holder.itemView.volunteerName.text=volunteerMealsDetailsData[position].name
        holder.itemView.noOfMeals.text=volunteerMealsDetailsData[position].noOfMeals
        holder.itemView.volunteerMealsPickDate.text=volunteerMealsDetailsData[position].date
        holder.itemView.volunteerMealsPickAtTime.text=volunteerMealsDetailsData[position].pickedAtTime
        holder.itemView.volunteerMealStatus.text=volunteerMealsDetailsData[position].status
        holder.itemView.volunteerMealsVerification.text=volunteerMealsDetailsData[position].verification
    }

    override fun getItemCount(): Int =volunteerMealsDetailsData.size
}