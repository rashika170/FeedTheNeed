package com.gdscandroid.loginproject.Volunteer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.gdscandroid.loginproject.Donator.DonatorHomeFragment
import com.gdscandroid.loginproject.R
import kotlinx.android.synthetic.main.activity_volunteer_home.*

class VolunteerHomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_volunteer_home)

        val volHomeFragment = DonatorHomeFragment()
        val volMealsFragment=VolMealsFragment()
        val volThingsFragment=VolThingsFragment()
        val volEducationFragment=VolEducationFragment()
        val volProfileFragment=VolProfileFragment()

        setCurrentFragment(volHomeFragment)
        bottomNavigationViewVol.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.home->setCurrentFragment(volHomeFragment)
                R.id.meals->setCurrentFragment(volMealsFragment)
                R.id.things->setCurrentFragment(volThingsFragment)
                R.id.education->setCurrentFragment(volEducationFragment)
                R.id.profile->setCurrentFragment(volProfileFragment)

            }
            true
        }


    }

    private fun setCurrentFragment(fragment: Fragment) =
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.flVolFragment,fragment)
            commit()
        }
}