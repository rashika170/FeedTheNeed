package com.gdscandroid.loginproject.Restaurant.dataClass

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.gdscandroid.loginproject.R
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.volunteer_meals_details_item.view.*

class UpcomingOrderRVAdapter(private val volunteerMealsDetailsData: ArrayList< VolunteerMealsDetailsData>) :RecyclerView.Adapter<UpcomingOrderRVAdapter.RVViewHolder>(){
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
        holder.itemView.volunteerMealsPickAtTime.text=volunteerMealsDetailsData[position].expectedPickTime
        holder.itemView.volunteerMealsCancel.setOnClickListener {
            val ref = FirebaseDatabase.getInstance().reference
            ref.child("VolunteerMealPost").child(volunteerMealsDetailsData[holder.adapterPosition].VolunteerUid!!)
                .child(volunteerMealsDetailsData[holder.adapterPosition].BookingId!!).child("Status").setValue("Cancelled")
            ref.child("RestaurantMealPost").child(volunteerMealsDetailsData[holder.adapterPosition].RestaurantUid!!)
                .child(volunteerMealsDetailsData[holder.adapterPosition].BookingId!!).child("Status").setValue("Cancelled")
            ref.child("RestaurantMealsData").child(volunteerMealsDetailsData[holder.adapterPosition].RestaurantUid.toString())
                .addListenerForSingleValueEvent(object :ValueEventListener{
                    override fun onDataChange(snapshot: DataSnapshot) {
                        var leftm:Int = snapshot.child("LeftDonation").value.toString().toInt()
                        leftm += volunteerMealsDetailsData[holder.adapterPosition].numOfMeals.toString().toInt()
                        ref.child("RestaurantMealsData").child(volunteerMealsDetailsData[holder.adapterPosition].RestaurantUid.toString())
                            .child("LeftDonation").setValue(leftm.toString())
                        Toast.makeText(holder.itemView.context,"Meals Cancelled",Toast.LENGTH_SHORT).show()
                    }

                    override fun onCancelled(error: DatabaseError) {
                        TODO("Not yet implemented")
                    }

                })
        }
    }

    override fun getItemCount(): Int =volunteerMealsDetailsData.size
}