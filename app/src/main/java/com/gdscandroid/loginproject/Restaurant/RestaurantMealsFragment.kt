package com.gdscandroid.loginproject.Restaurant

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.gdscandroid.loginproject.Donator.DonorData
import com.gdscandroid.loginproject.R
import com.gdscandroid.loginproject.Restaurant.dataClass.RestaurantData
import com.gdscandroid.loginproject.Restaurant.dataClass.UpcomingOrderRVAdapter
import com.gdscandroid.loginproject.Restaurant.dataClass.VolunteerMealsDetailsData
import com.gdscandroid.loginproject.Utility
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.fragment_restaurant_meals.*
import kotlinx.android.synthetic.main.volunteer_meals_details_item.*


class RestaurantMealsFragment : Fragment() {
    val dbRef= FirebaseDatabase.getInstance().reference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_restaurant_meals, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)


        val dbRef= activity?.let {
            Utility.getUid(it)
                ?.let { FirebaseDatabase.getInstance().reference.child("RestaurantMealPost").child(it) }
        }
        val volunteerMealsDetailsData = ArrayList<VolunteerMealsDetailsData>()
        val upcomingOrderRVAdapter=UpcomingOrderRVAdapter(volunteerMealsDetailsData)
        rvUpcomingMeals.layoutManager=LinearLayoutManager(activity)
        rvUpcomingMeals.adapter=upcomingOrderRVAdapter

        val postListener1= object : ChildEventListener{
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val post1:VolunteerMealsDetailsData?=snapshot.getValue(VolunteerMealsDetailsData::class.java)
                if(post1!=null && post1.Status.toString()=="Booked" ){
                    volunteerMealsDetailsData.add(0,post1)
                }
                upcomingOrderRVAdapter.notifyDataSetChanged()

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
        dbRef?.addChildEventListener(postListener1)

   }


    }

