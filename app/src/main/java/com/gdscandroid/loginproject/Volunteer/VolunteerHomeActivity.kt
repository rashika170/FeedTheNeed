package com.gdscandroid.loginproject.Volunteer

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.fragment.app.Fragment
import com.gdscandroid.loginproject.Donator.DonatorHomeFragment
import com.gdscandroid.loginproject.MainActivity
import com.gdscandroid.loginproject.R
import com.gdscandroid.loginproject.Utility
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
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

        feedVol.setOnClickListener {
            feedVol.setImageDrawable(getDrawable(R.drawable.feed_selected))
            volMeals.setImageDrawable(getDrawable(R.drawable.food_donations))
            volEducation.setImageDrawable(getDrawable(R.drawable.quest))
            volDonation.setImageDrawable(getDrawable(R.drawable.donations))
            setCurrentFragment(volHomeFragment)
        }

        volMeals.setOnClickListener {
            feedVol.setImageDrawable(getDrawable(R.drawable.feed))
            volMeals.setImageDrawable(getDrawable(R.drawable.food_doations_selected))
            volEducation.setImageDrawable(getDrawable(R.drawable.quest))
            volDonation.setImageDrawable(getDrawable(R.drawable.donations))
            setCurrentFragment(volMealsFragment)
        }

        volEducation.setOnClickListener {
            feedVol.setImageDrawable(getDrawable(R.drawable.feed))
            volMeals.setImageDrawable(getDrawable(R.drawable.food_donations))
            volEducation.setImageDrawable(getDrawable(R.drawable.quest_selcted))
            volDonation.setImageDrawable(getDrawable(R.drawable.donations))
            setCurrentFragment(volEducationFragment)
        }

        volDonation.setOnClickListener {
            feedVol.setImageDrawable(getDrawable(R.drawable.feed))
            volMeals.setImageDrawable(getDrawable(R.drawable.food_donations))
            volEducation.setImageDrawable(getDrawable(R.drawable.quest))
            volDonation.setImageDrawable(getDrawable(R.drawable.donations_selcted))
            setCurrentFragment(volThingsFragment)
        }

//        bottomNavigationViewVol.setOnNavigationItemSelectedListener {
//            when(it.itemId){
//                R.id.home->setCurrentFragment(volHomeFragment)
//                R.id.meals->setCurrentFragment(volMealsFragment)
//                R.id.things->setCurrentFragment(volThingsFragment)
//                R.id.education->setCurrentFragment(volEducationFragment)
//                R.id.profile->setCurrentFragment(volProfileFragment)
//
//            }
//            true
//        }


    }

    private fun setCurrentFragment(fragment: Fragment) =
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.flVolFragment,fragment)
            commit()
        }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        var inflater : MenuInflater = menuInflater
        inflater.inflate(R.menu.home_menu,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId){
            R.id.logout ->{
                lateinit var mGoogleSignInClient: GoogleSignInClient
                val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken(getString(R.string.default_web_client_id1))
                    .requestEmail()
                    .build()
                mGoogleSignInClient= GoogleSignIn.getClient(this,gso)
                mGoogleSignInClient.signOut().addOnCompleteListener {
                    Log.d("Homeactivit","logout");
                    Firebase.auth.signOut()
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    Utility.setName(this,"")
                    Utility.setLocation(this,"")
                    Utility.setProfile(this,"")
                    Utility.setMobile(this,"")
                    Utility.setUid(this,"")
                    Utility.setRole(this,"")
                    Utility.setRewardoint(this,0)
                    Utility.setDonationPoint(this,0)
                    Utility.setProfileComplete(this,false)
                    Utility.setMealDetail(this,"")
                    Utility.setMealPhotoContext(this,"")
                    finish()
                }
                return true
            }
            else ->{
                return super.onOptionsItemSelected(item)
            }
        }
    }
}