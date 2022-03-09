package com.gdscandroid.loginproject.Restaurant

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.fragment.app.Fragment
import com.gdscandroid.loginproject.Donator.DonatorHomeFragment
import com.gdscandroid.loginproject.Donator.DonatorProfileFragment
import com.gdscandroid.loginproject.MainActivity
import com.gdscandroid.loginproject.R
import com.gdscandroid.loginproject.Utility
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
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
