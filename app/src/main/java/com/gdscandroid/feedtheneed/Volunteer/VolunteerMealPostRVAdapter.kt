package com.gdscandroid.feedtheneed.Volunteer

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.gdscandroid.feedtheneed.R
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.edu_advertisment.view.*
import kotlinx.android.synthetic.main.vol_booked_meals_data.view.*
import kotlinx.android.synthetic.main.vol_booked_meals_data.view.imageView1
import kotlinx.android.synthetic.main.vol_booked_meals_data.view.imageView2
import kotlinx.android.synthetic.main.vol_booked_meals_data.view.textView10
import kotlinx.android.synthetic.main.vol_booked_meals_data.view.textView7
import kotlinx.android.synthetic.main.vol_things_item.view.*

class VolunteerMealPostRVAdapter (val volMealPostData:ArrayList<VolunteerMealPostData>): RecyclerView.Adapter<VolunteerMealPostRVAdapter.RVViewHolder>(){
    class RVViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RVViewHolder {
        val itemView= LayoutInflater.from(parent.context).inflate(
            R.layout.vol_booked_meals_data,parent,false
        )
        return RVViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: RVViewHolder, position: Int) {

        val bookId=volMealPostData[position].BookingId.toString()
        var total=0
        for(i in 0..bookId.length-1){
            total+=bookId[i].toInt()
        }
        holder.itemView.textView7.text="#"+total.toString()

        holder.itemView.restaurantName.text=volMealPostData[position].restaurantName
        holder.itemView.noOfMeals.text=volMealPostData[position].numOfMeals

        val time=volMealPostData[position].expectedPickTime.toString().split(",")
        holder.itemView.itemPickedTimeVol.text=time[0]
        holder.itemView.textView10.text=time[1]
//        holder.itemView.itemPickedTimeVol.text=volMealPostData[position].expectedPickTime
        holder.itemView.imageView1.setOnClickListener {
            val permissionCheck =
                ContextCompat.checkSelfPermission(holder.itemView.context as Activity, Manifest.permission.CALL_PHONE)
            if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(
                    holder.itemView.context as Activity, arrayOf(Manifest.permission.CALL_PHONE),
                    123
                )
            } else {
                val intent=Intent(Intent.ACTION_CALL)
                intent.data = Uri.parse("tel:${volMealPostData[position].RestaurantPhone}")
                holder.itemView.context.startActivity(intent)
            }
        }

        holder.itemView.imageView2.setOnClickListener {
//            val laat = Utility.getLatitudeContext(holder.itemView.context)
//            val longgg = Utility.getLongitudeContext(holder.itemView.context)
            val rid = volMealPostData[position].RestaurantUid
            val ref = rid?.let { it1 ->
                FirebaseDatabase.getInstance().reference.child("RestaurantMealsData").child(
                    it1
                )
            }
            ref?.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val laat = snapshot.child("latitude").value
                    val longgg = snapshot.child("longitude").value

                    val intent = Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("http://maps.google.com/maps?daddr="+laat+","+longgg)
                    )
                    holder.itemView.context.startActivity(intent)
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(holder.itemView.context,"Something Went Wrong",Toast.LENGTH_SHORT).show()
                }

            })

        }

        //holder.itemView.restaurantNumber.text=volMealPostData[position].RestaurantPhone
        holder.itemView.StatusVolMeal.text=volMealPostData[position].Status
        Glide.with(holder.itemView.context).load(volMealPostData[position].RestaurPhoto).into(holder.itemView.restauPic)
        if(volMealPostData[position].Status.toString().equals("Cancelled")||
            volMealPostData[position].Status.toString().equals("Verified") ||
            volMealPostData[position].Status.toString().equals("Booked")){
            holder.itemView.itemVerificationVol.visibility=View.GONE
        }else{
            holder.itemView.itemVerificationVol.visibility=View.VISIBLE
        }

        if(volMealPostData[position].Status.toString().equals("Cancelled")){
            holder.itemView.StatusVolMeal.background=holder.itemView.context.resources.getDrawable(R.drawable.distributed_background)
            holder.itemView.StatusVolMeal.setTextColor(Color.parseColor("#000000"))
        }else if(volMealPostData[position].Status.toString().equals("Verified")){
            holder.itemView.StatusVolMeal.background=holder.itemView.context.resources.getDrawable(R.drawable.processing_background)
            holder.itemView.StatusVolMeal.setTextColor(Color.parseColor("#000000"))
        }else if(volMealPostData[position].Status.toString().equals("Booked")){
            holder.itemView.StatusVolMeal.background=holder.itemView.context.resources.getDrawable(R.drawable.cancelled_background)
            holder.itemView.StatusVolMeal.setTextColor(Color.parseColor("#000000"))
        }else{
            holder.itemView.StatusVolMeal.background=holder.itemView.context.resources.getDrawable(R.drawable.collected_background)
            holder.itemView.StatusVolMeal.setTextColor(Color.parseColor("#000000"))
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