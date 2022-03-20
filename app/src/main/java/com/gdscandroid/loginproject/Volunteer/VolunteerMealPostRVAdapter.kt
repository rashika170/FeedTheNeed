package com.gdscandroid.loginproject.Volunteer

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
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