package com.gdscandroid.loginproject.Volunteer

import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.gdscandroid.loginproject.R
import com.gdscandroid.loginproject.Utility
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_vol_details.*
import kotlinx.android.synthetic.main.fragment_vol_education.*

class VolDetailsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val dbRef=FirebaseDatabase.getInstance().reference
        setContentView(R.layout.activity_vol_details)
        val volDetailsData=ArrayList<VolDetailsData>()
        var volDetailsRVAdapter= VolDetailsRVAdapter(volDetailsData)

        rvVolDetails.layoutManager= LinearLayoutManager(this)

        rvVolDetails.adapter=volDetailsRVAdapter

        val postListener= object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val post: VolDetailsData?=snapshot.getValue(VolDetailsData::class.java)
                if(post!=null ){
                    volDetailsData.add(0,post)
                }

                volDetailsRVAdapter.notifyDataSetChanged()
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

        dbRef.child("PersonalQuestData").child(Utility.getUid(this).toString()).child("Comment").addChildEventListener(postListener)
    }
}