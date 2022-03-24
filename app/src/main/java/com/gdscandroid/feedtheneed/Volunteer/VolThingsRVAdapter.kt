package com.gdscandroid.feedtheneed.Volunteer

import android.Manifest
import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.util.Util
import com.gdscandroid.feedtheneed.Donator.DonorData
import com.gdscandroid.feedtheneed.R
import com.gdscandroid.feedtheneed.Utility
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.vol_things_item.view.*

class VolThingsRVAdapter(val volThingsData:ArrayList<DonorData>): RecyclerView.Adapter<VolThingsRVAdapter.RVViewHolder>(){
    class RVViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RVViewHolder {
        val itemView= LayoutInflater.from(parent.context).inflate(
            R.layout.vol_things_item,parent,false
        )
        return RVViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: RVViewHolder, position: Int) {
        holder.itemView.button.setOnClickListener {
            var dialog:Dialog = Dialog(holder.itemView.context)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setContentView(R.layout.donation_description_dialog)
            val itemImg = dialog.findViewById(R.id.feedImageVol) as ImageView
            val cancelbtn = dialog.findViewById(R.id.cancelDialog) as Button
            val title = dialog.findViewById(R.id.itemTitleVol) as TextView
            val locatn = dialog.findViewById(R.id.itemLocationVol) as TextView
            val date = dialog.findViewById(R.id.itemPickedTimeVol) as TextView
            val time = dialog.findViewById(R.id.time) as TextView
            val dist = dialog.findViewById(R.id.textView9) as TextView

            val lati= volThingsData[position].latitude
            val longi=volThingsData[position].longitude

            val startPoint = Location("locationA")
            if (lati != null) {
                startPoint.setLatitude(lati.toDouble())
            }

            if(longi!=null){
                startPoint.setLongitude(longi.toDouble())
            }

            val endPoint = Location("locationA")
            endPoint.setLatitude(Utility.getLatitudeContext(dialog.context)!!.toDouble())
            endPoint.setLongitude(Utility.getLongitudeContext(dialog.context)!!.toDouble())

            val distance: Double = startPoint.distanceTo(endPoint)/1000.0


            dist.text = String.format("%.2f",distance)+" KM away"

            Log.d("jhol",volThingsData[position].location.toString())
            title.text = volThingsData[position].desc
            locatn.text = volThingsData[position].location
            val imageUri=volThingsData[position].image.toString()
            Glide.with(holder.itemView)
                .load(imageUri)
                .into(itemImg)
            val time2=volThingsData[position].pickedTime.toString().split(",")
            date.text=time2[0]
            time.text=time2[1]
            dialog.show()
            cancelbtn.setOnClickListener {
                dialog.dismiss()
            }
        }
        val bookId=volThingsData[position].bookId.toString()
        var total=0
        for(i in 0..bookId.length-1){
            total+=bookId[i].toInt()
        }
        holder.itemView.textView8.text="#"+total.toString()
        //holder.itemView.itemPickedTimeVol.text=volThingsData[position].pickedTime
        holder.itemView.itemStatusVol.text=volThingsData[position].status
        //holder.itemView.donatorPhn.text=volThingsData[position].donatorPhone

        holder.itemView.textView166.text = volThingsData[position].donatorName

        holder.itemView.imageView11.setOnClickListener {
            val permissionCheck =
                ContextCompat.checkSelfPermission(holder.itemView.context as Activity, Manifest.permission.CALL_PHONE)
            if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(
                    holder.itemView.context as Activity, arrayOf(Manifest.permission.CALL_PHONE),
                    123
                )
            } else {
                val intent= Intent(Intent.ACTION_CALL)
                intent.data = Uri.parse("tel:${volThingsData[position].donatorPhone}")
                holder.itemView.context.startActivity(intent)
            }
        }

        Glide.with(holder.itemView.context).load(volThingsData[position].donatorPic).into(holder.itemView.doantorImage)
        if(volThingsData[position].status.toString().equals("Booked")||volThingsData[position].status.toString().equals("Picked")){
            holder.itemView.itemBookingVol.visibility=View.GONE
        }else{
            holder.itemView.itemBookingVol.visibility=View.VISIBLE
        }
       holder.itemView.itemBookingVol.setOnClickListener {
           val db=Firebase.database.reference
           val bookRef=db.child("DonatorPost").child(volThingsData[position].bookId.toString())
           bookRef.child("pickedBy").setValue(volThingsData[position].name.toString())
           bookRef.child("status").setValue("Booked")
           bookRef.child("volPic").setValue(Utility.getProfileContext(holder.itemView.context))
           bookRef.child("volPhoneNumber").setValue(Utility.getMobileContext(holder.itemView.context))
           bookRef.child("volUid").setValue(Utility.getUidContext(holder.itemView.context))
       }


    }

    override fun getItemCount(): Int =volThingsData.size
}