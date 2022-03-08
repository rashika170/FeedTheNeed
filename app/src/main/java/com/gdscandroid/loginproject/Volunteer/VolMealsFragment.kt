package com.gdscandroid.loginproject.Volunteer

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.gdscandroid.loginproject.Donator.DonatorApply
import com.gdscandroid.loginproject.Donator.DonorData
import com.gdscandroid.loginproject.R
import com.gdscandroid.loginproject.Utility
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.fragment_donator_items.*
import kotlinx.android.synthetic.main.fragment_vol_meals.*


class VolMealsFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_vol_meals, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val dbRef= activity?.let {
            Utility.getUid(it)
                ?.let { FirebaseDatabase.getInstance().reference.child("VolunteerMealPost").child(it) }
        }
        val volMealPostData=ArrayList<VolunteerMealPostData>()
        val volMealPostRVAdapter= VolunteerMealPostRVAdapter(volMealPostData)
        rvVolMeals.layoutManager= LinearLayoutManager(activity)
        rvVolMeals.adapter=volMealPostRVAdapter

        val postListener= object : ChildEventListener{
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                Log.d("bhakk","onChildAdded")
                val post:VolunteerMealPostData?=snapshot.getValue(VolunteerMealPostData::class.java)
                if(post!=null ){
                    volMealPostData.add(0,post)
                }
                volMealPostRVAdapter.notifyDataSetChanged()
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                //TODO("Not yet implemented")
                Log.d("bhakk","onChildChanged")
                val post:VolunteerMealPostData?=snapshot.getValue(VolunteerMealPostData::class.java)
                var ind=-1;
                for(i in 0..volMealPostData.size-1){
                    if(volMealPostData[i].BookingId.toString().equals(post!!.BookingId.toString())){
                        ind = i
                    }
                }
                if(ind!=-1){
                    if (post != null) {
                        volMealPostData.set(ind,post)
                    }
                }

                volMealPostRVAdapter.notifyDataSetChanged()
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

        }

        dbRef?.addChildEventListener(postListener)


        floatingActionButton2.setOnClickListener{
            val intent = Intent (activity, VolunteerApply::class.java)
            startActivity(intent)
            activity?.finish()
        }
    }
}