package com.gdscandroid.loginproject.Volunteer

import android.app.DatePickerDialog
import android.app.Dialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.gdscandroid.loginproject.Donator.DonorData
import com.gdscandroid.loginproject.R
import com.gdscandroid.loginproject.Utility
import com.google.firebase.database.FirebaseDatabase
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
        holder.itemView.nomeals_vri.text=restaurData[position].LeftDonation
        holder.itemView.apply_vri.setOnClickListener {
            val dialog = Dialog(holder.itemView.context)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setContentView(R.layout.book_meal_dialog)
            val numMeals = dialog.findViewById(R.id.et_numMeals) as EditText
            val pickBtn = dialog.findViewById(R.id.btnPickTime) as Button
            val time:String="11:00 P.M."
            val bookBtn=dialog.findViewById(R.id.btnBook) as Button
            bookBtn.setOnClickListener {
                if(numMeals.text.toString().trim().toInt()<=restaurData[position].LeftDonation!!.toInt()){
                    val dbRef=FirebaseDatabase.getInstance().reference.child("RestaurantMealsData").child(restaurData[position].uid.toString())
                    val lMeals=restaurData[position].LeftDonation.toString().toInt()
                    val noMeals=numMeals.text.toString().trim().toInt()
                    dbRef.child("LeftDonation").setValue((lMeals-noMeals).toString())
                    val uid = restaurData[position].volUid
                    val database = FirebaseDatabase.getInstance().reference.child("VolunteerPost").child(
                        uid.toString()
                    ).push()
                    var ranid = database.key.toString()
                    ranid = ranid.substring(1,ranid.length-1)
                    val dbRef1=FirebaseDatabase.getInstance().reference.child("VolunteerMealPost").child(uid.toString()).child(ranid)
                    dbRef1.child("numOfMeals").setValue(noMeals.toString())
                    dbRef1.child("restaurantName").setValue(restaurData[position].name.toString())
                    dbRef1.child("expectedPickTime").setValue(time)
                    dbRef1.child("Status").setValue("Booked")
                    dbRef1.child("RestaurantUid").setValue(restaurData[position].uid)
                    dbRef1.child("BookingId").setValue(ranid)
                    dbRef1.child("RestaurantPhone").setValue(restaurData[position].Restaurphone)
                    dbRef1.child("Verification").setValue("NA")
                    dbRef1.child("VolunteerUid").setValue(restaurData[position].volUid)

                    val dbRef2=FirebaseDatabase.getInstance().reference.child("RestaurantMealPost").child(restaurData[position].uid.toString()).child(ranid)
                    dbRef2.child("numOfMeals").setValue(noMeals.toString())
                    dbRef2.child("VolunteerName").setValue(restaurData[position].VolName.toString())
                    dbRef2.child("expectedPickTime").setValue(time)
                    dbRef2.child("Status").setValue("Booked")
                    dbRef2.child("VolunteerUid").setValue(restaurData[position].volUid)
                    dbRef2.child("BookingId").setValue(ranid)
                    dbRef2.child("VolunteerNumber").setValue(restaurData[position].volunteerphone)
                    dbRef2.child("Verification").setValue("NA")
                    dbRef2.child("RestaurantUid").setValue(restaurData[position].uid)

                    dialog.dismiss()
                }
                notifyDataSetChanged()
            }

            dialog.show()
        }
    }

    override fun getItemCount(): Int = restaurData.size

}