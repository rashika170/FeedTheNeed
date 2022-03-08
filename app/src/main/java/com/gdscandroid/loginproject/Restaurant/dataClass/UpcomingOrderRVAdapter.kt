package com.gdscandroid.loginproject.Restaurant.dataClass

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.gdscandroid.loginproject.R
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.volunteer_meals_details_item.view.*
import java.util.*

class UpcomingOrderRVAdapter(private val volunteerMealsDetailsData: ArrayList<VolunteerMealsDetailsData>) :RecyclerView.Adapter<UpcomingOrderRVAdapter.RVViewHolder>(){
    class RVViewHolder(itemView: View):RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RVViewHolder {
        val itemView= LayoutInflater.from(parent.context).inflate(
            R.layout.volunteer_meals_details_item,parent,false
        )
        return RVViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: RVViewHolder, position: Int) {
        holder.itemView.volunteerName.text=volunteerMealsDetailsData[position].VolunteerName
        holder.itemView.volunteerMealStatus.text=volunteerMealsDetailsData[position].Status
        holder.itemView.volNumber.text=volunteerMealsDetailsData[position].VolunteerNumber
        holder.itemView.noOfMeals.text=volunteerMealsDetailsData[position].numOfMeals
        Glide.with(holder.itemView.context).load(volunteerMealsDetailsData[position].VolunteerPhoto).into(holder.itemView.volPhoto)
        holder.itemView.volunteerMealsPickAtTime.text=volunteerMealsDetailsData[position].expectedPickTime

        if(volunteerMealsDetailsData[position].Status=="Picked"){
            holder.itemView.volunteerMealsPicked.visibility=View.GONE
            holder.itemView.volunteerMealsCancel.visibility=View.GONE
        }

        holder.itemView.volunteerMealsPicked.setOnClickListener {
            FirebaseDatabase.getInstance().reference.child("VolunteerMealPost").child(volunteerMealsDetailsData[position].VolunteerUid!!)
                .child(volunteerMealsDetailsData[position].BookingId!!).child("Status").setValue("Picked")
            FirebaseDatabase.getInstance().reference.child("RestaurantMealPost").child(volunteerMealsDetailsData[position].RestaurantUid!!)
                .child(volunteerMealsDetailsData[position].BookingId!!).child("Status").setValue("Picked")
        }

        holder.itemView.volunteerMealsCancel.setOnClickListener {
            val ref = FirebaseDatabase.getInstance().reference
            ref.child("RestaurantMealsData").child(volunteerMealsDetailsData[position].RestaurantUid.toString())
                .addListenerForSingleValueEvent(object :ValueEventListener{
                    override fun onDataChange(snapshot: DataSnapshot) {
                        Log.d("dekhte he",position.toString())
                        var leftm:Int = snapshot.child("LeftDonation").value.toString().toInt()
                        leftm += volunteerMealsDetailsData[position].numOfMeals.toString().toInt()
                        ref.child("RestaurantMealsData").child(volunteerMealsDetailsData[position].RestaurantUid.toString())
                            .child("LeftDonation").setValue(leftm.toString())
                        Toast.makeText(holder.itemView.context,"Meals Cancelled",Toast.LENGTH_SHORT).show()
                        ref.child("VolunteerMealPost").child(volunteerMealsDetailsData[position].VolunteerUid!!)
                            .child(volunteerMealsDetailsData[position].BookingId!!).child("Status").setValue("Cancelled")
                        ref.child("RestaurantMealPost").child(volunteerMealsDetailsData[position].RestaurantUid!!)
                            .child(volunteerMealsDetailsData[position].BookingId!!).child("Status").setValue("Cancelled")
                    }

                    override fun onCancelled(error: DatabaseError) {
                        TODO("Not yet implemented")
                    }

                })
        }

    }

    override fun getItemCount(): Int =volunteerMealsDetailsData.size
}