package com.gdscandroid.loginproject.Volunteer

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.gdscandroid.loginproject.Donator.DonatorApply
import com.gdscandroid.loginproject.R
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

        floatingActionButton2.setOnClickListener{
            val intent = Intent (activity, VolunteerApply::class.java)
            startActivity(intent)
        }
    }
}