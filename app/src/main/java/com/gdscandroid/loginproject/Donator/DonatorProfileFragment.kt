package com.gdscandroid.loginproject.Donator

import android.app.Activity
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
import kotlinx.android.synthetic.main.fragment_donator_profile.*


class DonatorProfileFragment : Fragment() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_donator_profile, container, false)
        val image : ImageView = view.findViewById(R.id.home_prof_image)
        val name : TextView = view.findViewById(R.id.home_prof_name)
        val role : TextView = view.findViewById(R.id.home_prof_role)
        val phone : TextView = view.findViewById(R.id.home_prof_phone)
        val loctn : TextView = view.findViewById(R.id.home_prof_lctn)
        Glide.with(view).load(activity?.let { Utility.getProfile(it).toString() }).into(image)
        val navImg:ImageView=view.findViewById(R.id.navImg)
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

        return view
    }



}