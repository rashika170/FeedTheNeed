package com.gdscandroid.loginproject.Restaurant

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.gdscandroid.loginproject.R
import com.gdscandroid.loginproject.Restaurant.dataClass.CompletedOrderRVAdapter
import com.gdscandroid.loginproject.Restaurant.dataClass.UpcomingOrderRVAdapter
import com.gdscandroid.loginproject.Restaurant.dataClass.VolunteerMealsDetailsData
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_completed_meals.*
import kotlinx.android.synthetic.main.fragment_restaurant_meals.*

class CompletedMealsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_completed_meals)

        val dbRef=FirebaseDatabase.getInstance().reference

        val volunteerMealsDetailsData1 = ArrayList<VolunteerMealsDetailsData>()
        val completedOrderRVAdapter= CompletedOrderRVAdapter(volunteerMealsDetailsData1)
        rvCompletedMeals.layoutManager= LinearLayoutManager(this)
        rvCompletedMeals.adapter=completedOrderRVAdapter

        val postListener2= object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val post2: VolunteerMealsDetailsData?=snapshot.getValue(VolunteerMealsDetailsData::class.java)
                if(post2!=null && post2.status.toString()=="done" ){
                    volunteerMealsDetailsData1.add(post2)
                }
                completedOrderRVAdapter.notifyDataSetChanged()

            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {

            }

            override fun onChildRemoved(snapshot: DataSnapshot) {

            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {

            }

            override fun onCancelled(error: DatabaseError) {

            }


        }
        dbRef.child("VolunteerMealsDetails").addChildEventListener(postListener2)
    }
}