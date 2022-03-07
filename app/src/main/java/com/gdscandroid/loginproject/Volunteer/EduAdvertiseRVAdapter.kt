package com.gdscandroid.loginproject.Volunteer

import android.app.Dialog
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.EditText
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.gdscandroid.loginproject.R
import com.gdscandroid.loginproject.Utility
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.edu_advertisment.view.*

class EduAdvertiseRVAdapter (val eduAdvertiseData:ArrayList<EduAdvertiseData>): RecyclerView.Adapter<EduAdvertiseRVAdapter.RVViewHolder>(){
    class RVViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RVViewHolder {
        val itemView= LayoutInflater.from(parent.context).inflate(
            R.layout.edu_advertisment,parent,false
        )
        return RVViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: RVViewHolder, position: Int) {
        val hi=holder.itemView
        hi.name_education_p1.text=eduAdvertiseData[position].Leadname.toString()
        hi.date_education_p1.text=eduAdvertiseData[position].time.toString()
        hi.location_education_p1.text=eduAdvertiseData[position].Location.toString()
        hi.leftVol_education_p1.text=eduAdvertiseData[position].LeftVolunteers.toString()
        hi.phone_education_p1.text=eduAdvertiseData[position].Phone.toString()
        hi.info_education_p1.text=eduAdvertiseData[position].Info.toString()
        Glide.with(hi.context).load(eduAdvertiseData[position].LeadPic).into(hi.LeadPic)
        var left=0
        var cmt22=0
        if(eduAdvertiseData[position].LeftVolunteers.toString().toInt()>0){
            left=1
        }
        //Log.d("namebtaojaldi",eduAdvertiseData[position].Comment.toString())
        val cmt : HashMap<String, HashMap<String, String>>? = eduAdvertiseData[position].Comment
        if (cmt != null) {
            if(cmt.containsKey(Utility.getUidContext(hi.context).toString())){

                cmt22=1
            }
        }

        if(cmt22==1 && left==1){
            hi.status_education_p1.text = "Already Applied"
            hi.addComment.visibility = View.GONE
        }else if(cmt22==0 && left==0){
            hi.status_education_p1.text = "All positions are Filled"
            hi.addComment.visibility = View.GONE
        }else if(cmt22==1 && left==0){
            hi.status_education_p1.text = "Already Applied"
            hi.addComment.visibility = View.GONE
        }else{
            hi.status_education_p1.text = "Upcoming"
            hi.addComment.visibility = View.VISIBLE
        }

        hi.addComment.setOnClickListener {
            val dialog = Dialog(holder.itemView.context)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setContentView(R.layout.edu_comment_dialog)
            val cmt = dialog.findViewById(R.id.et_cmt) as EditText
            val bookBtn=dialog.findViewById(R.id.btnComment) as Button
            bookBtn.setOnClickListener {

                val newleft = eduAdvertiseData[position].LeftVolunteers.toString().toInt()-1
                FirebaseDatabase.getInstance().reference.child("PersonalQuestData")
                    .child(eduAdvertiseData[position].VolunteerUid.toString())
                    .child("LeftVolunteers").setValue(newleft.toString())


                FirebaseDatabase.getInstance().reference.child("QuestData")
                    .child(eduAdvertiseData[position].QuestId.toString())
                    .child("LeftVolunteers").setValue(newleft.toString())

                val ref = FirebaseDatabase.getInstance().reference.child("PersonalQuestData")
                    .child(eduAdvertiseData[position].VolunteerUid.toString()).child("Comment")
                    .child(Utility.getUidContext(hi.context).toString())
                ref.child("cmt").setValue(cmt.text.toString())
                ref.child("name").setValue(Utility.getNameContext(hi.context).toString())
                ref.child("Phone").setValue(Utility.getMobileContext(hi.context).toString())
                ref.child("Pic").setValue(Utility.getProfileContext(hi.context).toString())

                val ref1 = FirebaseDatabase.getInstance().reference.child("QuestData")
                    .child(eduAdvertiseData[position].QuestId.toString()).child("Comment")
                    .child(Utility.getUidContext(hi.context).toString())
                ref1.child("cmt").setValue(cmt.text.toString())
                ref1.child("name").setValue(Utility.getNameContext(hi.context).toString())
                ref1.child("Phone").setValue(Utility.getMobileContext(hi.context).toString())
                ref1.child("Pic").setValue(Utility.getProfileContext(hi.context).toString())

                dialog.dismiss()
            }

            dialog.show()
        }
    }

    override fun getItemCount(): Int =eduAdvertiseData.size

}