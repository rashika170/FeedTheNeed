package com.gdscandroid.loginproject.Volunteer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.gdscandroid.loginproject.Donator.DonorData
import com.gdscandroid.loginproject.R
import com.gdscandroid.loginproject.Utility
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_volunteer_apply.*
import kotlinx.android.synthetic.main.fragment_donator_items.*

class VolunteerApply : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_volunteer_apply)

        val volunteerData=ArrayList<AvailableRestauData>()
        val volunteerRVAdapter= VolunteerAPllyAdapter(volunteerData)
        rvVolun1.layoutManager= LinearLayoutManager(this)
        rvVolun1.adapter=volunteerRVAdapter

        val ref = Firebase.database.getReference("RestaurantData")
        ref.addChildEventListener(object : ChildEventListener{
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                Log.d("kahokaho",snapshot.key.toString())
                val post:AvailableRestauData = AvailableRestauData(snapshot.child("name").value.toString(),snapshot.child("mealsLeft").value.toString())
                volunteerData.add(post)
                volunteerRVAdapter.notifyDataSetChanged()
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                TODO("Not yet implemented")
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                TODO("Not yet implemented")
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                TODO("Not yet implemented")
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })

    }
}