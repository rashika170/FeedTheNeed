package com.gdscandroid.loginproject.Volunteer

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.gdscandroid.loginproject.R
import kotlinx.android.synthetic.main.vol_booked_meals_data.view.*

class VolunteerMealPostRVAdapter (val volMealPostData:ArrayList<VolunteerMealPostData>): RecyclerView.Adapter<VolunteerMealPostRVAdapter.RVViewHolder>(){
    class RVViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RVViewHolder {
        val itemView= LayoutInflater.from(parent.context).inflate(
            R.layout.vol_booked_meals_data,parent,false
        )
        return RVViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: RVViewHolder, position: Int) {
        holder.itemView.restaurantName.text=volMealPostData[position].restaurantName
        holder.itemView.noOfMeals.text=volMealPostData[position].numOfMeals
        holder.itemView.itemPickedTimeVol.text=volMealPostData[position].expectedPickTime
        holder.itemView.restaurantNumber.text=volMealPostData[position].RestaurantPhone
        holder.itemView.StatusVolMeal.text=volMealPostData[position].Status
        if(volMealPostData[position].Status.toString().equals("Cancelled")||
            volMealPostData[position].Status.toString().equals("Verified")){
            holder.itemView.itemVerificationVol.visibility=View.GONE
        }else{
            holder.itemView.itemVerificationVol.visibility=View.VISIBLE
        }

        holder.itemView.itemVerificationVol.setOnClickListener {
            var intent = Intent(holder.itemView.context,VolVerificationActivity::class.java)
            intent.putExtra("meals", volMealPostData[position].numOfMeals!!.toInt())
            intent.putExtra("BookingID",volMealPostData[position].BookingId)
            intent.putExtra("RestauUid",volMealPostData[position].RestaurantUid)
            intent.putExtra("VolunteerUid",volMealPostData[position].VolunteerUid)
            holder.itemView.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = volMealPostData.size
}