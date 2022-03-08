package com.gdscandroid.loginproject.Donator

import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.gdscandroid.loginproject.R
import com.gdscandroid.loginproject.Utility
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.doantor_item.view.*
import kotlinx.android.synthetic.main.vol_booked_meals_data.view.*


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
        holder.itemView.itemLocation.text=donorData[position].location
        val imageUri=donorData[position].image.toString()
        if (imageUri !== null) Glide.with(holder.itemView)
            .load(imageUri)
            .into(holder.itemView.feedImage)
        else holder.itemView.feedImage.setImageResource(R.drawable.ic_launcher_background)
        holder.itemView.itemPickedBy.text=donorData[position].pickedBy
        holder.itemView.itemPickedTime.text=donorData[position].pickedTime
        holder.itemView.itemStatus.text=donorData[position].status
        if(donorData[position].status.toString().equals("Booked")){
            holder.itemView.itemvolphone.text=donorData[position].volPhoneNumber
            Glide.with(holder.itemView.context).load(donorData[position].volPic).into(holder.itemView.vol_image)
            holder.itemView.donatorReleaseButton.visibility = View.VISIBLE
            holder.itemView.donatorConfirmation.visibility=View.VISIBLE
        }else{
            holder.itemView.donatorReleaseButton.visibility = View.GONE
            holder.itemView.itemvolphone.visibility = View.GONE
            holder.itemView.vol_image.visibility = View.GONE
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