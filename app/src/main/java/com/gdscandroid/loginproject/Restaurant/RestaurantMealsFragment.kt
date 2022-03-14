package com.gdscandroid.loginproject.Restaurant

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.gdscandroid.loginproject.Donator.DonatorProfileFragment
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
import kotlinx.android.synthetic.main.fragment_donator_items.*
import kotlinx.android.synthetic.main.fragment_restaurant_meals.*
import kotlinx.android.synthetic.main.fragment_restaurant_meals.profile_nav1
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

    private fun setCurrentFragment(fragment: Fragment) =
        activity?.supportFragmentManager?.beginTransaction()?.apply {
            replace(R.id.flFragment,fragment)
            commit()
        }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)


//        profile_nav1.setOnClickListener {
//            setCurrentFragment(DonatorProfileFragment())
//        }

        Glide.with(requireContext()).load(activity?.let { Utility.getProfile(it).toString() }).into(profile_nav1)

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
                val post1:VolunteerMealsDetailsData?=snapshot.getValue(VolunteerMealsDetailsData::class.java)
                var ind=-1
                for(i in 0..volunteerMealsDetailsData.size-1){
                    if(volunteerMealsDetailsData[i].BookingId.toString().equals(post1!!.BookingId.toString())){
                        ind=i
                    }
                }
                if(ind!=-1){
                    volunteerMealsDetailsData.removeAt(ind)
                }
                upcomingOrderRVAdapter.notifyDataSetChanged()

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


