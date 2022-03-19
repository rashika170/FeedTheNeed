package com.gdscandroid.loginproject.Restaurant

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.gdscandroid.loginproject.R
import com.gdscandroid.loginproject.Utility
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class RestaurantProfileFragment : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        val view = inflater.inflate(R.layout.fragment_restaurant_profile, container, false)
        val image : ImageView = view.findViewById(R.id.home_prof_image)
        val name : TextView = view.findViewById(R.id.home_prof_name)
        val role : TextView = view.findViewById(R.id.home_prof_role)
        val phone : TextView = view.findViewById(R.id.home_prof_phone)
        val loctn : TextView = view.findViewById(R.id.home_prof_lctn)

        Glide.with(view).load(activity?.let { Utility.getProfile(it).toString() }).into(image)
        val navImg: ImageView =view.findViewById(R.id.navImg)
        Glide.with(view).load(activity?.let { Utility.getProfile(it).toString() }).into(navImg)
        name.text = activity?.let { Utility.getName(it).toString() }
        role.text = "."+activity?.let { Utility.getrole(it).toString() }
        phone.text = activity?.let { Utility.getMobile(it).toString() }
        val loc= Utility.getLocation(requireActivity()).toString().split(",")

        loctn.text = loc[loc.size-4]+","+loc[loc.size-3]
        val total = Utility.getDonationPoint(requireActivity())?.let {
            Utility.getRewardoint(requireActivity())
                ?.plus(it)
        }
        view.findViewById<TextView>(R.id.rewardsPoints).text=total.toString()+" Points"

        val dbref=FirebaseDatabase.getInstance().reference.child("RestaurantMealsData")
            .child(Utility.getUid(requireActivity()).toString())
        dbref.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val compl = snapshot.child("CompletedDonation").value.toString()
                val total = snapshot.child("TotalDonation").value.toString()

                view.findViewById<TextView>(R.id.profile_total_donation).text = "Total donations: "+total
                view.findViewById<TextView>(R.id.profile_completed_donation).text = "Completed donations: " +compl

            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })

        return view
    }

}