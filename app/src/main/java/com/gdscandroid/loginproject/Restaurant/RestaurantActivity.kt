package com.gdscandroid.loginproject.Restaurant

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.gdscandroid.loginproject.Donator.DonatorHomeFragment
import com.gdscandroid.loginproject.Donator.DonatorProfileFragment
import com.gdscandroid.loginproject.R
import kotlinx.android.synthetic.main.activity_restaurent.*

class RestaurantActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_restaurent)


        val homeFragment = DonatorHomeFragment()
        val mealsFragment = RestaurantMealsFragment()
        val profileFragment = DonatorProfileFragment()
        val posFragment = RestaurantPOSFragment()

        setCurrentFragment(homeFragment)

        bottomNavigationViewRestaurant.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.home ->setCurrentFragment(homeFragment)
                R.id.meals ->setCurrentFragment(mealsFragment)
                R.id.profile->setCurrentFragment(profileFragment)
                R.id.pos -> setCurrentFragment(posFragment)
            }
            true
        }


    }

    private fun setCurrentFragment(fragment: Fragment) =
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.flFragmentRestaurant,fragment)
            commit()
        }
}
