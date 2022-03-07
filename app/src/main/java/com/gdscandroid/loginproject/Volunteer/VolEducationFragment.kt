package com.gdscandroid.loginproject.Volunteer

import android.content.Intent
import android.location.Location
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gdscandroid.loginproject.Donator.DonorData
import com.gdscandroid.loginproject.Donator.DonorRVAdapter
import com.gdscandroid.loginproject.R
import com.gdscandroid.loginproject.Utility
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_donator_items.*
import kotlinx.android.synthetic.main.fragment_vol_education.*


class VolEducationFragment : Fragment() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_vol_education, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val dbRef=FirebaseDatabase.getInstance().reference.child("PersonalQuestData")
        val uid= activity?.let { Utility.getUid(it).toString() }

        dbRef.addListenerForSingleValueEvent(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {

                if(snapshot.hasChild(uid.toString())){
                    Log.d("errornahijari",snapshot.toString())
                    activity?.findViewById<CardView>(R.id.cvQuest)?.visibility = View.VISIBLE
                    activity?.findViewById<RecyclerView>(R.id.rvEduAdvertise)?.visibility = View.GONE
                    activity?.findViewById<TextView>(R.id.name_education_p)?.text= activity?.let { Utility.getName(it).toString() }
                    activity?.findViewById<TextView>(R.id.date_education_p)?.text=snapshot.child(uid.toString()).child("time").value.toString()
                    activity?.findViewById<TextView>(R.id.location_education_p)?.text=snapshot.child(uid.toString()).child("Location").value.toString()
                    activity?.findViewById<TextView>(R.id.nov_education_p)?.text=snapshot.child(uid.toString()).child("noofVolunteers").value.toString()
                    activity?.findViewById<TextView>(R.id.info_education_p)?.text=snapshot.child(uid.toString()).child("Info").value.toString()
                    activity?.findViewById<Button>(R.id.volDetails_education_p)?.setOnClickListener {
                        startActivity(Intent(activity,VolDetailsActivity::class.java))
                    }
                    activity?.findViewById<FloatingActionButton>(R.id.floatingActionButtonVolQuest)?.visibility = View.GONE



                }else{
                    activity?.findViewById<FloatingActionButton>(R.id.floatingActionButtonVolQuest)?.visibility = View.VISIBLE
                    activity?.findViewById<CardView>(R.id.cvQuest)?.visibility = View.GONE
                    activity?.findViewById<RecyclerView>(R.id.rvEduAdvertise)?.visibility = View.VISIBLE
                    val dbRef2= FirebaseDatabase.getInstance().reference
                    val eduAdvData=ArrayList<EduAdvertiseData>()
                    var eduAdvertiseRVAdapter= EduAdvertiseRVAdapter(eduAdvData)

                    rvEduAdvertise.layoutManager= LinearLayoutManager(activity)

                    rvEduAdvertise.adapter=eduAdvertiseRVAdapter

                    val postListener= object : ChildEventListener {
                        override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                            val post: EduAdvertiseData?=snapshot.getValue(EduAdvertiseData::class.java)
                            val lati= post?.Latitude
                            val longi=post?.Longitude

                            val startPoint = Location("locationA")
                            if (lati != null) {
                                startPoint.setLatitude(lati.toDouble())
                            }

                            if(longi!=null){
                                startPoint.setLongitude(longi.toDouble())
                            }

                            val endPoint = Location("locationA")
                            endPoint.setLatitude(activity?.let { Utility.getLatitude(it) }!!.toDouble())
                            endPoint.setLongitude(Utility.getLongitude(activity!!)!!.toDouble())

                            val distance: Double = startPoint.distanceTo(endPoint)/1000.0
                            if(post!=null && post.Status!="Complete" && distance<=10){
                                eduAdvData.add(0,post)
                            }

                            eduAdvertiseRVAdapter.notifyDataSetChanged()
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

                    dbRef2.child("QuestData").addChildEventListener(postListener)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })




        floatingActionButtonVolQuest.setOnClickListener{
            val intent =Intent(activity,VolQuestApplyActivity::class.java)
            startActivity(intent)
        }
    }

}