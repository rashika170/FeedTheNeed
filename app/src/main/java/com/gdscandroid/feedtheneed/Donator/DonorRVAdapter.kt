package com.gdscandroid.feedtheneed.Donator

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.gdscandroid.feedtheneed.R
import com.gdscandroid.feedtheneed.Utility
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.doantor_item.view.*


class DonorRVAdapter(val donorData:ArrayList<DonorData>): RecyclerView.Adapter<DonorRVAdapter.RVViewHolder>() {
    class RVViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RVViewHolder {
        val itemView= LayoutInflater.from(parent.context).inflate(
            R.layout.doantor_item,parent,false
        )
        return RVViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: RVViewHolder, position: Int) {
        Log.d("jabardastikakaam","thak gya hooon vmro")

        holder.itemView.itemTitle.text=donorData[position].desc
//        holder.itemView.itemLocation.text=donorData[position].location
        val imageUri=donorData[position].image.toString()
        Glide.with(holder.itemView)
            .load(imageUri)
            .into(holder.itemView.feedImage)
        val temp = donorData[position].pickedBy?.split(" ")
        //Log.d("newwork",temp.toString())
        holder.itemView.itemPickedBy.text= temp?.get(0) ?: "NoOne"
        val time=donorData[position].pickedTime.toString().split(",")
        holder.itemView.itemPickedTime.text=time[0]
        holder.itemView.time.text=time[1]
        holder.itemView.itemStatus.text=donorData[position].status

        val bookId=donorData[position].bookId.toString()
        var total=0
        for(i in 0..bookId.length-1){
            total+=bookId[i].toInt()
        }
        holder.itemView.donationId.text="#"+total.toString()

        holder.itemView.itemvolphone.setOnClickListener{
            if(donorData[position].status.toString().equals("Booked")){
                Log.d("newwork",donorData[position].uid.toString())
                val permissionCheck =
                    ContextCompat.checkSelfPermission(holder.itemView.context as Activity, Manifest.permission.CALL_PHONE)
                if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(
                        holder.itemView.context as Activity, arrayOf(Manifest.permission.CALL_PHONE),
                        123
                    )
                } else {
                    val intent=Intent(Intent.ACTION_CALL)
                    intent.data = Uri.parse("tel:${donorData[position].volPhoneNumber}")
                    holder.itemView.context.startActivity(intent)
                }
            }else if(donorData[position].status.toString().equals("Picked")){
                Toast.makeText(holder.itemView.context,"Item already Picked",Toast.LENGTH_SHORT).show()
            }
            else{
                Toast.makeText(holder.itemView.context,"Item is not booked",Toast.LENGTH_SHORT).show()
            }
        }

        if(donorData[position].status.toString().equals("Booked")||donorData[position].status.toString().equals("Picked")){
            Glide.with(holder.itemView.context).load(donorData[position].volPic).into(holder.itemView.vol_image)
        }
        //Log.d("newwork",donorData[position].pickedBy+donorData[position].volUid.toString())
        if(donorData[position].status.toString().equals("Booked") ){
            holder.itemView.donatorReleaseButton.visibility = View.VISIBLE
            holder.itemView.donatorConfirmation.visibility=View.VISIBLE

        }else{
            holder.itemView.donatorReleaseButton.visibility = View.GONE
            holder.itemView.itemvolphone.visibility = View.VISIBLE
            holder.itemView.vol_image.visibility = View.VISIBLE
            holder.itemView.donatorConfirmation.visibility=View.GONE
        }
        holder.itemView.donatorConfirmation.setOnClickListener {
            FirebaseDatabase.getInstance().reference.child("DonatorPost")
                .child(donorData[position].bookId.toString()).child("status").setValue("Picked")
            Toast.makeText(holder.itemView.context,"Item Picked by volunteer",Toast.LENGTH_SHORT).show()
            var donationPoints= Utility.getDonationPointContext(holder.itemView.context)
            donationPoints = donationPoints?.plus(100)
            FirebaseDatabase.getInstance().reference.child("Users").
                    child(donorData[position].uid.toString()).child("DonationPoints").setValue(donationPoints)
            if (donationPoints != null) {
                Utility.setDonationPointContext(holder.itemView.context,donationPoints.toLong())
            }
            FirebaseDatabase.getInstance().reference.child("Users").child(donorData[position].volUid.toString()).
                    addListenerForSingleValueEvent(object :ValueEventListener{
                        override fun onDataChange(snapshot: DataSnapshot) {
                            var donationPoints=snapshot.child("DonationPoints").value.toString().toLong()
                            donationPoints+=100
                            FirebaseDatabase.getInstance().reference.child("Users").child(donorData[position].volUid.toString()).
                                child("DonationPoints").setValue(donationPoints)
                        }

                        override fun onCancelled(error: DatabaseError) {
                            TODO("Not yet implemented")
                        }

                    })
        }
        holder.itemView.donatorReleaseButton.setOnClickListener{
            FirebaseDatabase.getInstance().reference.child("DonatorPost")
                .child(donorData[position].bookId.toString()).child("status").setValue("Posted")
            Toast.makeText(holder.itemView.context,"Item Release Successfully",Toast.LENGTH_SHORT).show()
            //holder.itemView.donatorReleaseButton.visibility=View.GONE
        }

    }

    override fun getItemCount(): Int = donorData.size


}