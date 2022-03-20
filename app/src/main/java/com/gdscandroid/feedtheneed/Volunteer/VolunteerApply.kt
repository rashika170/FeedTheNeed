package com.gdscandroid.feedtheneed.Volunteer

import android.content.Intent
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.gdscandroid.feedtheneed.Utility
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_volunteer_apply.*
import kotlinx.android.synthetic.main.fragment_donator_items.*
import com.gdscandroid.feedtheneed.R

class VolunteerApply : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_volunteer_apply)

        val volunteerData=ArrayList<AvailableRestauData>()
        val volunteerRVAdapter= VolunteerAPllyAdapter(volunteerData)
        rvVolun1.layoutManager= LinearLayoutManager(this)
        rvVolun1.adapter=volunteerRVAdapter

        val ref = Firebase.database.getReference("RestaurantMealsData")
        ref.addChildEventListener(object : ChildEventListener{
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                Log.d("kahokaho",snapshot.key.toString())
                val post: AvailableRestauData? = snapshot.getValue(AvailableRestauData::class.java)
                post!!.uid=snapshot.key
                post.volUid=Utility.getUid(this@VolunteerApply)
                post.volunteerphone = Utility.getMobile(this@VolunteerApply)
                post.VolName = Utility.getName(this@VolunteerApply)
                val lati=post.latitude
                val longi=post.longitude

                val startPoint = Location("locationA")
                if (lati != null) {
                    startPoint.setLatitude(lati.toDouble())
                }
                startPoint.setLongitude(longi!!.toDouble())

                val endPoint = Location("locationA")
                endPoint.setLatitude(Utility.getLatitude(this@VolunteerApply)!!.toDouble())
                endPoint.setLongitude(Utility.getLongitude(this@VolunteerApply)!!.toDouble())

                val distance: Double = startPoint.distanceTo(endPoint)/1000.0
                if (post != null && distance<=20) {
                    volunteerData.add(post)
                }
                volunteerRVAdapter.notifyDataSetChanged()
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                //TODO("Not yet implemented")
                Log.d("sammekam","bhaitriggerhuahe")
                val post: AvailableRestauData? = snapshot.getValue(AvailableRestauData::class.java)
                post!!.uid=snapshot.key
                for(i in 0..volunteerData.size-1){
                    if(volunteerData[i].uid.toString().equals(post.uid.toString())){
                        volunteerData.set(i,post)
                    }
                }

                volunteerRVAdapter.notifyDataSetChanged()
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                //TODO("Not yet implemented")
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                //TODO("Not yet implemented")
            }

            override fun onCancelled(error: DatabaseError) {
                //TODO("Not yet implemented")
            }

        })

    }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this,VolunteerHomeActivity::class.java))
    }
}